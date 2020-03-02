/*******************************************************************************
 * Copyright (C) 2020 CraftedMods (see https://github.com/CraftedMods)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package craftedMods.mtLotr.devices;

import java.util.*;

import minetweaker.*;
import minetweaker.api.item.*;
import minetweaker.api.minecraft.MineTweakerMC;
import minetweaker.api.recipes.*;
import minetweaker.mc1710.recipes.*;
import minetweaker.mc1710.util.MineTweakerHacks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraftforge.oredict.*;
import stanhebben.zenscript.annotations.ZenMethod;

public abstract class FactionCraftingTable
{
    private final List<IRecipe> recipes;

    protected FactionCraftingTable (List<IRecipe> recipes)
    {
        this.recipes = recipes;
    }

    public void addShapedInternal (IItemStack output, IIngredient[][] ingredients)
    {
        this.addShaped (output, ingredients, null, false);
    }

    public void addShapedMirroredInternal (IItemStack output, IIngredient[][] ingredients)
    {
        this.addShaped (output, ingredients, null, true);
    }

    public void addShapelessInternal (IItemStack output, IIngredient[] ingredients)
    {
        ShapelessRecipe recipe = new ShapelessRecipe (output, ingredients, null);
        IRecipe irecipe = RecipeConverter.convert ((ShapelessRecipe) recipe);
        MineTweakerAPI.apply ((IUndoableAction) new ActionAddRecipe (irecipe, (ICraftingRecipe) recipe));
    }

    public int removeShapedInternal (IIngredient output, IIngredient[][] ingredients)
    {
        int ingredientsWidth = 0;
        int ingredientsHeight = 0;
        if (ingredients != null)
        {
            ingredientsHeight = ingredients.length;
            for (int i = 0; i < ingredients.length; ++i)
            {
                ingredientsWidth = Math.max (ingredientsWidth, ingredients[i].length);
            }
        }
        ArrayList<IRecipe> toRemove = new ArrayList<IRecipe> ();
        ArrayList<Integer> removeIndex = new ArrayList<Integer> ();
        block1: for (int i = 0; i < this.recipes.size (); ++i)
        {
            IRecipe recipe = this.recipes.get (i);
            if (recipe.getRecipeOutput () == null
                || !output.matches (MineTweakerMC.getIItemStack ((ItemStack) recipe.getRecipeOutput ())))
                continue;
            if (ingredients != null)
            {
                if (recipe instanceof ShapedRecipes)
                {
                    ShapedRecipes srecipe = (ShapedRecipes) recipe;
                    if (ingredientsWidth != srecipe.recipeWidth || ingredientsHeight != srecipe.recipeHeight)
                        continue;
                    for (int j = 0; j < ingredientsHeight; ++j)
                    {
                        IIngredient[] row = ingredients[j];
                        for (int k = 0; k < ingredientsWidth; ++k)
                        {
                            ItemStack recipeIngredient = srecipe.recipeItems[j * srecipe.recipeWidth + k];
                            IIngredient ingredient = k > row.length ? null : row[k];
                            if (!matches ((Object) recipeIngredient, ingredient))
                                continue block1;
                        }
                    }
                }
                else if (recipe instanceof ShapedOreRecipe)
                {
                    ShapedOreRecipe srecipe = (ShapedOreRecipe) recipe;
                    int recipeWidth = MineTweakerHacks.getShapedOreRecipeWidth ((ShapedOreRecipe) srecipe);
                    int recipeHeight = srecipe.getRecipeSize () / recipeWidth;
                    if (ingredientsWidth != recipeWidth || ingredientsHeight != recipeHeight)
                        continue;
                    for (int j = 0; j < ingredientsHeight; ++j)
                    {
                        IIngredient[] row = ingredients[j];
                        for (int k = 0; k < ingredientsWidth; ++k)
                        {
                            IIngredient ingredient = k > row.length ? null : row[k];
                            Object input = srecipe.getInput ()[j * recipeWidth + k];
                            if (!matches (input, ingredient))
                                continue block1;
                        }
                    }
                }
            }
            else if (! (recipe instanceof ShapedRecipes) && ! (recipe instanceof ShapedOreRecipe))
                continue;
            toRemove.add (recipe);
            removeIndex.add (i);
        }
        MineTweakerAPI.apply ((IUndoableAction) new ActionRemoveRecipes (toRemove, removeIndex));
        return toRemove.size ();
    }

    @ZenMethod
    public int removeShapelessInternal (IIngredient output, IIngredient[] ingredients, boolean wildcard)
    {
        ArrayList<IRecipe> toRemove = new ArrayList<IRecipe> ();
        ArrayList<Integer> removeIndex = new ArrayList<Integer> ();
        block0: for (int i = 0; i < this.recipes.size (); ++i)
        {
            IRecipe recipe = this.recipes.get (i);
            if (recipe.getRecipeOutput () == null
                || !output.matches (MineTweakerMC.getIItemStack ((ItemStack) recipe.getRecipeOutput ())))
                continue;
            if (ingredients != null)
            {
                if (recipe instanceof ShapelessRecipes)
                {
                    ShapelessRecipes srecipe = (ShapelessRecipes) recipe;
                    if (ingredients.length > srecipe.getRecipeSize ()
                        || !wildcard && ingredients.length < srecipe.getRecipeSize ())
                        continue;
                    block1: for (int j = 0; j < ingredients.length; ++j)
                    {
                        for (int k = 0; k < srecipe.getRecipeSize (); ++k)
                        {
                            if (matches (srecipe.recipeItems.get (k), ingredients[j]))
                                continue block1;
                        }
                        continue block0;
                    }
                }
                else if (recipe instanceof ShapelessOreRecipe)
                {
                    ShapelessOreRecipe srecipe = (ShapelessOreRecipe) recipe;
                    ArrayList<?> inputs = srecipe.getInput ();
                    if (inputs.size () < ingredients.length || !wildcard && inputs.size () > ingredients.length)
                        continue;
                    block3: for (int j = 0; j < ingredients.length; ++j)
                    {
                        for (int k = 0; k < srecipe.getRecipeSize (); ++k)
                        {
                            if (matches (inputs.get (k), ingredients[j]))
                                continue block3;
                        }
                        continue block0;
                    }
                }
            }
            else if (! (recipe instanceof ShapelessRecipes) && ! (recipe instanceof ShapelessOreRecipe))
                continue;
            toRemove.add (recipe);
            removeIndex.add (i);
        }
        MineTweakerAPI.apply ((IUndoableAction) new ActionRemoveRecipes (toRemove, removeIndex));
        return toRemove.size ();
    }

    private void addShaped (IItemStack output, IIngredient[][] ingredients, IRecipeFunction function, boolean mirrored)
    {
        ShapedRecipe recipe = new ShapedRecipe (output, ingredients, function, mirrored);
        IRecipe irecipe = RecipeConverter.convert ((ShapedRecipe) recipe);
        MineTweakerAPI.apply ((IUndoableAction) new ActionAddRecipe (irecipe, (ICraftingRecipe) recipe));
    }

    private static boolean matches (Object input, IIngredient ingredient)
    {
        if (input == null != (ingredient == null)){ return false; }
        if (ingredient != null && (input instanceof ItemStack
            ? !ingredient.matches (MineTweakerMC.getIItemStack ((ItemStack) ((ItemStack) input)))
            : input instanceof String
                && !ingredient
                    .contains ((IIngredient) MineTweakerMC.getOreDict ((String) ((String) input))))){ return false; }
        return true;
    }

    private class ActionAddRecipe implements IUndoableAction
    {
        private final IRecipe recipe;
        // private final ICraftingRecipe craftingRecipe;

        public ActionAddRecipe (IRecipe recipe, ICraftingRecipe craftingRecipe)
        {
            this.recipe = recipe;
            // this.craftingRecipe = craftingRecipe;
        }

        public void apply ()
        {
            recipes.add (this.recipe);
        }

        public boolean canUndo ()
        {
            return true;
        }

        public void undo ()
        {
            recipes.remove ((Object) this.recipe);
        }

        public String describe ()
        {
            return "Adding recipe for " + this.recipe.getRecipeOutput ().getDisplayName ();
        }

        public String describeUndo ()
        {
            return "Removing recipe for " + this.recipe.getRecipeOutput ().getDisplayName ();
        }

        public Object getOverrideKey ()
        {
            return null;
        }
    }

    private class ActionRemoveRecipes implements IUndoableAction
    {
        private final List<Integer> removingIndices;
        private final List<IRecipe> removingRecipes;

        public ActionRemoveRecipes (List<IRecipe> recipes, List<Integer> indices)
        {
            this.removingIndices = indices;
            this.removingRecipes = recipes;
        }

        public void apply ()
        {
            for (int i = this.removingIndices.size () - 1; i >= 0; --i)
            {
                recipes.remove ((int) this.removingIndices.get (i));
            }
        }

        public boolean canUndo ()
        {
            return true;
        }

        public void undo ()
        {
            for (int i = 0; i < this.removingIndices.size (); ++i)
            {
                int index = Math.min (recipes.size (), this.removingIndices.get (i));
                recipes.add (index, this.removingRecipes.get (i));
            }
        }

        public String describe ()
        {
            return "Removing " + this.removingIndices.size () + " recipes";
        }

        public String describeUndo ()
        {
            return "Restoring " + this.removingIndices.size () + " recipes";
        }

        public Object getOverrideKey ()
        {
            return null;
        }
    }

}
