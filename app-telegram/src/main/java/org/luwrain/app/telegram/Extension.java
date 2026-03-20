
package org.luwrain.app.telegram;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.i18n.*;

public final class Extension extends EmptyExtension
{
    static private final String
	LOG_COMPONENT = Core.LOG_COMPONENT;

    private Core core = null;

    @Override public String init(Luwrain luwrain)
    {
	final JniLoader jniLoader = new JniLoader();
	if (!jniLoader.loadByShortName(luwrain.getClass().getClassLoader(), "tdjni"))
	{
	    Log.warning(LOG_COMPONENT, "unable to load tdjni");
	    return "Unable to load tdjni";
	}
	this.core = new Core(luwrain, ()->{});
	return null;
    }

    @Override public Command[] getCommands(Luwrain luwrain)
    {
	return new Command[]{new SimpleShortcutCommand("telegram")};
    }

    @Override public ExtensionObject[] getExtObjects(Luwrain luwrain)
    {
	return new Shortcut[]{
	    new Shortcut(){
		@Override public String getExtObjName() { return "telegram"; }
		@Override public Set<Flags> getShortcutFlags() { return EnumSet.noneOf(Flags.class); }
		@Override public String[] getFileExtensions() { return new String[0]; }
		@Override public Application[] prepareApp(String[] args)
		{
		    NullCheck.notNull(args, "args");
		    if (core == null)
		    {
			luwrain.message("No core", Luwrain.MessageType.ERROR);
		    }
		    return new Application[]{new App(core)};
		}
	    }
	};
    }

    @Override public void i18nExtension(Luwrain luwrain, org.luwrain.i18n.I18nExtension i18nExt)
    {
	i18nExt.addCommandTitle(Lang.EN, "telegram", "Telegram");
	i18nExt.addCommandTitle(Lang.RU, "telegram", "Телеграм");
    }
}
