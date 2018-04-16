
package org.luwrain.api.wikipedia;

public class WikipediaException extends Exception
{
    public WikipediaException(Exception e)
    {
	super(e.getClass().getName() + ":" + e.getMessage(), e);
    }
}
