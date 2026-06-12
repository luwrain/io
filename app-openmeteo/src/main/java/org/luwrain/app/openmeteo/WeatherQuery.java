// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.openmeteo;

import java.util.*;
import java.net.*;
import java.net.http.*;
import java.io.*;

import org.apache.logging.log4j.*;

import org.luwrain.core.*;

final class WeatherQuery
{
    static private final Logger log = LogManager.getLogger();

    static private final String GEOCODING_URL = "https://geocoding-api.open-meteo.com/v1/search";
    static private final String FORECAST_URL = "https://api.open-meteo.com/v1/forecast";

    private final Luwrain luwrain;
    private final Strings strings;

    WeatherQuery(App app)
    {
	this.luwrain = app.getLuwrain();
	this.strings = app.getStrings();
    }

    /**
     * Ищет координаты по названию города через Open-Meteo Geocoding API.
     * Возвращает массив из двух строк: {latitude, longitude}, либо null при ошибке.
     */
    String[] geocode(String cityName)
    {
	try {
	    final var url = new URI(GEOCODING_URL + "?name=" + URLEncoder.encode(cityName, "UTF-8") + "&count=1&language=ru&format=json");
	    final var client = HttpClient.newHttpClient();
	    final var request = HttpRequest.newBuilder(url).GET().build();
	    final var response = client.send(request, HttpResponse.BodyHandlers.ofString());
	    if (response.statusCode() != 200)
	    {
		log.error("Geocoding API returned status " + response.statusCode());
		return null;
	    }
	    // TODO: полноценный парсинг JSON-ответа (сейчас упрощённая заглушка)
	    final var body = response.body();
	    final var lat = extractJsonString(body, "\"latitude\"");
	    final var lon = extractJsonString(body, "\"longitude\"");
	    if (lat == null || lon == null)
		return null;
	    return new String[]{lat, lon};
	}
	catch(Exception ex)
	{
	    log.error("Geocoding query failed: " + ex.getMessage());
	    return null;
	}
    }

    /**
     * Запрашивает погоду по координатам через Open-Meteo Forecast API.
     * Возвращает текстовое описание погоды либо null при ошибке.
     */
    String getWeather(String latitude, String longitude)
    {
	try {
	    final var url = new URI(FORECAST_URL
		+ "?latitude=" + latitude
		+ "&longitude=" + longitude
		+ "&current_weather=true"
		+ "&timezone=auto"
		+ "&daily=sunrise,sunset");
	    final var client = HttpClient.newHttpClient();
	    final var request = HttpRequest.newBuilder(url).GET().build();
	    final var response = client.send(request, HttpResponse.BodyHandlers.ofString());
	    if (response.statusCode() != 200)
	    {
		log.error("Forecast API returned status " + response.statusCode());
		return null;
	    }
	    // TODO: полноценный парсинг JSON-ответа (сейчас упрощённая заглушка)
	    final var body = response.body();
	    final var temp = extractJsonString(body, "\"temperature\"");
	    final var windSpeed = extractJsonString(body, "\"windspeed\"");
	    final var weatherCode = extractJsonString(body, "\"weathercode\"");

	    if (temp == null)
		return null;

	    final var sb = new StringBuilder();
	    sb.append(strings.temperature()).append(": ").append(temp).append("°C\n");
	    if (windSpeed != null)
		sb.append(strings.windSpeed()).append(": ").append(windSpeed).append(" ").append(strings.windSpeedUnit()).append("\n");
	    if (weatherCode != null)
		sb.append(strings.weatherCode()).append(": ").append(weatherCodeToText(weatherCode));
	    return sb.toString().trim();
	}
	catch(Exception ex)
	{
	    log.error("Forecast query failed: " + ex.getMessage());
	    return null;
	}
    }

    /**
     * Упрощённое извлечение значения по ключу из JSON-строки.
     * TODO: заменить на полноценный JSON-парсер (Jackson/Gson).
     */
    static private String extractJsonString(String json, String key)
    {
	final var keyPattern = key + "\":";
	final var idx = json.indexOf(keyPattern);
	if (idx < 0)
	    return null;
	var start = idx + keyPattern.length();
	// пропускаем пробелы
	while (start < json.length() && (json.charAt(start) == ' ' || json.charAt(start) == '\t'))
	    start++;
	if (start >= json.length())
	    return null;
	final var ch = json.charAt(start);
	if (ch == '"')
	{
	    // строковое значение
	    start++;
	    final var end = json.indexOf('"', start);
	    if (end < 0)
		return null;
	    return json.substring(start, end);
	}
	// числовое значение
	var end = start;
	while (end < json.length() && (Character.isDigit(json.charAt(end)) || json.charAt(end) == '.' || json.charAt(end) == '-'))
	    end++;
	if (end == start)
	    return null;
	return json.substring(start, end);
    }

    static private String weatherCodeToText(String code)
    {
	if (code == null)
	    return "—";
	try {
	    final var c = Integer.parseInt(code);
	    // Интерпретация кодов WMO
	    // https://open-meteo.com/en/docs
	    return switch(c)
	    {
		case 0 -> "Ясно";
		case 1, 2, 3 -> "Переменная облачность";
		case 45, 48 -> "Туман";
		case 51, 53, 55 -> "Морось";
		case 61, 63, 65 -> "Дождь";
		case 71, 73, 75 -> "Снег";
		case 80, 81, 82 -> "Ливень";
		case 95, 96, 99 -> "Гроза";
		default -> "Код " + code;
	    };
	}
	catch(NumberFormatException ex)
	{
	    return "Код " + code;
	}
    }
}
