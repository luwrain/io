/*
   Copyright 2012-2022 Michael Pozhidaev <msp@luwrain.org>

   This file is part of LUWRAIN.

   LUWRAIN is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   LUWRAIN is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.controls;

import java.util.*;

import org.luwrain.core.*;
import static org.luwrain.controls.EditUtils.*;

public class EditSpellChecking implements EditArea.ChangeListener
{
    @Override public void onEditChange(EditArea editArea, MarkedLines lines, HotPoint hotPoint)
    {
	final SortedMap<Integer, String> text = new TreeMap<>();
	blockBounds(editArea, hotPoint.getHotPointY(),(line, marks)->(!line.trim().isEmpty()),
		    (lines_, index)->text.put(index, lines.getLine(index)));
    }

    
}
