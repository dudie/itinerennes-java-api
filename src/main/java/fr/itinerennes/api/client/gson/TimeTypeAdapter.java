package fr.itinerennes.api.client.gson;

/*
 * [license]
 * ItineRennes Java API client
 * ----
 * Copyright (C) 2010 - 2013 Dudie
 * ----
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * [/license]
 */

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import fr.itinerennes.api.client.model.Time;

/**
 * @author Jeremie Huchet
 */
public class TimeTypeAdapter implements JsonSerializer<Time>, JsonDeserializer<Time> {

    private static final String TIME_FORMAT = "HH:mm";

    private final DateFormat dateFormatter;

    public TimeTypeAdapter(final String timezone, final Locale locale) {

        dateFormatter = new SimpleDateFormat(TIME_FORMAT, locale);
        dateFormatter.setTimeZone(TimeZone.getTimeZone(timezone));
    }

    @Override
    public Time deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
        final String value = json.getAsString();
        try {
            return new Time(dateFormatter.parse(value).getTime());
        } catch (final ParseException e) {
            final String msg = String.format("Can't parse time value %s", value);
            throw new JsonParseException(msg, e);
        }
    }

    @Override
    public JsonElement serialize(final Time src, final Type typeOfSrc, final JsonSerializationContext context) {

        return new JsonPrimitive(dateFormatter.format(src));
    }

}
