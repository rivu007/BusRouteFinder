package com.goeuro.busroute.model;

/**
 * Model class represents a bus station and contains all the {@link BusRoute} which could pass through
 * this station.
 * 
 * @since 1.0
 * @version 1.0
 * @author Abhilash Ghosh
 *
 */
public class BusStation {

	private final Integer id;

	public BusStation(Integer id) {
		super();
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

}
