package com.develdio.reminderappcore.services;

public class ManagerService {
	private Service service = null;

	private ManagerService( Service service ) {
		this.service = service;
	}

	public static ManagerService setup( Service service ) {
		return new ManagerService( service );
	}

	public void start() {
		try {
			service.start();
		} catch (Exception e) {}
	}
}
