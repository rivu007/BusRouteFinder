package com.goeuro.busroute.service;

import java.util.List;

import com.goeuro.busroute.model.BusRoute;
import com.goeuro.busroute.model.RouteResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.goeuro.busroute.dao.BusRouteRepository;
import com.goeuro.busroute.dao.BusStationRepository;

/**
 * Bus Router Service which check if the direct connection between exists or not.
 *
 * @since 1.0
 * @version 1.0
 * @author Abhilash Ghosh
 */
@Service
public class BusRouteService {

	private BusStationRepository busStationRepository;

	private BusRouteRepository busRouteRepository;

	@Autowired
	public BusRouteService(BusStationRepository busStationRepository, BusRouteRepository busRouteRepository) {
		this.busStationRepository = busStationRepository;
		this.busRouteRepository = busRouteRepository;
	}

	/**
	 * Checks if a direct route between the given stations exists.
	 */
	public RouteResponse scanDirectRoute(Integer departureStationId, Integer arrivalStationId) {
		if (!busStationRepository.isStation(departureStationId) || !busStationRepository.isStation(arrivalStationId))
			return new RouteResponse(departureStationId, arrivalStationId, false);;

		List<BusRoute> routes = busStationRepository.getRoutesFor(departureStationId);

		if (routes.isEmpty())
			return new RouteResponse(departureStationId, arrivalStationId, false);

		return new RouteResponse(departureStationId, arrivalStationId, isDirectRouteExists(routes, arrivalStationId));
	}

	private boolean isDirectRouteExists(List<BusRoute> busRoutes, Integer arrivalStationId){
		return busRoutes.stream().anyMatch(route -> busRouteRepository.hasStation(route, arrivalStationId));
	}
}
