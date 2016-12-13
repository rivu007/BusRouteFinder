package com.goeuro.busroute.listener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.goeuro.busroute.model.BusRoute;
import com.goeuro.busroute.model.BusStation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.goeuro.busroute.listener.event.RestartEvent;
import com.goeuro.busroute.dao.BusRouteRepository;
import com.goeuro.busroute.dao.BusStationRepository;

import rx.Observable;
import rx.Subscription;
import rx.observables.StringObservable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Import the bus routes from given file. Initializes the routes from the file
 * before the application starts. This prevents unwanted calls to the service
 * Until the service is ready to receive calls.
 * 
 * @author Abhilash Ghosh
 *
 */
@Component
public class BusRoutesImporter implements ApplicationListener<ContextRefreshedEvent> {

	public static final Logger LOG = LoggerFactory.getLogger(BusRoutesImporter.class);
	@Value("${file.name}")
	private String fileName;

	private BusRouteRepository routeRepo;
	private BusStationRepository stationRepo;

	private final Subject<Object, Object> subject = new SerializedSubject<>(PublishSubject.create());
	private Subscription subscriber;

	@Autowired
	public BusRoutesImporter(BusRouteRepository routeRepo, BusStationRepository stationRepo) {
		this.routeRepo = routeRepo;
		this.stationRepo = stationRepo;
	}

	@PostConstruct()
	public void setup() {
		initialize();
	}

	@PreDestroy
	public void cleanup() {
		subscriber.unsubscribe();
	}

	// initialize routes data before bringing the service up.
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		subject.onNext(event);
	}

	@EventListener()
	public void restart(RestartEvent event) {
		subject.onNext(event);
	}

	public void initialize() {
		subscriber = subject.subscribe(event -> {
			routeRepo.clear();
			stationRepo.clear();

			File busRoutesFile = loadFile();

			try {
				Observable<String> routesFile = StringObservable.from(new FileReader(busRoutesFile));

				Observable<String> routes = StringObservable.byLine(routesFile).cache();

				validateTotalRoutes(routes);

				validateUniqueStationsAndStore(routes.skip(1));

			} catch (FileNotFoundException e) {

				if (LOG.isDebugEnabled())
					LOG.debug(e.getLocalizedMessage(), e);
			}
		});
	}

	// checks if dep_sid != arr_sid
	private void validateUniqueStationsAndStore(Observable<String> routes) {

		routes.map(route -> Arrays.asList(route.split(" "))) // gets route id
																// and station
																// id as list
				.forEach(route -> extractAndAdd(route));
	}

	// extract route, validate duplicate stations and add them to
	// the repo
	private void extractAndAdd(List<String> route) {
		List<Integer> routesAndStationIds = route.stream().map(Integer::parseInt).collect(Collectors.toList());

		Integer routeId = routesAndStationIds.remove(0);// 0 represents the
														// route id

		validateStation(routesAndStationIds);
		addRouteAndStations(routeId, routesAndStationIds);
	}

	private void validateStation(List<Integer> stationIds) {
		Set<Integer> uniqueStations = stationIds.stream().collect(Collectors.toSet());

		if (uniqueStations.size() != stationIds.size())
			throw new RuntimeException("There cannot be two stations with same ids on one route"); // use
																									// resource
																									// bundle
	}

	private void addRouteAndStations(Integer routeId, List<Integer> routesAndStationIds) {

		routesAndStationIds.stream().forEach(stationId -> addRouteAndStation(routeId, stationId));
	}

	private void addRouteAndStation(Integer routeId, Integer stationId) {

		BusRoute route = routeRepo.getRoute(routeId);
		BusStation station = stationRepo.getStation(stationId);

		if (route == null)
			route = new BusRoute(routeId);
		if (station == null)
			station = new BusStation(stationId);

		routeRepo.addRoute(route);
		routeRepo.addStationToRoute(route, station);

		stationRepo.addStation(station);
		stationRepo.addRouteToStation(route, station);

	}

	/**
	 * Validates if N routes followed by N number of routes exists. Eg: 3 routes
	 * followed by 3 route values should exist.
	 * 
	 * @param routes
	 *            observable routes
	 * 
	 */
	private void validateTotalRoutes(Observable<String> routes) {

		Observable<Integer> totalRoutesStated = routes.first().map(Integer::parseInt);

		Observable<Integer> totalRoutesCounted = routes.count().map(count -> Math.subtractExact(count, 1));

		Observable.sequenceEqual(totalRoutesStated, totalRoutesCounted).subscribe(equal -> {
			if (!equal)
				throw new RuntimeException(
						"Number of routes stated and number of routes found is different. Please check and restart"); // use
																														// resource
																														// bundle
		});
	}

	// check if the given file is a file or not
	private File loadFile() {
		if (fileName == null)
			throw new RuntimeException("Please specify the \"--file.name\" property for the bus route provider"); // can
																													// use
																													// custom
																													// exceptions
																													// as
																													// well

		File routesFile = new File(fileName);

		if (!routesFile.isFile())
			throw new RuntimeException("Given File is not a file or ca. Please check and restart"); // can
																									// use
																									// custom
																									// exceptions
																									// as
																									// well
		return routesFile;
	}
}
