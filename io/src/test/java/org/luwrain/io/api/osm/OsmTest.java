
package org.luwrain.io.api.osm;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import static org.luwrain.io.api.Base.*;

public class OsmTest
{
    OsmApiService  s;

    @Disabled @Test public void byAddr() throws Exception
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
	if (!allowApiTests())
	    return;
	final var r = s.findByName("node", "ТГУ");
	assertNotNull(r);
	assertEquals(32, r.size());
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
