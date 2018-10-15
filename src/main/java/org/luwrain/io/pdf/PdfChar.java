
package org.luwrain.io.pdf;

public final class PdfChar
{
    public final char ch;
    public final double x;
    public final double y;
    public final boolean bold;

    PdfChar(char ch, double x, double y, boolean bold)
    {
	 this.ch = ch;
	 this.x = x;
	 this.y = y;
	 this.bold = bold;
	 }
}
