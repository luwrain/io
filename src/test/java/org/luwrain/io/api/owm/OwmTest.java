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
import java.io.*;

import com.google.gson.*;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class OwmTest
{
    static private final Gson gson = new Gson();

    @Test void parse()
    {
	final var s = loadExample();
	assertNotNull(s);
    }

    private Status loadExample()
    {
	try {
	try (final var r = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("example.json"), "UTF-8"))){
	    return gson.fromJson(r, Status.class);
	}
	}
	catch(IOException e)
	{
	    throw new RuntimeException(e);
	}
    }

    

}
