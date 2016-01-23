package com.develdio.reminderappsystem.task;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

public class ContainerTask {
	static private Map< TaskTime, List< Task > > mapTask =
			new HashMap< TaskTime, List< Task > >();

	public static void addList( TaskTime name, List< Task > listOfTask ) {
		ContainerTask.mapTask.put( name, listOfTask );
	}

	public static List< Task > getList( TaskTime taskTime ) {
		return mapTask.get( taskTime );
	}
}
