package com.goeuro.busroute.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.goeuro.busroute.model.BusRoute;
import com.goeuro.busroute.model.BusStation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

/**
 * in memory data repository
 * 
 * @author Abhilash Ghosh
 *
 */
@Repository
public class InMemoryBusRoutesRepository implements BusRouteRepository {

	private BusStationRepository stationRepo;

	final Map<Integer, BusRoute> busRoutes = new HashMap<>();

	final ListMultimap<BusRoute, BusStation> routeToStations = ArrayListMultimap.create();

	@Autowired
	public InMemoryBusRoutesRepository(BusStationRepository stationRepo) {
		this.stationRepo = stationRepo;
	}

	@Override
	public BusRoute getRoute(Integer routeId) {
		return busRoutes.get(routeId);
	}

	@Override
	public void addRoute(BusRoute route) {
		busRoutes.put(route.getId(), route);

	}

	@Override
	public void addStationToRoute(BusRoute busRoute, BusStation busStation) {
		synchronized (this) {
			routeToStations.put(busRoute, busStation);
		}

	}

	@Override
	public boolean hasStation(BusRoute route, Integer arr_sid) {
		List<BusStation> collection = routeToStations.get(route);
		BusStation arrivalStation = stationRepo.getStation(arr_sid);
		return collection.contains(arrivalStation);
	}

	@Override
	public void clear() {
		busRoutes.clear();
		routeToStations.clear();

	}

}
