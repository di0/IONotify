package com.develdio.reminderappcore.services.async;

// TODO a ideia e criar um factory que vai criar um servico sincronizado ou nao
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.develdio.reminderappcore.events.ContainerEventHandle;
import com.develdio.reminderappcore.events.EventHandle;
import com.develdio.reminderappcore.services.Service;

public abstract class DeliveryAsyncService extends AsyncService {

	final private Object fetchMonitor;
	private List<? extends EventHandle<?>> listOfEventHandle;

	/**
	 * Construct with monitor from fetch.
	 */
	public DeliveryAsyncService(Object fetchMonitor) {
		this.fetchMonitor = fetchMonitor;
	}

	final public void start() {
		new Thread(this).start();
	}

	final public void stop() {
		Thread.currentThread().stop();
	}

	final public boolean isRunning() {
		return Service.isRunning;
	}

	/**
	 * Delivery an EventHandle any Implements its and received a EventHandle.
	 */
	public abstract void onEvent(List<? extends EventHandle<?>> EventHandle);

	/**
	 * Run this service.
	 */
	final public void run() {
		delivery();
	}

	/**
	 * Delivery a list of EventHandle.
	 */
	private void delivery() {
		if ( isRunning() )
		{
			log("Synchronized delivery running");
			Map<String, String> alreadyProcessed = new HashMap<String, String>();
			String idEventHandle = "0";

			while ( isRunning() )
			{
				synchronized (fetchMonitor)
				{
					try 
					{
						// Waiting by Fetch Synchronized Events
						fetchMonitor.wait();

						// Get EventHandle now
						log("EventHandle captured...");

						listOfEventHandle = ContainerEventHandle.getFromQueue();
						if ( ( null != listOfEventHandle) || ( !listOfEventHandle.isEmpty() ) )
						{
							for ( final EventHandle<?> EventHandle : listOfEventHandle )
							{
								if ( ! (alreadyProcessed
										.containsValue( EventHandle.getIdentification() ) ) )
								{
									log( "Processing EventHandle "
											+ EventHandle.getDescription() + " ID: "
											+ EventHandle.getIdentification() );

									idEventHandle = EventHandle.getIdentification();
									alreadyProcessed.put(idEventHandle, idEventHandle);
								}
							}

							onEvent(listOfEventHandle);
						}
						else
						{
							Thread.sleep(600);
						}
					}
					catch (InterruptedException ie)
					{
						log(ie.getMessage());
					}
				}
			}
		} 
		else
		{
			log("Service is not running");
		}
	}
}
