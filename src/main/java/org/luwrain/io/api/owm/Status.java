/*
   Copyright 2012-2024 Michael Pozhidaev <msp@luwrain.org>

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

package org.luwrain.io.api.owm;

import java.util.*;
import com.google.gson.annotations.*;

public class Status
{
    protected String base, name;
    protected int timezone, id, cod, dt;
    protected double visibility;
    protected Coord coord;
    protected List<Weather> weather;
    protected Params main;
    protected Wind wind;
    protected Sys sys;

    static public class Coord
    {
	protected double lon, lat;
	 }

    static public class Weather
    {
	protected String id, main, description, icon;
    }

    static final class Params
    {
	protected double temp, feels_like, temp_min, temp_max, pressure, humidity;
    }

    static public class Wind
    {
	double speed, deg, gust;
    }

    static public class Rain
    {
	@SerializedName("1h")
	protected double value;
    }

    static public class Clouds
    {
	protected int all;
    }

    static public class Sys
    {
	protected int type, id, sunrise, sunset;
	protected String country;
    }

    
}
