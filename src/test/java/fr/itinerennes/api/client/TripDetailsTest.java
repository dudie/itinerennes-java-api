package fr.itinerennes.api.client;

/*
 * [license]
 * ItineRennes Java API client
 * ----
 * Copyright (C) 2010 - 2014 Dudie
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import fr.itinerennes.api.client.model.Route;
import fr.itinerennes.api.client.model.TripSchedule;
import fr.itinerennes.api.client.model.TripStopTime;
import fr.itinerennes.junit.rules.RunWithWebServer;

/**
 * Check trip details deserialization.
 * 
 * @author Jeremie Huchet
 */
public class TripDetailsTest {

    // private static final SimpleDateFormat FORMATER = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    /** Run a web server. */
    @Rule
    public static final RunWithWebServer SERVER = new RunWithWebServer("/www");

    /** Trip Details deserialization result. */
    private TripSchedule trip;

    /**
     * Setup the ItineRennes client.
     */
    @Before
    public void setupItineRennesClient() throws ParseException, IOException {

        final SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", new PlainSocketFactory(), 80));
        final ClientConnectionManager connexionManager = new SingleClientConnManager(null, registry);

        final HttpClient httpClient = new DefaultHttpClient(connexionManager, null);
        final JsonItineRennesApiClient obaClient = new JsonItineRennesApiClient(httpClient, SERVER.getUrl().toString());

        trip = obaClient.getTripDetails("2_12235");
    }

    @Test
    public void verifyTripAttributes() {
        assertEquals("5 | Lycée Bréquigny", trip.getHeadsign());
    }

    @Test
    public void verifyRouteAttributes() {
        final Route r = trip.getRoute();

        assertNotNull("Missing route details", r);
        assertEquals("2", r.getAgencyId());
        assertEquals("2_0005", r.getId());
        assertEquals("eb8200", r.getColor());
        assertEquals("ffffff", r.getTextColor());
        assertEquals("Majeure", r.getDescription());
        assertEquals("Patton <> Lycée Bréquigny", r.getLongName());
        assertEquals("5", r.getShortName());
        assertEquals(3, r.getType());
    }

    @Test
    public void verifyStopTimes() {
        assertEquals("29 stop times should be returned by the api", 29, trip.getStopTimes().size());
    }

    @Test
    public void verifyFirstStopTime() {
        // final String expected = "2013-11-05 00:36";

        final TripStopTime first = trip.getStopTimes().get(0);
        // assertEquals(expected, FORMATER.format(first.getArrivalTime()));
        // assertEquals(expected, FORMATER.format(first.getDepartureTime()));
        assertEquals(19500, first.getArrivalTime());
        assertEquals(19500, first.getArrivalTime());
    }

    @Test
    public void verifyLastStopTime() {
        // final String expected = "2013-11-05 00:36";

        final TripStopTime last = trip.getStopTimes().get(28);
        // assertEquals(expected, FORMATER.format(last.getArrivalTime()));
        // assertEquals(expected, FORMATER.format(last.getDepartureTime()));
        assertEquals(21480, last.getArrivalTime());
        assertEquals(21480, last.getDepartureTime());
    }
}
