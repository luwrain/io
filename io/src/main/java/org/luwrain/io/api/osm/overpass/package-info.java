// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

/**
 * Overpass API client for OpenStreetMap.
 *
 * <p>This package provides a client for the
 * <a href="https://wiki.openstreetmap.org/wiki/Overpass_API/Overpass_QL">Overpass API</a>,
 * which allows running arbitrary Overpass QL queries against OSM data.</p>
 *
 * <p>The main entry point is {@link org.luwrain.io.api.osm.overpass.OverpassClient},
 * which offers the following capabilities:</p>
 * <ul>
 *   <li><b>Adjacent streets</b> — find streets that intersect a given street</li>
 *   <li><b>Street geometry</b> — get coordinates of a street</li>
 *   <li><b>Address search</b> — find nodes by city, street, and house number</li>
 *   <li><b>Find by name</b> — search OSM elements by name</li>
 *   <li><b>Coordinate lookup</b> — find the nearest OSM element to a point</li>
 *   <li><b>Public transport</b> — stops on streets and routes, route listing</li>
 *   <li><b>Institutions</b> — find amenities and other POIs near a street</li>
 * </ul>
 *
 * <p>All queries use OkHttp for HTTP transport. JSON responses are deserialized
 * with Gson, with polymorphic element deserialization via
 * {@link org.luwrain.io.api.osm.overpass.OverpassElementDeserializer}.</p>
 *
 * @see org.luwrain.io.api.osm.nominatim
 */
package org.luwrain.io.api.osm.overpass;
