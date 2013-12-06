package fr.itinerennes.api.client.model;

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

import com.google.gson.annotations.SerializedName;

/**
 * Class representing a trip stop time.
 * 
 * @author Olivier Boudet
 */
public class TripStopTime {

    /** Stop. */
    private Stop stop;

    /** Arrival time (seconds since midnight). */
    @SerializedName("arrival")
    private int arrivalTime;

    /** Departure time (seconds since midnight). */
    @SerializedName("departure")
    private int departureTime;

    /** Stop headsign. */
    private String stopHeadsign;

    /**
     * Gets the stop.
     * 
     * @return the stop
     */
    public Stop getStop() {

        return stop;
    }

    /**
     * Sets the stop.
     * 
     * @param stop
     *            the stop to set
     */
    public void setStop(final Stop stop) {

        this.stop = stop;
    }

    /**
     * Gets the arrivalTime.
     * 
     * @return the arrivalTime
     */
    public int getArrivalTime() {

        return arrivalTime;
    }

    /**
     * Sets the arrivalTime.
     * 
     * @param arrivalTime
     *            the arrivalTime to set
     */
    public void setArrivalTime(final int arrivalTime) {

        this.arrivalTime = arrivalTime;
    }

    /**
     * Gets the departureTime.
     * 
     * @return the departureTime
     */
    public int getDepartureTime() {

        return departureTime;
    }

    /**
     * Sets the departureTime.
     * 
     * @param departureTime
     *            the departureTime to set
     */
    public void setDepartureTime(final int departureTime) {

        this.departureTime = departureTime;
    }

    /**
     * Gets the stopHeadsign.
     * 
     * @return the stopHeadsign
     */
    public String getStopHeadsign() {

        return stopHeadsign;
    }

    /**
     * Sets the stopHeadsign.
     * 
     * @param stopHeadsign
     *            the stopHeadsign to set
     */
    public void setStopHeadsign(final String stopHeadsign) {

        this.stopHeadsign = stopHeadsign;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TripStopTime [stop=");
        builder.append(stop);
        builder.append(", arrivalTime=");
        builder.append(arrivalTime);
        builder.append(", departureTime=");
        builder.append(departureTime);
        builder.append(", stopHeadsign=");
        builder.append(stopHeadsign);
        builder.append("]");
        return builder.toString();
    }

}
