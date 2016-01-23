package com.develdio.reminderappcore.services.test;

import java.util.ArrayList;
import java.util.List;

import com.develdio.reminderappcore.events.EventHandler;
import com.develdio.reminderappcore.services.Service;
import com.develdio.reminderappcore.services.async.DeliveryAsyncService;

class ContainerEventHandle {
	public static List<? extends EventHandler<?>> getList() {
		EventHandler<?> e = new EventHandler<String>() {
			public String capture() {
				return "New Task";
			}

			public String getDescription() {
				return "Just a test";
			}

			public String getIdentification() {
				return "100";
			}
		};

		List<EventHandler<?>> listOfEventHandle = new ArrayList<EventHandler<?>>();
		listOfEventHandle.add(e);

		return listOfEventHandle;
	}
}

class TestDeliveryServiceSync extends DeliveryAsyncService {
	public TestDeliveryServiceSync(Object o) {
		super(o);
	}

	public void onEventHandle(List<? extends EventHandler<?>> listOfEventHandle) {
		for (EventHandler<?> EventHandle : listOfEventHandle)
			log("Task: -> " + EventHandle.capture());
	}

	@Override
	public void onEvent(List<? extends EventHandler<?>> EventHandle) {
	}
}

public class DeliverySyncServiceTest {
	public void start(Service service) throws Exception {
		service.start();
	}

	public static void main(String[] args) throws Exception {
		DeliverySyncServiceTest dst = new DeliverySyncServiceTest();

		Object monitor = new Object();		
		DeliveryAsyncService taskService = new TestDeliveryServiceSync(monitor);
		dst.start(taskService);
		monitor.notify();
	}
}
