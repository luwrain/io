/*
   Copyright 2012-2016 Michael Pozhidaev <michael.pozhidaev@gmail.com>

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

public class WifiScanResult
{
    public enum Type {SUCCESS, FAILED};

    private final Type type;
    private final WifiNetwork[] networks;

    WifiScanResult()
    {
	this.type = Type.FAILED;
	this.networks = new WifiNetwork[0];
    }

    WifiScanResult(WifiNetwork[] networks)
    {
	NullCheck.notNullItems(networks, "networks");
	this.type = Type.SUCCESS;
	this.networks = networks;
    }

    public Type getType()
    {
	return type;
    }

    public WifiNetwork[] getNetworks()
    {
	return networks;
    }
}
