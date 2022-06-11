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

package org.luwrain.nlp.ru;

import java.util.*;

import org.luwrain.core.*;
import org.luwrain.i18n.*;

public final class GramAttr implements org.luwrain.nlp.GrammaticalAttr
{
    public enum Gender {
	MASCULINE,
	FEMININE,
	NEUTER;

	static public Gender find(String s)
	{
	    NullCheck.notNull(s, "s");
	    switch(s.toUpperCase())
	    {
	    case "M":
	    case "MASC":
	    case "MASCULINE":
		return Gender.MASCULINE;
	    case "F":
	    case "FEM":
	    case "FEMININE":
		return Gender.FEMININE;
	    case "N":
	    case "NEU":
	    case "NEUTER":
		return Gender.NEUTER;
	    default:
		return null;
	    }
	}
    };

    public enum Number {
	SINGULAR,
	PLURAL;

	static public Number find(String s)
	{
	    NullCheck.notNull(s, "s");
	    switch(s.toUpperCase())
	    {
	    case "S":
	    case "SG":
	    case "SINGULAR":
		return Number.SINGULAR;
	    case "P":
	    case "PL":
	    case "PLURAL":
		return Number.PLURAL;
	    default:
		return null;
	    }
	}
    };

    public enum Case {
	NOM,
	GEN,
	DAT,
	ACC,
	INST,
	PRAE;

	static public Case find(String s)
	{
	    NullCheck.notNull(s, "s");
	    switch(s.toUpperCase())
	    {
	    case "N":
	    case "NOM":
		return Case.NOM;
	    case "G":
	    case "GEN":
		return Case.GEN;
	    case "D":
	    case "DAT":
		return Case.DAT;
	    case "A":
	    case "ACC":
		return Case.ACC;
	    case "I":
	    case "INS":
	    case "INST":
		return Case.INST;
	    case "P":
	    case "PRE":
	    case "PRA":
	    case "PRAE":
		return Case.PRAE;
	    default:
		return null;
	    }
	}
    };

    private final Gender gender;
    private final Number number;
    private final Case gcase;

    public GramAttr(Gender gender, Number number, Case gcase)
    {
	this.gender = gender;
	this.number = number;
	this.gcase = gcase;
    }

    public GramAttr(GramAttr a1, GramAttr a2)
    {
	this.gender = a2.getGender() != null?a2.getGender():a1.getGender();
	this.number = a2.getNumber() != null?a2.getNumber():a1.getNumber();
	this.gcase = a2.getCase() != null?a2.getCase():a1.getCase();
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

    static public GramAttr fromString(String s)
    {
	NullCheck.notNull(s, "s");
	final String[] items = s.replaceAll(";", ",").split(",", -1);;
	Gender g = null;
	Case c = null;
	Number n = null;
	for(String i: items)
	{
	    final String k = i.trim();
	    if (k.length() < 2)
		continue;
	    final Gender gg = Gender.find(k);
	    final Case cc = Case.find(k);
	    final Number nn = Number.find(k);
	    if (cc != null)
		c = cc; else
		if (nn != null)
		    n = nn; else
		    if (gg != null)
			g = gg;
	}
	return new GramAttr(g, n, c);
    }
}
