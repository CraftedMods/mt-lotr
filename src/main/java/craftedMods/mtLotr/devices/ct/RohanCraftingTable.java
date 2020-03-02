package craftedMods.mtLotr.devices.ct;

import craftedMods.mtLotr.devices.FactionCraftingTable;
import lotr.common.recipe.LOTRRecipes;
import minetweaker.api.item.*;
import minetweaker.api.recipes.IRecipeFunction;
import stanhebben.zenscript.annotations.*;

@ZenClass(value = "mods.lotr.ct.rohan")
public class RohanCraftingTable extends FactionCraftingTable
{

    private static final RohanCraftingTable instance;

    static
    {
        instance = new RohanCraftingTable ();
    }

    protected RohanCraftingTable ()
    {
        super (LOTRRecipes.rohirricRecipes);
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
