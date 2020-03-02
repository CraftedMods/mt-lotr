package craftedMods.mtLotr.devices.ct;

import craftedMods.mtLotr.devices.FactionCraftingTable;
import lotr.common.recipe.LOTRRecipes;
import minetweaker.api.item.*;
import minetweaker.api.recipes.IRecipeFunction;
import stanhebben.zenscript.annotations.*;

@ZenClass(value = "mods.lotr.ct.rhun")
public class RhunCraftingTable extends FactionCraftingTable
{

    private static final RhunCraftingTable instance;

    static
    {
        instance = new RhunCraftingTable ();
    }

    protected RhunCraftingTable ()
    {
        super (LOTRRecipes.rhunRecipes);
    }

    @ZenMethod
    public static void addShaped (IItemStack output, IIngredient[][] ingredients, IRecipeFunction function)
    {
        instance.addShapedInternal (output, ingredients, function);
    }

    @ZenMethod
    public static void addShapedMirrored (IItemStack output, IIngredient[][] ingredients, IRecipeFunction function)
    {
        instance.addShapedMirroredInternal (output, ingredients, function);
    }

    @ZenMethod
    public static void addShapeless (IItemStack output, IIngredient[] ingredients, IRecipeFunction function)
    {
        instance.addShapelessInternal (output, ingredients, function);
    }

    @ZenMethod
    public static int removeShapeless (IIngredient output, IIngredient[] ingredients, boolean wildcard)
    {
        return instance.removeShapelessInternal (output, ingredients, wildcard);
    }

    @ZenMethod
    public static int removeShaped (IIngredient output, IIngredient[][] ingredients)
    {
        return instance.removeShapedInternal (output, ingredients);
    }

}