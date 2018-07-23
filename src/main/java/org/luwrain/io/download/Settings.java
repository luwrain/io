
package org.luwrain.io.download;

import org.luwrain.core.*;

public interface Settings
{
    public static final String COMPLETED = "completed";
    public static final String FAILED = "failed";

    static public final String DOWNLOAD_PATH = "/org/luwrain/io/download";

    public interface Entry
    {
	String getUrl(String defValue);
	void setUrl(String value);
	String getDestFile(String defValue);
	void setDestFile(String value);
	String getStatus(String defValue);
	void setStatus(String value);
	String getErrorInfo(String defValue);
	void setErrorInfo(String value);
    }

    static public Entry createEntry(Registry registry, int id)
    {
	NullCheck.notNull(registry, "registry");
	if (id < 0)
	    throw new IllegalArgumentException("id may not be negative (" + id + ")");
	final String path = Registry.join(DOWNLOAD_PATH, "" + id);
	registry.addDirectory(path);
	return RegistryProxy.create(registry, path, Entry.class);
    }

    static public int nextId(Registry registry)
    {
	NullCheck.notNull(registry, "registry");
	registry.addDirectory(DOWNLOAD_PATH);
	return Registry.nextFreeNum(registry, DOWNLOAD_PATH);
    }
}
