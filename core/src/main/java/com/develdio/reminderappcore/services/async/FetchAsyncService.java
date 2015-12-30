/*
 *  Copyright 2014 Diogo Pereira Pinto <dio@lognull.com>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package com.develdio.reminderappcore.services.async;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.develdio.reminderappcore.events.ContainerEventHandle;
import com.develdio.reminderappcore.events.EventHandle;
import com.develdio.reminderappcore.events.EventTime;
import com.develdio.reminderappcore.events.GenerateEventHandle;

public abstract class FetchAsyncService extends AsyncService {
	// Delivery monitor side
	private final Object deliveryMonitor;

	// Event cached
	private final Map<String, String> eventProcessed = new HashMap<String, String>();

	public FetchAsyncService(Object deliveryMonitor) {
		this.deliveryMonitor = deliveryMonitor;
	}

	final public void start() {
		new Thread(this).start();
	}

	final public void stop() {
		Thread.currentThread().stop();
	}

	final public boolean isRunning() {
		return AsyncService.hasStarted;
	}

	final public void run() {
		fetch();
	}

	private void fetch() {
		if ( isRunning() )
		{
			log("Async fetch running");
			while ( isRunning() )
			{
				synchronized (deliveryMonitor) {
					try
					{
						GenerateEventHandle generateEvent = GenerateEventHandle
								.byTime(EventTime.NOW);
						List<EventHandle<?>> listOfEventHandle = generateEvent.getList();
						removeEventAlreadyFetched(listOfEventHandle);
						if ( listOfEventHandle.isEmpty() )
							Thread.sleep(600);
						else
						{
							log("Event found");
							// Get event from Container
							ContainerEventHandle
									.addFromQueue(EventTime.NOW, listOfEventHandle);
							// Notify Delivery side about
							deliveryMonitor.notify();
						}
					}
					catch (InterruptedException ie)
					{
						log( ie.getMessage() );
					}
					catch (Exception e)
					{
						log( e.getMessage() );
					}
				}
			}
		}
	}

	private void removeEventAlreadyFetched(List<EventHandle<?>> listOfEventHandle) {
		for ( Iterator<EventHandle<?>> iteratorEvent = listOfEventHandle.iterator(); iteratorEvent.hasNext(); )
		{
			EventHandle<?> eventHandle = iteratorEvent.next();
			if (eventHandle.getIdentification().equals(eventProcessed.get(eventHandle.getIdentification())))
			{
				iteratorEvent.remove();
			}
			else
			{
				eventProcessed.put(eventHandle.getIdentification(), eventHandle.getIdentification());
			}
		}
	}
}
