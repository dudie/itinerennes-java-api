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
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * @author Jeremie Huchet
 */
public class DateTypeAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {

    private final DateFormat dateFormatter;

    public DateTypeAdapter(final String dateFormat, final String timezone, final Locale locale) {

        dateFormatter = new SimpleDateFormat(dateFormat, locale);
        dateFormatter.setTimeZone(TimeZone.getTimeZone(timezone));
    }

    @Override
    public Date deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
        final String value = json.getAsString();
        try {
            return dateFormatter.parse(value);
        } catch (final ParseException e) {
            final String msg = String.format("Can't parse date value %s", value);
            throw new JsonParseException(msg, e);
        }
    }

    @Override
    public JsonElement serialize(final Date src, final Type typeOfSrc, final JsonSerializationContext context) {

        return new JsonPrimitive(dateFormatter.format(src));
    }

}
