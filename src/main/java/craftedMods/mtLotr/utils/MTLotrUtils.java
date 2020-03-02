package craftedMods.mtLotr.utils;

import craftedMods.mtLotr.MTLotr;
import minetweaker.MineTweakerAPI;

public class MTLotrUtils
{

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
