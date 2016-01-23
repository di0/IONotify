package com.develdio.reminderappcore.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContainerEventHandle {

	static private Map< EventTime, List< EventHandler<?> > > mapEvent =
			new HashMap< EventTime, List< EventHandler< ? > > >();

	public static void addFromQueue( EventTime name, List<EventHandler< ? > > listOfEvent ) {
		ContainerEventHandle.mapEvent.put( name, listOfEvent );
	}

	public static List< ? extends EventHandler< ? > > getFromQueue() {

		EventHandler< ? > e = new EventHandler< String >() {

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

		List< EventHandler< ? > > listOfEventHandle = new ArrayList< EventHandler< ? > >();
		listOfEventHandle.add( e );

		return listOfEventHandle;
	}
}
