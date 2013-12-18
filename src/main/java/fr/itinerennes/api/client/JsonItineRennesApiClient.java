package fr.itinerennes.api.client;

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

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import fr.itinerennes.api.client.gson.ItineRennesApiGsonFactory;
import fr.itinerennes.api.client.model.Agency;
import fr.itinerennes.api.client.model.FeedInfo;
import fr.itinerennes.api.client.model.StopSchedule;
import fr.itinerennes.api.client.model.StopWithRoutes;
import fr.itinerennes.api.client.model.TripSchedule;

/**
 * Manage calls to the ItineRennes API.
 * 
 * @author Olivier Boudet
 * @author Jeremie Huchet
 */
public class JsonItineRennesApiClient implements ItineRennesApiClient {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonItineRennesApiClient.class);

    /** The HTTP Accept header name. */
    private static final String H_ACCEPT = "Accept";

    /** The HTTP client. */
    private final HttpClient httpClient;

    /** The ItineRennes API url to use. */
    private final String baseUrl;
    
    /** The gson (de)serializer. */
    private final Gson gson;

    /**
     * Creates a ItineRennes API client.
     * 
     * @param httpClient
     *            an http client
     * @param url
     *            the url of the ItineRennes API
     */
    public JsonItineRennesApiClient(final HttpClient httpClient, final String url) {

        this.httpClient = httpClient;
        this.baseUrl = url.replaceAll("/$", "");
        this.gson = ItineRennesApiGsonFactory.newInstance();
    }

    /**
     * Creates a generic request to the ItineRennes API. This method will set the request headers.
     * 
     * @param url
     *            the url where to make the call
     * @return an {@link HttpGet} to send to execute the request
     * @throws IOException 
     */
    private <T> T execute(final String url, final ResponseHandler<T> handler) throws IOException {

        final HttpGet req = new HttpGet(url);
        req.addHeader(H_ACCEPT, "application/json");
        req.addHeader(H_ACCEPT, "text/json");
        req.addHeader(H_ACCEPT, "text/plain");

        LOGGER.info("GET: {}", url);

        return httpClient.execute(req, handler);
    }

    @Override
    public FeedInfo getFeedInfo() throws IOException {
        final String urlCall = String.format("%s/infos.json", baseUrl);
        return execute(urlCall, new ApiHttpResponseHandler<FeedInfo>(FeedInfo.class, gson));
    }

    @Override
    public Agency getAgency(final String agencyId) throws IOException {
        final String urlCall = String.format("%s/agency/%s.json", baseUrl, agencyId);
        return execute(urlCall, new ApiHttpResponseHandler<Agency>(Agency.class, gson));
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.api.client.ItineRennesApiClient#getStop(java.lang.String)
     */
    @Override
    public StopWithRoutes getStop(final String stopId) throws IOException {
        final String urlCall = String.format("%s/stop/%s.json", baseUrl, stopId);
        return execute(urlCall, new ApiHttpResponseHandler<StopWithRoutes>(StopWithRoutes.class, gson));
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IOException
     * @see ItineRennesApiClient#getTripDetails(String)
     */
    @Override
    public final TripSchedule getTripDetails(final String tripId) throws IOException {

        LOGGER.debug("getTripDetails.start - tripId={}", tripId);

        final String urlCall = String.format("%s/trip-details/%s.json", baseUrl, tripId);
        final TripSchedule schedule = execute(urlCall,
                new ApiHttpResponseHandler<TripSchedule>(TripSchedule.class, gson));

        LOGGER.debug("getTripDetails.end");
        return schedule;
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IOException
     * @see ItineRennesApiClient#getScheduleForStop(String, Date)
     */
    @Override
    public final StopSchedule getScheduleForStop(final String stopId, final Date date)
            throws IOException {

        LOGGER.debug("getScheduleForStop.start - stopId={}", stopId);

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        final String urlCall = String.format("%1$s/schedule-for-stop/%2$tY/%2$tm/%2$td/%3$s.json", baseUrl, date , stopId);
        final StopSchedule schedule = execute(urlCall,
                new StopScheduleHttpResponseHandler(gson));
        schedule.setDate(date);

        LOGGER.debug("getScheduleForStop.end");
        return schedule;
    }
}
