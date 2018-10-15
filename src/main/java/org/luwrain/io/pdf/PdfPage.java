
package org.luwrain.io.pdf;

import org.luwrain.core.*;

public final class PdfPage
{
    final int num;
    final PdfChar[] chars;

    public PdfPage(int num, PdfChar[] chars)
    {
	if (num < 1)
	    throw new IllegalArgumentException("num (" + num + " may not be less than 1");
	NullCheck.notNullItems(chars, "chars");
	this.num = num;
	this.chars = chars;
    }
}
