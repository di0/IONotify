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

package com.develdio.reminderappsystem.task.async;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.develdio.reminderappcore.events.EventHandler;
import com.develdio.reminderappcore.message.Message;
import com.develdio.reminderappcore.services.async.DeliveryAsyncService;
import com.develdio.reminderappsystem.task.Task;
import com.develdio.reminderappsystem.task.builder.TaskBuilder;

public class TaskDeliveryAsyncService extends DeliveryAsyncService {

	// List of Task
	private List< Task > listOfTask = new ArrayList< Task >();

	public TaskDeliveryAsyncService( Object fetchMonitor ) {
		super( fetchMonitor );
	}

	@Override
	public void onEvent( List<? extends EventHandler< ? > > listOfEventHandle ) {

		for ( EventHandler< ? > eventHandle : listOfEventHandle )
		{
			Message message = new Message();
			message.setSubject( eventHandle.getDescription() );
			Task task = new TaskBuilder.TaskBuilderMake( message ).create();
			listOfTask.add( task );
			deliveryListOfTaskNow();
		}
	}

	/**
	 * Delivery a list of Task
	 */
	private void deliveryListOfTaskNow() {

		log( "Async delivery running" );
		Map< String, String > taskProcessed = new HashMap< String, String >();
		String idTask = "0";

		try
		{
			log( "Message fetched..." );

			// Get task now
			// listOfTask = ContainerTask.getList( TaskTime.NOW );
			if ( ( null != listOfTask  ) || ( ! listOfTask.isEmpty() ) )
			{
				for ( final Task task : listOfTask )
				{
					if ( ! taskProcessed.containsValue( task.getIdentification() ) )
					{
						log( "Processing task " +  task.getDescription() + " ID: " + 
								task.getIdentification() );

						idTask = task.getIdentification();
						taskProcessed.put( idTask, idTask );

						List< Task > listOfTask = new ArrayList< Task >();
						listOfTask.add( task );
					}
					else
					{
						Thread.sleep(600);
					}
				}
			}
		}
		catch ( InterruptedException ie )
		{
			log( ie.getMessage() );
		}
	}
}