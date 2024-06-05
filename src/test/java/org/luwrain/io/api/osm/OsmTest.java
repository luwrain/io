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

package org.luwrain.io.api.osm;

import java.util.*;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class OsmTest
{
    OsmApiService  s;

    @Test public void byAddr() throws Exception
    {
	final var r = s.getNodesByAddress("Томск", "проспект Ленина", "36");
	assertNotNull(r);
	assertEquals(2, r.size());
	final var rr = r.get(1);
	assertNotNull(rr);
	final var  tags = rr.getTags();
	assertNotNull(tags);
	assertEquals(6, tags.size());
	assertNotNull(tags.get("name"));
	assertEquals("Университетская роща ТГУ", tags.get("name"));
    }

    @Test public void byName() throws Exception
    {
	final var r = s.getOSMEntityByName("node", "ТГУ");
	assertNotNull(r);
	assertEquals(29, r.size());
	final var rr = r.get(0);
	assertNotNull(rr);
	final var tags = rr.getTags();
	assertNotNull(tags);
	assertNotNull(tags.get("name"));
	assertEquals("Научная библиотека ТГУ", tags.get("name"));
    }

    @BeforeEach void init()
    {
	s = new OsmApiService();
    }
}
