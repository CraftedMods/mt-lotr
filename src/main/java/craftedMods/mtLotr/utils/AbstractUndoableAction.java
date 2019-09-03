package craftedMods.mtLotr.utils;

import craftedMods.mtLotr.MTLotr;
import minetweaker.IUndoableAction;

public abstract class AbstractUndoableAction implements IUndoableAction
{

    protected boolean canUndo;

    protected final String deviceName;
    protected final String deviceEntryName;

    protected AbstractUndoableAction (String deviceName, String deviceEntryName)
    {
        this.deviceName = deviceName;
        this.deviceEntryName = deviceEntryName;
    }

    @Override
    public boolean canUndo ()
    {
        return canUndo;
    }

    @Override
    public String describe ()
    {
        return String.format ("%s Applied %s changes for the %s", MTLotr.PREFIX, deviceEntryName, deviceName);
    }

    @Override
    public String describeUndo ()
    {
        return String.format ("%s Reverted %s changes for the %s", MTLotr.PREFIX, deviceEntryName, deviceName);
    }

    @Override
    public Object getOverrideKey ()
    {
        return null;
    }

}
