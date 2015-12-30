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
package com.develdio.reminderappcore.message;

import java.util.List;

import com.develdio.reminderappcore.message.exception.MessageException;

public interface MessageDisplay {
	/**
	 * Get specified message identified by message
	 * 
	 * @param Message which search 
	 * @return Specified message identified by key
	 */
	public String displaySubject(int key) throws MessageException;

	/**
	 * All subject of message
	 * 
	 * @param Message which search 
	 * @return All suject of message
	 */
	public List<String> displayListOfSubject() throws MessageException;

	/**
	 * List message specified between Date annotation
	 * 
	 * @return All message between Date
	 */
	public List<Message> displayListOfMessageBetweenDate(long fromDate) throws MessageException;

	/**
	 * Get message by Date
	 * 
	 * @param date
	 */
	public List<Message> displayListMessageByDate(String date) throws MessageException;
}
