package com.goeuro.busroute.model;

/**
 * Model class represents a bus route. It connects two or more {@link BusStation}.
 * 
 * @since 1.0
 * @version 1.0
 * @author Abhilash Ghosh
 *
 */
public class BusRoute {

	private final Integer id;

	public BusRoute(Integer id) {
		super();
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

}
