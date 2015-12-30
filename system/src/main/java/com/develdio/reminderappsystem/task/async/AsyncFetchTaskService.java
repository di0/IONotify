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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.develdio.reminderappcore.services.async.AsyncService;
import com.develdio.reminderappsystem.task.ContainerTask;
import com.develdio.reminderappsystem.task.GenerateTask;
import com.develdio.reminderappsystem.task.Task;
import com.develdio.reminderappsystem.task.TaskTime;

public class AsyncFetchTaskService extends AsyncService {

	// Delivery side
	private final Object deliveryMonitor;

	// Task cached
	private final Map<String, String> taskProcessed = new HashMap<String, String>();

	public AsyncFetchTaskService(Object deliveryMonitor) {
		this.deliveryMonitor = deliveryMonitor;
	}

	public void run() {
		fetchListOfTaskNow();
	}

	private void fetchListOfTaskNow() {
		if (isRunning())
		{
			log("Async fetch running");
			while (isRunning())
			{
				synchronized (deliveryMonitor) {
					try
					{
						GenerateTask generateTask = GenerateTask.byDate(TaskTime.NOW);
						List<Task> listOfTask = generateTask.getList();
						removeTaskAlreadyFetched(listOfTask);
						if (listOfTask.isEmpty())
							Thread.sleep(600);
						else
						{
							log("Task found");
							// fetch task to Container
							ContainerTask.addList(TaskTime.NOW, listOfTask);
							// Notify delivery
							deliveryMonitor.notify();
						}
					}
					catch (InterruptedException ie)
					{
						log(ie.getMessage());
					}
					catch (Exception e)
					{
						log(e.getMessage());
					}
				}
			}
		}
	}

	private void removeTaskAlreadyFetched(List<Task> listOfTask) {
		for ( Iterator<Task> iteratorTask = listOfTask.iterator(); iteratorTask.hasNext(); )
		{
			Task task = iteratorTask.next();
			if (task.getIdentification().equals(taskProcessed.get(task.getIdentification())))
			{
				iteratorTask.remove();
			}
			else
			{
				taskProcessed.put(task.getIdentification(), task.getIdentification());
			}
		}
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isRunning() {
		// TODO Auto-generated method stub
		return false;
	}
}
