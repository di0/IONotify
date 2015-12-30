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

import com.develdio.reminderappcore.services.async.AsyncService;
import com.develdio.reminderappsystem.task.ContainerTask;
import com.develdio.reminderappsystem.task.Task;
import com.develdio.reminderappsystem.task.TaskTime;

public abstract class AsyncDeliveryTaskService extends AsyncService {

	// List of Task
	private List<Task> listOfTask = new ArrayList<Task>();

	// Notification from Fetch task
	private final Object fetchMonitor;

	public AsyncDeliveryTaskService(Object fetchMonitor) {
		this.fetchMonitor = fetchMonitor;
	}

	/**
	 * New task to the Listener.
	 * @param listOfTask List of Task received
	 */
	public abstract void onTask(List<Task> listOfTask);

	/**
	 * Run this service
	 */
	final public void run() {
		deliveryListOfTaskNow();
	}

	/**
	 * Delivery a list of Task
	 */
	private void deliveryListOfTaskNow() {
		if (isRunning())
		{
			log("Async delivery running");
			Map<String, String> taskProcessed = new HashMap<String, String>();
			String idTask = "0";

			while (isRunning())
			{
				synchronized (fetchMonitor)
				{
					try
					{
						// Wait by Delivery Task
						fetchMonitor.wait();
						log("Message fetched...");

						// Get task now
						listOfTask = ContainerTask.getList(TaskTime.NOW);
						if (null != listOfTask || !listOfTask.isEmpty())
						{
							for (final Task task : listOfTask)
							{
								if (!taskProcessed.containsValue(task.getIdentification()))
								{
									log("Processing task " + 
										task.getDescription() + " ID: " + 
										task.getIdentification());

									idTask = task.getIdentification();
									taskProcessed.put(idTask, idTask);

									List<Task> listOfTask = new ArrayList<Task>();
									listOfTask.add(task);
									onTask(listOfTask);
								}
								else
								{
									Thread.sleep(600);
								}
							}
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
