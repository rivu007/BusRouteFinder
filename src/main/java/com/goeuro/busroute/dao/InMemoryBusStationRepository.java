package com.goeuro.busroute.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.goeuro.busroute.model.BusRoute;
import com.goeuro.busroute.model.BusStation;
import org.springframework.stereotype.Repository;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

/**
 * In memory data repo. Not thread safe
 * 
 * @author Abhilash Ghosh
 *
 */
@Repository
public class InMemoryBusStationRepository implements BusStationRepository {

	private Map<Integer, BusStation> stations = new HashMap<>();

	private final ListMultimap<BusStation, BusRoute> stationToRoutes = ArrayListMultimap.create();

	@Override
	public boolean isStation(int stationId) {
		return stations.containsKey(stationId);
	}

	@Override
	public BusStation getStation(int stationId) {
		return stations.get(stationId);
	}

	@Override
	public void addStation(BusStation station) {
		stations.put(station.getId(), station);
	}

	@Override
	public void addRouteToStation(BusRoute busRoute, BusStation busStation) {
		synchronized (this) {
			stationToRoutes.put(busStation, busRoute);
		}

	}

	@Override
	public List<BusRoute> getRoutesFor(Integer dep_sid) {
		BusStation busStation = stations.get(dep_sid);
		return stationToRoutes.get(busStation);
	}

	@Override
	public void clear() {
		stations.clear();
		stationToRoutes.clear();
	}

}
