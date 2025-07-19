
package org.luwrain.app.mastodon;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.i18n.*;

public final class Extension extends EmptyExtension
{
    @Override public Command[] getCommands(Luwrain luwrain)
    {
	return new Command[]{ new SimpleShortcutCommand("mastodon") };
    }

    @Override public ExtensionObject[] getExtObjects(Luwrain luwrain)
    {
	return new ExtensionObject[]{ new SimpleShortcut("mastodon", App.class) };
    }

    @Override public void i18nExtension(Luwrain luwrain, org.luwrain.i18n.I18nExtension i18nExt)
    {
	i18nExt.addCommandTitle(Lang.EN, "mastodon", "Mastodon");
	i18nExt.addCommandTitle(Lang.RU, "mastodon", "Мастодон");
	try {
	    i18nExt.addStrings(Lang.EN, Strings.NAME, new ResourceStringsObj(luwrain, getClass().getClassLoader(), getClass(), "strings.properties").create(Lang.EN, Strings.class));
	    i18nExt.addStrings(Lang.RU, Strings.NAME, new ResourceStringsObj(luwrain, getClass().getClassLoader(), getClass(), "strings.properties").create(Lang.RU, Strings.class));
	}
	catch(java.io.IOException e)
	{
	    throw new RuntimeException(e);
	}
    }
}
