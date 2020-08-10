/*
   Copyright 2012-2020 Michael Pozhidaev <msp@luwrain.org>

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

package org.luwrain.nlp.ru;

import org.luwrain.core.*;
import org.luwrain.i18n.*;

public final class GramAttr implements GrammaticalAttr
{
    public enum Gender {
	MASCULINE,
	FEMININE,
NEUTER
    };

    public enum Number {
	SINGULAR,
	PLURAL
    };

    public enum Case {
	NOM,
	GEN,
	DAT,
	ACC,
	INST,
	PRAE
    };

    private final Gender gender;
    private final Number number;
    private final Case gcase;

    public GramAttr(Gender gender, Number number, Case gcase)
    {
	NullCheck.notNull(gender, "gender");
	NullCheck.notNull(number, "number");
	NullCheck.notNull(gcase, "gcase");
	this.gender = gender;
	this.number = number;
	this.gcase = gcase;
    }

    public Gender getGender()
    {
	return gender;
    }

    public Number getNumber()
    {
	return number;
    }

    public Case getCase()
    {
	return gcase;
    }
}
