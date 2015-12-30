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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.develdio.reminderappcore.message.Message;
import com.develdio.reminderappcore.message.MessageDisplay;
import com.develdio.reminderappcore.message.MessageDisplayImpl;
import com.develdio.reminderappcore.message.MessageReader;
import com.develdio.reminderappcore.message.MessageReaderImpl;
import com.develdio.reminderappcore.message.exception.MessageException;

import com.develdio.reminderappsystem.task.builder.TaskBuilder;


public class GenerateTask {

	private  List<Message> listOfMessage = new ArrayList<Message>();

	private GenerateTask() {
		MessageReader messageReader = new MessageReaderImpl();
		MessageDisplay messageDisplay = new MessageDisplayImpl(messageReader);
	}

	private GenerateTask(DateFormat dateFormat) {
		MessageReader messageReader = new MessageReaderImpl();
		MessageDisplay messageDisplay = new MessageDisplayImpl(messageReader);

		try
		{
			listOfMessage = messageDisplay
					.displayListMessageByDate(dateFormat.format(new Date()));
		}
		catch (MessageException me)
		{
		}
	}

	public static GenerateTask byDate(TaskTime taskTime) throws Exception {
		switch (taskTime)
		{
			case NOW:
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				return new GenerateTask(dateFormat);
			default:
				throw new Exception("Time not found");
		}
	}

	public List<Task> getList() {
		List<Task> listOfTask = new ArrayList<Task>();
		for (Message message : listOfMessage )
		{
			Task task = new TaskBuilder.TaskBuilderMake(message).create();
			listOfTask.add(task);
		}

		return listOfTask;
	}
}
