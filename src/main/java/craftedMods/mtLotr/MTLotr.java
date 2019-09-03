package craftedMods.mtLotr;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import craftedMods.mtLotr.devices.Millstone;
import minetweaker.MineTweakerAPI;

@Mod(modid = "mt-lotr", name = "MineTweaker Integration for the LOTR Mod", version = "1.0.0-BETA")
public class MTLotr
{

    public static final String PREFIX = "[MT LOTR]:";

    @EventHandler
    public void onPreInit (FMLPreInitializationEvent event)
    {
        MineTweakerAPI.registerClass (Millstone.class);

        event.getModLog ().info (
            "MT LOTR IS PRESENT - RECIPES OF THE LOTR MOD ARE EVENTUALLY MODIFIED!");
        event.getModLog ().info (
            "BEFORE REPORTING BUGS TO THE LOTR MOD, MAKE SURE THOSE ARE REPRODUCIBLE WITH AN UNMODIFIED VERSION OF THE LOTR MOD!");
    }

}
