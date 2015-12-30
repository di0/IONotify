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

import java.sql.Date;
import java.util.List;

import com.develdio.reminderappcore.database.GenericDao;
import com.develdio.reminderappcore.message.exception.MessageException;

public class MessageReaderImpl implements MessageReader {

	MessageDao messageDao = new MessageDao(new GenericDao());

	@Override
	public List<Message> fetchMessage() throws MessageException {
		return messageDao.findAll();
	}

	@Override
	public Message getMessageById(int id) throws MessageException  {
		return messageDao.findByID(id);
	}

	@Override
	public List<Message> getMessageBetween(long beginning) {
		return messageDao.findBetweenDate(new Date(beginning));
	}

	@Override
	public List<Message> getMessageByDate(String date) {
		return messageDao.findByDate(date);
	}
}
