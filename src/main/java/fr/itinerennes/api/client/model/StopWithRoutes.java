package fr.itinerennes.api.client.model;

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
