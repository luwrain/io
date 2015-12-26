/*
   Copyright 2012-2015 Michael Pozhidaev <michael.pozhidaev@gmail.com>

   This file is part of the LUWRAIN.

   LUWRAIN is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   LUWRAIN is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.network;

import org.luwrain.core.NullCheck;

public class WifiNetwork
{
String name;
boolean hasPassword;
    String password = "";

    WifiNetwork(String name, boolean hasPassword)
    {
	this.name = name;
	this.hasPassword = hasPassword;
	NullCheck.notNull(name, "name");
    }

    public String name()
    {
	return name;
    }

    public boolean hasPassword()
    {
	return hasPassword;
    }

    public void setPassword(String password)
    {
	NullCheck.notNull(password, "password");
	this.password = password;
    }

    public String password()
    {
	return password;
    }

    @Override public String toString()
    {
	return name;
    }
}
