package com.goeuro.busroute;

import com.goeuro.busroute.dao.BusRouteRepository;
import com.goeuro.busroute.dao.InMemoryBusRoutesRepository;
import com.goeuro.busroute.dao.InMemoryBusStationRepository;
import com.goeuro.busroute.model.BusRoute;
import com.goeuro.busroute.model.BusStation;
import com.goeuro.busroute.model.RouteResponse;
import com.goeuro.busroute.service.BusRouteService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.goeuro.busroute.dao.BusStationRepository;

public class BusRouteServiceTest {

	private BusStationRepository stationRepo;

	private BusRouteRepository routeRepo;

	BusRouteService busRouteService;

	@Before
	public void setup() {
		stationRepo = new InMemoryBusStationRepository();
		routeRepo = new InMemoryBusRoutesRepository(stationRepo);
		busRouteService = new BusRouteService(stationRepo, routeRepo);
	}

	@After
	public void tearDown() {
		stationRepo = null;
		routeRepo = null;
		busRouteService = null;
	}

	@Test
	public void isDirect_false_depSid_equal_arrSid() {
		RouteResponse response = busRouteService.scanDirectRoute(3, 3);// externalize data
		Assert.assertFalse(response.isDirect_bus_route());
	}


	@Test
	public void isDirect_true() {
		initProviders();

		RouteResponse response = busRouteService.scanDirectRoute(2, 4);
		Assert.assertTrue(response.isDirect_bus_route());
	}

	private void initProviders() {
		BusRoute busRoute1 = new BusRoute(1);
		BusStation busStation1 = new BusStation(2);
		BusStation busStation2 = new BusStation(4);

		stationRepo.addStation(busStation1);
		stationRepo.addStation(busStation2);
		stationRepo.addRouteToStation(busRoute1, busStation1);
		stationRepo.addRouteToStation(busRoute1, busStation2);

		routeRepo.addRoute(busRoute1);
		routeRepo.addStationToRoute(busRoute1, busStation1);
		routeRepo.addStationToRoute(busRoute1, busStation2);
	}

	@Test
	public void isDirect_false() {
		initProviders();

		RouteResponse response = busRouteService.scanDirectRoute(2, 3);
		Assert.assertFalse(response.isDirect_bus_route());
	}

}
