package fr.itinerennes.api.client.model;

import java.util.List;

/**
 * @author Jeremie Huchet
 */
public class StopWithRoutes extends Stop {

    /** The routes serving this stop. */
    private List<Route> routes;

    /**
     * Gets the routes serving this stop.
     * 
     * @return the routes serving this stop
     */
    public List<Route> getRoutes() {
        return routes;
    }

    /**
     * Sets the routes serving this stop.
     * 
     * @param routes
     *            the routes serving this stop to set
     */
    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

}
