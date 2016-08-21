package com.develdio.reminderappcore.services.test;

import java.util.ArrayList;
import java.util.List;

import com.develdio.reminderappcore.events.EventHandle;
import com.develdio.reminderappcore.services.Service;
import com.develdio.reminderappcore.services.sync.DeliverySyncService;

class ContainerEventHandle
{
	public static List<? extends EventHandle<?>> getList()
	{
		EventHandle<?> e = new EventHandle<String>()
		{
			public String capture()
			{
				return "New Task";
			}

			public String getDescription()
			{
				return "Just a test";
			}

			public String getIdentification()
			{
				return "100";
			}
		};

		List<EventHandle<?>> listOfEventHandle = new ArrayList<EventHandle<?>>();
		listOfEventHandle.add( e );

		return listOfEventHandle;
	}
}

class TestDeliveryServiceSync extends DeliverySyncService 
{
	public TestDeliveryServiceSync( Object o ) 
	{
		super( o );
	}

	public void onEventHandle( List<? extends EventHandle<?>> listOfEventHandle )
	{
		for ( EventHandle<?> EventHandle : listOfEventHandle )
		{
			log( "Task: -> " + EventHandle.capture() );
		}
	}

	@Override
	public void onEvent(List<? extends EventHandle<?>> EventHandle) {}
}

public class DeliverySyncServiceTest {
	public void start(Service service) throws Exception {
		service.start();
	}

	public static void main(String[] args) throws Exception {
		DeliverySyncServiceTest dst = new DeliverySyncServiceTest();

		Object monitor = new Object();		
		DeliverySyncService taskService = new TestDeliveryServiceSync(monitor);
		dst.start(taskService);
		monitor.notify();
	}
}
