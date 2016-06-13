
package org.luwrain.app.wifi;

import org.luwrain.core.*;
import org.luwrain.network.*;
import org.luwrain.util.*;

interface Settings
{
    static public final String NETWORKS_PATH = "/org/luwrain/network/wifi-networks";

    public interface Network
    {
	String getPassword(String defValue);
	void setPassword(String value);
    }

    static public Network createNetwork(Registry registry, WifiNetwork network)
    {
	NullCheck.notNull(registry, "registry");
	NullCheck.notNull(network, "network");
	return RegistryProxy.create(registry, RegistryPath.join(NETWORKS_PATH, makeRegistryName(network.name())), Network.class);
    }

    static String makeRegistryName(String value)
    {
	return value.replaceAll("/", "_").replaceAll("\n", "_").replaceAll(" ", "_");
    }
}
