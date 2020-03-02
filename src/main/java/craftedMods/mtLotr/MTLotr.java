package craftedMods.mtLotr;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import craftedMods.mtLotr.devices.Millstone;
import craftedMods.mtLotr.devices.ct.*;
import minetweaker.MineTweakerAPI;

@Mod(modid = "mt-lotr", name = "MineTweaker Integration for the LOTR Mod", version = "1.0.0-BETA")
public class MTLotr
{

    public static final String PREFIX = "[MT LOTR]:";

    @EventHandler
    public void onPreInit (FMLPreInitializationEvent event)
    {
        // Faction crafting tables
        MineTweakerAPI.registerClass (AngmarCraftingTable.class);
        MineTweakerAPI.registerClass (BlueDwarvenCraftingTable.class);
        MineTweakerAPI.registerClass (DaleCraftingTable.class);
        MineTweakerAPI.registerClass (DolAmrothCraftingTable.class);
        MineTweakerAPI.registerClass (DolGuldurCraftingTable.class);
        MineTweakerAPI.registerClass (DorwinionCraftingTable.class);
        MineTweakerAPI.registerClass (DunlendingCraftingTable.class);
        MineTweakerAPI.registerClass (DwarvenCraftingTable.class);
        MineTweakerAPI.registerClass (GaladhrimCraftingTable.class);
        MineTweakerAPI.registerClass (GondorianCraftingTable.class);
        MineTweakerAPI.registerClass (GulfCraftingTable.class);
        MineTweakerAPI.registerClass (GundabadCraftingTable.class);
        MineTweakerAPI.registerClass (HalfTrollCraftingTable.class);
        MineTweakerAPI.registerClass (HighElvenCraftingTable.class);
        MineTweakerAPI.registerClass (HobbitCraftingTable.class);
        MineTweakerAPI.registerClass (IsengardCraftingTable.class);
        MineTweakerAPI.registerClass (MordorCraftingTable.class);
        MineTweakerAPI.registerClass (MoredainCraftingTable.class);
        MineTweakerAPI.registerClass (NearHaradCraftingTable.class);
        MineTweakerAPI.registerClass (RangerNorthCraftingTable.class);
        MineTweakerAPI.registerClass (RhunCraftingTable.class);
        MineTweakerAPI.registerClass (RivendellCraftingTable.class);
        MineTweakerAPI.registerClass (RohanCraftingTable.class);
        MineTweakerAPI.registerClass (TauredainCraftingTable.class);
        MineTweakerAPI.registerClass (UmbarCraftingTable.class);
        MineTweakerAPI.registerClass (WoodElvenCraftingTable.class);

        MineTweakerAPI.registerClass (Millstone.class);

        event.getModLog ().info (
            "MT LOTR IS PRESENT - RECIPES OF THE LOTR MOD ARE EVENTUALLY MODIFIED!");
        event.getModLog ().info (
            "BEFORE REPORTING BUGS TO THE LOTR MOD, MAKE SURE THOSE ARE REPRODUCIBLE WITH AN UNMODIFIED VERSION OF THE LOTR MOD!");
    }

}
