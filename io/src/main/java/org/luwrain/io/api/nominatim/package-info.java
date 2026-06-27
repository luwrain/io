/**
 * Nominatim API client for OpenStreetMap geocoding.
 *
 * <p>This package provides a client for the
 * <a href="https://nominatim.org/release-docs/latest/api/Overview/">Nominatim API</a>,
 * which offers the following capabilities:</p>
 * <ul>
 *   <li><b>Forward geocoding</b> ({@code /search}) — convert an address to coordinates</li>
 *   <li><b>Reverse geocoding</b> ({@code /reverse}) — convert coordinates to an address</li>
 *   <li><b>Lookup</b> ({@code /lookup}) — retrieve OSM objects by their IDs</li>
 *   <li><b>Details</b> ({@code /details}) — get detailed information about an OSM object</li>
 *   <li><b>Status</b> ({@code /status}) — check the service availability</li>
 * </ul>
 *
 * <p>The main entry point is {@link org.luwrain.io.api.nominatim.NominatimClient},
 * which uses OkHttp for HTTP transport and Gson for JSON deserialization.</p>
 */
package org.luwrain.io.api.nominatim;
