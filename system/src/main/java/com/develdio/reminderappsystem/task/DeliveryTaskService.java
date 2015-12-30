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

package com.develdio.reminderappsystem.task;

import java.util.List;

import com.develdio.reminderappsystem.task.Task;

import com.develdio.reminderappsystem.task.async.AsyncDeliveryTaskService;

public class DeliveryTaskService extends AsyncDeliveryTaskService {

	public DeliveryTaskService(Object fetchMonitor) {
		super(fetchMonitor);
	}

	@Override
	public void onTask(List<Task> listOfTask) {
		for (Task task : listOfTask)
			log(task.getDescription());
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
