package craftedMods.mtLotr.utils;

import craftedMods.mtLotr.MTLotr;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;

public class MTLotrUtils
{

    public static ItemStack toStack (IItemStack iStack)
    {
        if (iStack == null)
        {
            return null;
        }
        else
        {
            Object internal = iStack.getInternal ();
            if (! (internal instanceof ItemStack))
            {
                System.out.println ("ERROR");
                // LogHelper.logError("Not a valid item stack: " + iStack);
            }

            return (ItemStack) internal;
        }
    }
    
    public static void logInfo (String message, Object... params)
    {
        MineTweakerAPI.logInfo (MTLotr.PREFIX + " " + String.format (message, params));
    }

    public static void logWarning (String message, Object... params)
    {
        MineTweakerAPI.logWarning (MTLotr.PREFIX + " " + String.format (message, params));
    }

    public static void logError (String message, Object... params)
    {
        MineTweakerAPI.logError (MTLotr.PREFIX + " " + String.format (message, params));
    }

    public static void logErrorThrowable (String message, Throwable throwable, Object... params)
    {
        MineTweakerAPI.logError (MTLotr.PREFIX + " " + String.format (message, params), throwable);
    }

}
