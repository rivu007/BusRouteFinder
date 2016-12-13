package com.goeuro.busroute.dao;

import com.goeuro.busroute.model.BusRoute;
import com.goeuro.busroute.model.BusStation;

/**
 * Provides data related to bus routes.
 * 
 * @author Abhilash Ghosh
 *
 */
public interface BusRouteRepository {

	/**
	 * Get route by id
	 * 
	 * @param routeId
	 *            the route id
	 */
	BusRoute getRoute(Integer routeId);

	/**
	 * Add a route to the map
	 * 
	 * @param route
	 *            route to be added
	 */
	void addRoute(BusRoute route);

	/**
	 * Add station to route
	 * 
	 * @param busRoute
	 *            the route
	 * @param busStation
	 *            the station
	 */
	void addStationToRoute(BusRoute busRoute, BusStation busStation);

	/**
	 * check if a station exists on the given route
	 * 
	 * @param route
	 *            the route
	 * @param sid
	 *            the station id
	 * @return {@code true} if exists else {@code false}
	 */
	boolean hasStation(BusRoute route, Integer sid);

	/**
	 * Clear all data
	 */
	void clear();
}
