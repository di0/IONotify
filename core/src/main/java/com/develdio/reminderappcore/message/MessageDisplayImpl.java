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

import java.util.ArrayList;
import java.util.List;

import com.develdio.reminderappcore.message.exception.MessageException;


/**
 * Class responsible by implemented the message show system.
 *  
 * @author dio
 * @version $id$
 */
public class MessageDisplayImpl implements MessageDisplay {

	private MessageReader messageReader = null;

	/**
	 * Dependency where this class to reader
	 *
	 * @param The stored content
	 */
	public MessageDisplayImpl(MessageReader content) {
		messageReader = content;
	}

	/**
	 * Get specified message identified by message
	 *
	 * @param int Identification of the message 
	 * @return Specified message identified by key
	 */
	@Override
	public String displaySubject(int id) throws MessageException {
		try {
			final Message message = messageReader
				.getMessageById(id);

			return message.getSubject();

		} catch (MessageException e) {
			throw new MessageException("Subject not found");
		}
	}

	/**
	 * Show a list subject of all message 
	 *
	 * @throws MessageException if not found Message
	 * @return All subjects
	 */
	@Override
	public List<String> displayListOfSubject() throws MessageException {
		try {
			List<String> subjectOfMessages = new ArrayList<String>();

			List<Message> messages = messageReader.fetchMessage();
			for (Message message : messages) {
				subjectOfMessages.add(message.getSubject());
			}

			return subjectOfMessages;

		} catch (MessageException e) {
			throw new MessageException("List of subject found");
		}
	}

	/**
	 * List all message Between Date specified
	 *
	 * @param  Date that message
	 * @throws MessageException if not found Message
	 * @return List of message specified by date
	 */ 
	@Override
	public List<Message> displayListOfMessageBetweenDate(long fromDate) {

		List<Message> messages = new ArrayList<Message>(); 

		try {
			messages = messageReader.getMessageBetween(fromDate);

		} catch (MessageException e) {
			e.printStackTrace();
		}

		return messages;
	}

	/**
	 * List all message by Date
	 *
	 * @param Date that message
	 * @throws MessageException if not found Message
	 * @return List of Message specified by date
	 */
	@Override
	public List<Message> displayListMessageByDate(String date) {

		List<Message> messages = new ArrayList<Message>();

		try {
			messages = messageReader.getMessageByDate(date);

		} catch (MessageException e) {
			e.printStackTrace();
		}

		return messages;
	}
}
