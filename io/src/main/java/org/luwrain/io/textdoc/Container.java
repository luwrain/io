
package org.luwrain.io.textdoc;

import java.util.*;

public class Container 
{
    private List<ContainerItem> items = null;

    public List<ContainerItem> getItems()
    {
	if (this.items == null)
	    this.items = new ArrayList<>();
	return this.items;
    }

    public void setAttributes(Attributes attr)
    {
    }
}
