package com.develdio.reminderappcore.services.test;

import com.develdio.reminderappcore.network.connection.server.ServerProxyAsyncService;
import com.develdio.reminderappcore.services.Service;
import com.develdio.reminderappcore.services.Service.Action;

class ManagerService {
	private Service service = null;

	private ManagerService(Service service) {
		this.service = service;
	}

	public static ManagerService setup(Service service) {
		return new ManagerService(service);
	}

	public void start() {
		try {
			service.exec(Action.S_START);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

public class ProviderServiceTest {
	public static void main(String[] args) {
	}
}
