package com.develdio.reminderappcore.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContainerEventHandle {

	static private Map<EventTime, List<EventHandle<?>>> mapEvent =
			new HashMap<EventTime, List<EventHandle<?>>>();

	public static void addFromQueue(EventTime name, List<EventHandle<?>> listOfEvent) {
		ContainerEventHandle.mapEvent.put(name, listOfEvent);
	}

	public static List<? extends EventHandle<?>> getFromQueue() {

		EventHandle<?> e = new EventHandle<String>() {

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

		List<EventHandle<?>> listOfEventHandle = new ArrayList<EventHandle<?>>();
		listOfEventHandle.add(e);

		return listOfEventHandle;
	}
}
