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
package craftedMods.mtLotr;

import java.net.MalformedURLException;

import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.*;
import cpw.mods.fml.common.Mod.*;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.common.gameevent.TickEvent;
import craftedMods.mtLotr.devices.Millstone;
import craftedMods.mtLotr.devices.ct.*;
import craftedMods.mtLotr.utils.version.*;
import craftedMods.mtLotr.utils.version.VersionChecker.EnumVersionComparison;
import minetweaker.MineTweakerAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.event.*;
import net.minecraft.event.ClickEvent.Action;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;

@Mod(modid = "mt-lotr", name = "MineTweaker Integration for the LOTR Mod", version = MTLotr.VERSION)
public class MTLotr
{

    public static final String PREFIX = "[MT LOTR]:";
    public static final String VERSION = "1.0.0-BETA";

    @Instance(value = "mt-lotr")
    public static MTLotr instance;

    private Logger logger;

    private boolean worldLoaded;

    private VersionChecker versionChecker;

    @EventHandler
    public void onPreInit (FMLPreInitializationEvent event) throws MalformedURLException
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

        logger = event.getModLog ();

        logger.info (
            "MT LOTR IS PRESENT - RECIPES OF THE LOTR MOD ARE EVENTUALLY MODIFIED!");
        logger.info (
            "BEFORE REPORTING BUGS TO THE LOTR MOD, MAKE SURE THOSE ARE REPRODUCIBLE WITH AN UNMODIFIED VERSION OF THE LOTR MOD!");

        versionChecker = new VersionChecker ("https://raw.githubusercontent.com/CraftedMods/mt-lotr/master/version.txt",
            SemanticVersion.of (VERSION));

        try
        {
            logger.debug (String.format ("Starting version check..."));
            versionChecker.checkVersion ();
            if (versionChecker.getRemoteVersion () != null)
            {
                logger.info (String.format ("Found a remote version: %s (%s version)",
                    versionChecker.getRemoteVersion ().getRemoteVersion ().toString (),
                    versionChecker.compareRemoteVersion ().toDisplayString ()));
            }
        }
        catch (Exception e)
        {
            logger.error (String.format ("Version check failed"), e);
        }

        FMLCommonHandler.instance ().bus ().register (this);
        MinecraftForge.EVENT_BUS.register (this);
    }

    public Logger getLogger ()
    {
        return logger;
    }

    @SubscribeEvent
    public void onWorldLoad (WorldEvent.Load event)
    {
        this.worldLoaded = true;
    }

    @SubscribeEvent
    public void onTick (TickEvent.ClientTickEvent event)
    {
        if (this.worldLoaded && Minecraft.getMinecraft ().theWorld != null
            && Minecraft.getMinecraft ().thePlayer != null)
        {
            if (versionChecker.compareRemoteVersion () == EnumVersionComparison.NEWER)
            {
                Minecraft.getMinecraft ().thePlayer.addChatComponentMessage (
                    getVersionNotificationChatText (versionChecker.getRemoteVersion (), false));
            }
            this.worldLoaded = false;
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedIn (PlayerLoggedInEvent event)
    {
        if (FMLCommonHandler.instance ().getSide () == Side.SERVER
            && MinecraftServer.getServer ().getConfigurationManager ().func_152596_g (event.player.getGameProfile ()))
        {
            // Player is OP
            if (versionChecker.compareRemoteVersion () == EnumVersionComparison.NEWER)
            {
                event.player.addChatComponentMessage (
                    getVersionNotificationChatText (versionChecker.getRemoteVersion (), true));
            }
        }
    }

    private IChatComponent getVersionNotificationChatText (RemoteVersion version, boolean server)
    {
        IChatComponent part1 = new ChatComponentText ("[MT LOTR]: ")
            .setChatStyle (new ChatStyle ().setColor (EnumChatFormatting.GREEN));
        IChatComponent part2 = new ChatComponentText (String.format ("New %sversion: ", server ? "server " : ""))
            .setChatStyle (new ChatStyle ().setColor (EnumChatFormatting.WHITE));
        IChatComponent part3 = new ChatComponentText (version.getRemoteVersion ().toString ())
            .setChatStyle (
                version.getDownloadURL () != null
                    ? new ChatStyle ().setColor (EnumChatFormatting.YELLOW).setUnderlined (true)
                        .setChatClickEvent (new ClickEvent (Action.OPEN_URL, version.getDownloadURL ().toString ()))
                        .setChatHoverEvent (new HoverEvent (net.minecraft.event.HoverEvent.Action.SHOW_TEXT,
                            new ChatComponentText ("Click to open the download page of the new version")))
                    : new ChatStyle ().setColor (EnumChatFormatting.YELLOW));
        part1.appendSibling (part2).appendSibling (part3);
        if (version.getChangelogURL () != null)
        {
            IChatComponent part4 = new ChatComponentText (
                " (")
                    .setChatStyle (new ChatStyle ().setColor (EnumChatFormatting.WHITE));
            IChatComponent part5 = new ChatComponentText (
                "Changelog")
                    .setChatStyle (new ChatStyle ().setColor (EnumChatFormatting.BLUE).setUnderlined (true)
                        .setChatClickEvent (new ClickEvent (Action.OPEN_URL, version.getChangelogURL ().toString ()))
                        .setChatHoverEvent (new HoverEvent (net.minecraft.event.HoverEvent.Action.SHOW_TEXT,
                            new ChatComponentText ("Click to show the changelog in the browser"))));
            IChatComponent part6 = new ChatComponentText (")")
                .setChatStyle (new ChatStyle ().setColor (EnumChatFormatting.WHITE));
            part1.appendSibling (part4).appendSibling (part5).appendSibling (part6);
        }

        return part1;
    }

}
