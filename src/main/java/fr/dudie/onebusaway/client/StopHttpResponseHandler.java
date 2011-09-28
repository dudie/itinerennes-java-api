/* 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.dudie.onebusaway.client;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.dudie.onebusaway.exceptions.OneBusAwayException;
import fr.dudie.onebusaway.model.Route;
import fr.dudie.onebusaway.model.Stop;

/**
 * Handles responses for a call to the "stop" method of the OneBusAway API.
 * 
 * @author Olivier Boudet
 */
public final class StopHttpResponseHandler implements ResponseHandler<Stop> {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(StopHttpResponseHandler.class);

    /**
     * {@inheritDoc}
     * 
     * @see org.apache.http.client.ResponseHandler#handleResponse(org.apache.http.HttpResponse)
     */
    @Override
    public Stop handleResponse(final HttpResponse response) throws IOException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("handleResponse.start");
        }

        Stop stop = null;
        String content = null;
        JSONObject data;

        try {
            content = EntityUtils.toString(response.getEntity(), "utf-8");
            data = OneBusAwayUtils.getServiceResponse(content);

            if (data != null) {
                stop = new Stop();

                final JSONObject jsonEntry = data.getJSONObject("entry");
                final JSONObject jsonReferences = data.getJSONObject("references");
                final JSONArray jsonRoutes = jsonReferences.getJSONArray("routes");

                final HashMap<String, Route> routes = OneBusAwayUtils
                        .getReferencedRoutes(jsonRoutes);
                stop = OneBusAwayUtils.convertJsonObjectToStop(jsonEntry, routes);

            }
        } catch (final JSONException e) {
            LOGGER.error("error while parsing the response from OneBusAway api", e);
            throw new IOException("error while parsing the response from OneBusAway api\n"
                    + content);
        } catch (final OneBusAwayException e) {
            LOGGER.error("OneBusAway api returned an error", e);
            throw new IOException(e.getMessage());
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("handleResponse.end");
        }
        return stop;
    }
}