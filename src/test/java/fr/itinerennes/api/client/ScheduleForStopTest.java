package fr.itinerennes.api.client;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import fr.itinerennes.api.client.model.ScheduleStopTime;
import fr.itinerennes.api.client.model.StopSchedule;
import fr.itinerennes.junit.rules.RunWithWebServer;

/**
 * Check schedule-for-stop deserialization.
 * 
 * @author Jeremie Huchet
 */
public class ScheduleForStopTest {

    private static final SimpleDateFormat FORMATER = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    /** Run a web server. */
    @Rule
    public static final RunWithWebServer SERVER = new RunWithWebServer("/www");

    /** Schedule for stop deserialization result. */
    private StopSchedule schedule;

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

        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        calendar.setTime(sdf.parse("2013-11-04"));

        schedule = obaClient.getScheduleForStop("2_1017", calendar.getTime());
    }

    @Test
    public void referenceDateIsValid() {
        assertEquals("2013-11-04 00:00", FORMATER.format(schedule.getDate()));
    }

    @Test
    public void verifyStopAttributes() {
        assertEquals("the stop id should be 2_1017", "2_1017", schedule.getStop().getId());
        assertEquals("the stop name should be Les Halles", "Les Halles", schedule.getStop().getName());
    }

    @Test
    public void verifyStopTimes() {
        assertEquals("378 stop times should be returned by the api", 378, schedule.getStopTimes().size());

        final Map<String, Integer> counts = new HashMap<String, Integer>();
        for (final ScheduleStopTime stopTime : schedule.getStopTimes()) {
            final String route = stopTime.getRoute().getShortName();
            final int before = counts.containsKey(route) ? counts.get(route) : 0;
            counts.put(route, before + 1);
        }
        assertEquals("96 stop times should be returned by the api for line 1", new Integer(96), counts.get("1"));
        assertEquals("116 stop times should be returned by the api for line 5", new Integer(116), counts.get("5"));
        assertEquals("119 stop times should be returned by the api for line 9", new Integer(119), counts.get("9"));
        assertEquals("47 stop times should be returned by the api for line 57", new Integer(47), counts.get("57"));
    }

    @Test
    public void verifyFirstStopTime() {
        final String expected = "2013-11-04 05:46";

        final ScheduleStopTime first = schedule.getStopTimes().get(0);
        assertEquals(expected, FORMATER.format(first.getArrivalTime()));
        assertEquals(expected, FORMATER.format(first.getDepartureTime()));
    }

    @Test
    public void verifyTodayLastStopTime() {
        final String expected = "2013-11-04 23:51";

        final ScheduleStopTime last = schedule.getStopTimes().get(371);
        assertEquals(expected, FORMATER.format(last.getArrivalTime()));
        assertEquals(expected, FORMATER.format(last.getDepartureTime()));
    }

    @Test
    public void verifyLastStopTime() {
        final String expected = "2013-11-05 00:36";

        final ScheduleStopTime last = schedule.getStopTimes().get(377);
        assertEquals(expected, FORMATER.format(last.getArrivalTime()));
        assertEquals(expected, FORMATER.format(last.getDepartureTime()));
    }
}
