
package org.luwrain.app.mastodon;

import java.io.*;

import com.github.scroogemcfawk.mastodon.api.*;
import org.luwrain.core.*;

final class Data
{
    	static final String PATH = "/org/luwrain/app/mastodon";

    final Settings sett;
    final ApplicationClient client;

    Data(Luwrain luwrain) throws IOException
    {
	sett = RegistryProxy.create(luwrain.getRegistry(), PATH, Settings.class);
	client = new ApplicationClient(new Configuration());
    }

    interface Settings
    {
	String getToken(String defaultValue);
	String setToken(String value);

}
}

