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
package com.develdio.reminderappsystem.task.builder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.develdio.reminderappcore.message.Message;
import com.develdio.reminderappsystem.task.MessageTask;

public final class TaskBuilder implements MessageTask {

	private String description = "";

	private String identification = "";

	private String content = "";

	private String scheduleDate = "";

	private TaskBuilder(TaskBuilderMake m) {
		description = m.description;
		identification = m.identification;
		content = m.content;
		scheduleDate = m.scheduleDate;
	}

	public String getDescription() {
		return description;
	}

	public String getIdentification() {
		return this.identification;
	}

	public String getContent() {
		return this.content;
	}

	public String getStatus() {
		return "";
	}

	public String getOwner() {
		return "";
	}

	public String getScheduleDate() {
		return this.scheduleDate;
	}

	public static class TaskBuilderMake {

		private final Message message;
		private String description = "";
		private String identification = "";
		private String content = "";
		private String scheduleDate = "";

		public TaskBuilderMake(Message message) {
			this.message = message;
		}

		public TaskBuilder create() {
			this.description = message.getSubject();
			this.identification = message.getID();
			this.content = message.getContent();

			DateFormat gmtFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
			this.scheduleDate = gmtFormat.format(message.getStart());

			return new TaskBuilder(this);
		}
	}
}
