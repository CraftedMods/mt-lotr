package craftedMods.mtLotr.devices;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import craftedMods.mcLib.api.utils.ItemStackMap;
import craftedMods.mtLotr.utils.*;
import lotr.common.recipe.LOTRMillstoneRecipes;
import lotr.common.recipe.LOTRMillstoneRecipes.MillstoneResult;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.*;

@ZenClass(value = "mods.lotr.millstone")
@SuppressWarnings("unchecked")
public class Millstone
{

    private static Map<ItemStack, MillstoneResult> millstoneRecipeMap;

    static
    {
        try
        {
            Field recipeListField = LOTRMillstoneRecipes.class.getDeclaredField ("recipeList");
            recipeListField.setAccessible (true);

            Map<ItemStack, MillstoneResult> oldMap = (Map<ItemStack, MillstoneResult>) recipeListField.get (null);

            Map<ItemStack, MillstoneResult> newMap = ItemStackMap.create (false);
            newMap.putAll (oldMap);

            millstoneRecipeMap = newMap;

            recipeListField.set (null, millstoneRecipeMap);

            MTLotrUtils.logInfo ("Replaced the LOTR millstone recipes map with an item stack sensitive one");
        }
        catch (Exception e)
        {
            MTLotrUtils.logErrorThrowable (
                "Couldn't access the millstone recipe map, recipes for the millstone can't be modified", e);
        }
    }

    public static boolean matches (ItemStack itemstack, ItemStack target)
    {
        return target.getItem () == itemstack.getItem ()
            && (target.getItemDamage () == 32767 || target.getItemDamage () == itemstack.getItemDamage ());
    }

    @ZenMethod
    public static void addRecipe (IItemStack ingredient, IItemStack result)
    {
        addRecipe (ingredient, result, 1.0f);
    }

    @ZenMethod
    public static void addRecipe (IItemStack ingredient, IItemStack result, float chance)
    {
        if (ingredient == null)
        {

            MTLotrUtils.logError ("The ingredient stack for the Millstone recipe to add is null");
            return;
        }
        if (result == null)
        {
            MTLotrUtils.logError ("The result stack for the Millstone recipe to add is null");
            return;
        }
        if (chance < 0 || chance > 1)
        {
            MTLotrUtils.logError ("The result chance (%f) for the Millstone recipe to add is not between 0 and 1",
                chance);
            return;
        }

        if (millstoneRecipeMap != null)
        {
            MineTweakerAPI.apply (
                new Add (MineTweakerMC.getItemStack (ingredient), new MillstoneResult (MineTweakerMC.getItemStack (result), chance)));
        }
    }

    private static class Add extends AbstractUndoableAction
    {

        private final ItemStack ingredient;
        private final MillstoneResult result;

        private MillstoneResult overridenResult;

        public Add (ItemStack ingredient, MillstoneResult result)
        {
            super ("Millstone", "recipe");
            this.ingredient = ingredient;
            this.result = result;
        }

        @Override
        public void apply ()
        {
            if (millstoneRecipeMap.containsKey (ingredient))
                MTLotrUtils.logWarning ("A recipe for the Millstone was overridden with a new one");

            overridenResult = millstoneRecipeMap.put (ingredient, result);

            canUndo = true;
        }

        @Override
        public void undo ()
        {
            if (overridenResult != null)
            {
                millstoneRecipeMap.put (ingredient, overridenResult);
            }
            else
            {
                millstoneRecipeMap.remove (ingredient);
            }
        }

    }

    @ZenMethod
    public static void removeRecipe (IItemStack result)
    {
        removeRecipe (null, result, -1.0f, false, false);
    }

    @ZenMethod
    public static void removeRecipe (IItemStack ingredient, IItemStack result)
    {
        removeRecipe (ingredient, result, -1.0f, true, false);
    }

    @ZenMethod
    public static void removeRecipe (IItemStack ingredient, IItemStack result, float chance)
    {
        removeRecipe (ingredient, result, chance, true, true);
    }

    private static void removeRecipe (IItemStack ingredient, IItemStack result, float chance, boolean useIngredient,
        boolean useChance)
    {
        if (result == null)
        {
            MTLotrUtils.logError ("The result stack for the Millstone recipe to remove is null");
            return;
        }

        if (ingredient == null && useIngredient)
        {
            MTLotrUtils.logError ("The ingredient stack for the Millstone recipe to remove is null");
            return;
        }

        if (useChance && (chance < 0 || chance > 1))
        {
            MTLotrUtils.logError ("The result chance (%f) for the Millstone recipe to remove is not between 0 and 1",
                chance);
            return;
        }

        if (millstoneRecipeMap != null)
        {
            MineTweakerAPI.apply (
                new Remove (MineTweakerMC.getItemStack (ingredient),MineTweakerMC.getItemStack (result), chance, useIngredient,
                    useChance));
        }
    }

    private static class Remove extends AbstractUndoableAction
    {

        private final ItemStack ingredient;
        private final ItemStack result;
        private final float chance;

        private final boolean useIngredient;
        private final boolean useChance;

        private Collection<Map.Entry<ItemStack, MillstoneResult>> removedRecipes;

        public Remove (ItemStack ingredient, ItemStack result, float chance, boolean useIngredient, boolean useChance)
        {
            super ("Millstone", "recipe");
            this.ingredient = ingredient;
            this.result = result;
            this.chance = chance;
            this.useIngredient = useIngredient;
            this.useChance = useChance;
        }

        @Override
        public void apply ()
        {
            Collection<Map.Entry<ItemStack, MillstoneResult>> recipesMatchingByResultStack = millstoneRecipeMap
                .entrySet ().stream ()
                .filter (entry -> matches (entry.getValue ().resultItem, result))
                .collect (Collectors.toList ());

            if (recipesMatchingByResultStack.isEmpty ())
            {
                MTLotrUtils.logError ("No Millstone recipes matched to the specified result item");
                canUndo = false;
            }
            else
            {
                if (useIngredient)
                {
                    recipesMatchingByResultStack = recipesMatchingByResultStack.stream ()
                        .filter (entry -> matches (ingredient, entry.getKey ()))
                        .collect (Collectors.toList ());
                }

                if (recipesMatchingByResultStack.isEmpty ())
                {
                    MTLotrUtils.logError ("No Millstone recipes matched to the specified ingredient item");
                    canUndo = false;
                }
                else
                {
                    if (useChance)
                    {
                        recipesMatchingByResultStack = recipesMatchingByResultStack.stream ()
                            .filter (entry -> chance == entry.getValue ().chance)
                            .collect (Collectors.toList ());
                    }

                    if (recipesMatchingByResultStack.isEmpty ())
                    {
                        MTLotrUtils.logError ("No Millstone recipes matched to the specified result chance");
                        canUndo = false;
                    }
                    else
                    {
                        recipesMatchingByResultStack.forEach (entry -> millstoneRecipeMap.remove (entry.getKey ()));
                        removedRecipes = recipesMatchingByResultStack;
                        canUndo = true;
                    }
                }
            }
        }

        @Override
        public void undo ()
        {
            int overriddenRecipes = 0;
            for (Map.Entry<ItemStack, MillstoneResult> removedRecipe : removedRecipes)
            {
                if (millstoneRecipeMap.containsKey (removedRecipe.getKey ()))
                    ++overriddenRecipes;
                millstoneRecipeMap.put (removedRecipe.getKey (), removedRecipe.getValue ());
            }

            if (overriddenRecipes > 0)
                MTLotrUtils.logWarning ("%d Millstone recipes which were removed got replaced with a new one",
                    overriddenRecipes);
        }

    }

}
