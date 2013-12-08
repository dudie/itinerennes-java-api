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

import static java.lang.String.format;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import fr.itinerennes.api.client.model.Agency;
import fr.itinerennes.api.client.model.FeedInfo;
import fr.itinerennes.api.client.model.Route;
import fr.itinerennes.api.client.model.ScheduleStopTime;
import fr.itinerennes.api.client.model.Stop;
import fr.itinerennes.api.client.model.StopSchedule;
import fr.itinerennes.api.client.model.TripSchedule;
import fr.itinerennes.api.client.model.TripStopTime;

public class GsonSerializationPreviewTest {

    private static final Map<Integer, Stop> STOPS = new HashMap<Integer, Stop>();
    private static final Map<String, Route> ROUTES = new HashMap<String, Route>();
    private static final Map<String, ScheduleStopTime> STOP_TIMES = new HashMap<String, ScheduleStopTime>();
    private static final Map<String, TripStopTime> TRIP_STOP_TIMES = new HashMap<String, TripStopTime>();

    private static Gson gson = ItineRennesApiGsonFactory.newInstance(true);
    private Date date;

    @Before
    public void initilizeDate() {
        final Calendar cal = Calendar.getInstance();
        // 2012-08-22 00:00
        cal.set(2012, 7, 22, 13, 0);
        this.date = cal.getTime();
    }

    @Test
    public void previewFeedInfo() {

        final FeedInfo fi = new FeedInfo();
        fi.setEnd(new Date());
        fi.setLang(Locale.FRENCH);
        fi.setPublisherName("Aaaa Bbbb");
        fi.setPublisherUrl("http://www.publisher/aaa");
        fi.setStart(new Date());
        fi.setVersion("vX");

        preview("FeedInfo", fi);
    }

    @Test
    public void previewAgency() {

        final Agency a = new Agency();
        a.setId("AAA");
        a.setLang(Locale.FRENCH);
        a.setName("Aaaa Bbbb");
        a.setPhone("0123456789");
        a.setTimezone(TimeZone.getDefault());
        a.setUrl("http://www.agency.com/aaa");

        preview("Agency", a);
    }

    @Test
    public void previewStopSchedule() {

        final StopSchedule ss = new StopSchedule();
        ss.setDate(date);
        ss.setStop(getStop(42));
        ss.getStopTimes().add(getScheduleStopTime("AAA", date, 1, 3));
        ss.getRoutes().add(getRoute("AAA"));
        ss.getStopTimes().add(getScheduleStopTime("BBB", date, 2, 4));
        ss.getRoutes().add(getRoute("BBB"));

        preview("StopSchedule", ss);
    }

    @Test
    public void previewTripSchedule() {

        final TripSchedule ts = new TripSchedule();
        ts.setRoute(getRoute("AAA"));
        ts.getStopTimes().add(getTripStopTime("AAA", 1, date, 8, 3));
        ts.getStopTimes().add(getTripStopTime("AAA", 1, date, 9, 12));

        preview("TripSchedule", ts);
    }

    private TripStopTime getTripStopTime(final String routeId, final int stopId, final Date date, final int h, final int m) {

        final String key = format("%s %d /%s_%d_%d", routeId, stopId, date, h, m);

        final TripStopTime tst;
        if (TRIP_STOP_TIMES.containsKey(key)) {
            tst = TRIP_STOP_TIMES.get(key);
        } else {
            final Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            tst = new TripStopTime();
            cal.add(Calendar.HOUR, h);
            tst.setArrivalTime((int) (cal.getTimeInMillis() / 1000));
            cal.add(Calendar.MINUTE, m);
            tst.setDepartureTime((int) (cal.getTimeInMillis() / 1000));
            tst.setStop(getStop(stopId));
            tst.setStopHeadsign(format("%s headsign", routeId));
            TRIP_STOP_TIMES.put(key, tst);
        }

        return tst;
    }

    private static void preview(final String name, final Object o) {
        System.out.printf("=== [%1$s] ===\n%2$s\n=== [/%1$s] ===\n", name, gson.toJson(o));
    }

    private static Stop getStop(final int code) {

        final Stop stop;
        if (STOPS.containsKey(code)) {
            stop = STOPS.get(code);
        } else {
            stop = new Stop();
            stop.setCode(code);
            stop.setDirection(format("stop_%d direction", code));
            stop.setId(format("stop_%d", code));
            stop.setLat(1.745035);
            stop.setLon(-0.507234);
            stop.setName(format("stop_%d name", code));
            STOPS.put(code, stop);
        }
        return stop;
    }

    private static Route getRoute(final String routeId) {

        final Route route;
        if (ROUTES.containsKey(routeId)) {
            route = ROUTES.get(routeId);
        } else {
            route = new Route();
            route.setId(routeId);
            route.setAgencyId("1");
            route.setColor(format("black-%s", routeId));
            route.setTextColor(format("white-%s", routeId));
            route.setLongName(format("route %s long name", routeId));
            route.setShortName(format("route %s short name", routeId));
            route.setDescription(format("route %s description", routeId));
            route.setType(0);
            ROUTES.put(routeId, route);
        }
        return route;
    }

    private static ScheduleStopTime getScheduleStopTime(final String routeId, final Date date, final int h, final int m) {

        final String key = format("%s /%s_%d_%d", routeId, date, h, m);

        final ScheduleStopTime sst;
        if (STOP_TIMES.containsKey(key)) {
            sst = STOP_TIMES.get(key);
        } else {
            final Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            sst = new ScheduleStopTime();
            cal.add(Calendar.HOUR, h);
            sst.setArrivalTime(new Time(cal.getTimeInMillis()));
            cal.add(Calendar.MINUTE, m);
            sst.setDepartureTime(new Time(cal.getTimeInMillis()));
            sst.setHeadsign(format("%s headsign", routeId));
            sst.setRouteId(routeId);
            sst.setRoute(getRoute("AAA"));
            STOP_TIMES.put(key, sst);
        }
        return sst;
    }
}
