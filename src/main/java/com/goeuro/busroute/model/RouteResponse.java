package com.goeuro.busroute.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response representation of a route
 *
 * @since 1.0
 * @version 1.0
 * @author Abhilash Ghosh
 *
 */
public class RouteResponse {
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private final int departureStationId;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private final int arrivalStationId;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private final boolean direct_bus_route;

	@JsonCreator
	public RouteResponse(@JsonProperty("dep_sid") int departureStationId,
						 @JsonProperty("arr_sid") int arrivalStationId,
						 @JsonProperty("direct_bus_route") boolean direct_bus_route) {
		this.departureStationId = departureStationId;
		this.arrivalStationId = arrivalStationId;
		this.direct_bus_route = direct_bus_route;
	}

	public int getDep_sid() {
		return departureStationId;
	}

	public int getArr_sid() {
		return arrivalStationId;
	}

	public boolean isDirect_bus_route() {
		return direct_bus_route;
	}
}
