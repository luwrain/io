
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
	//Main
	assertNotNull(s.main);
	assertEquals(284.75, s.main.temp);
		assertEquals(284.24, s.main.feels_like);
		//Weather
		assertNotNull(s.weather);
		final var w = s.weather.get(0);
		assertNotNull(w);
		assertEquals("Rain", w.main);
				assertEquals("light rain", w.description);
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
