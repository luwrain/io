
package org.luwrain.app.telegram;

import com.google.auto.service.*;
import org.luwrain.core.*;

@AutoService(org.luwrain.core.Extension.class)
public final class Extension extends EmptyExtension
{
    static Luwrain luwrain = null;

    @Override public String init(Luwrain luwrain)
    {
	if (Extension.luwrain == null)
	    Extension.luwrain = luwrain;
	return null;
    }
}
