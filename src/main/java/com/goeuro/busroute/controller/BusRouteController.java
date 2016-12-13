package com.goeuro.busroute.controller;

import com.goeuro.busroute.model.RouteResponse;
import com.goeuro.busroute.service.BusRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller that checks if bus service exists between two routes.
 *
 * @since 1.0
 * @version 1.0
 * @author Abhilash Ghosh
 *
 */
@RestController
@RequestMapping("/api")
public class BusRouteController {

	@Autowired
	private BusRouteService busRouteService;

	/**
	 * Check if a route exists directly between the departing station and the
	 * arriving station.
	 * 
	 * @return {@code RouteResponse} with value direct as true if route exist
	 */
	@RequestMapping(value = "/direct", method = RequestMethod.GET)
	public RouteResponse direct(@RequestParam(value = "dep_sid") int departureStationId, @RequestParam(value = "arr_sid") int arrivalStationId) {
		if (departureStationId==arrivalStationId)
			return new RouteResponse(departureStationId, arrivalStationId, false);

		return busRouteService.scanDirectRoute(departureStationId, arrivalStationId);
	}
}
