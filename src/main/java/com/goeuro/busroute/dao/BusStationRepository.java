package com.goeuro.busroute.dao;

import java.util.List;

import com.goeuro.busroute.model.BusRoute;
import com.goeuro.busroute.model.BusStation;

/**
 * Provides data related to bus stations.
 * 
 * @author Abhilash Ghosh
 *
 */
public interface BusStationRepository {

	/**
	 * Checks if the station with given id exists
	 * 
	 * @param stationId
	 *            station id
	 * @return {@code true} if exists else {@code false}
	 */
	boolean isStation(int stationId);

	/**
	 * Gets the station with given id
	 * 
	 * @param stationId
	 *            station id
	 * @return {@code BusStation} if exists else {@code null};
	 */
	BusStation getStation(int stationId);

	/**
	 * Adds a station to the station repo.
	 * 
	 * @param station
	 */
	void addStation(BusStation station);

	/**
	 * Add route to stations.
	 * 
	 * @param busRoute
	 *            the route
	 * @param busStation
	 *            the station
	 */
	void addRouteToStation(BusRoute busRoute, BusStation busStation);

	/**
	 * Get routes for given statoin
	 * 
	 * @param sid
	 *            the station
	 * @return {@code List of routes} else {@code null};
	 */
	List<BusRoute> getRoutesFor(Integer sid);

	/**
	 * Clear all data
	 */
	void clear();

}
