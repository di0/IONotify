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

import com.develdio.reminderappcore.database.GenericDao;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MessageDao {
	private GenericDao db = null;

	public MessageDao(GenericDao db) {
		this.db = db;
	}

	public List<Message> findAll() {
		List<Message> listOfAllMessage = new ArrayList<Message>();

		try {
			ResultSet rs = db.selectFrom("SELECT * FROM mensagem");
			while (rs.next()) {
				Message message = new Message();
				message.setID(rs.getString("id"));
				message.setSubject(rs.getString("assunto"));
				message.setContents(rs.getString("conteudo"));
				message.setStart(rs.getDate("date_inicio"));
				message.setEnd(rs.getDate("date_fim"));

				listOfAllMessage.add(message);
			}

			return listOfAllMessage;

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	// TODO lancar excecao para mais de uma mensagem encontrada
	public Message findByID(int id) {
		try {
			ResultSet rs = db.selectFrom("SELECT * FROM mensagem WHERE id = " + id);
			Message message = new Message();;
			while (rs.next()) {
				message.setID(rs.getString("id"));
				message.setSubject(rs.getString("assunto"));
				message.setContents(rs.getString("conteudo"));
				message.setStart(rs.getDate("date_inicio"));
				message.setEnd(rs.getDate("date_fim"));
			}
			return message;

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	public List<Message> findBetweenDate(Date init) {
		List<Message> listOfMessageByDateInterval = new ArrayList<Message>();

		try {
			String sql = "SELECT * FROM mensagem BETWEEN date_inicio = " + init;
			ResultSet rs = db.selectFrom(sql);
			while (rs.next()) {
				Message message = new Message();
				message.setID(rs.getString("id"));
				message.setSubject(rs.getString("assunto"));
				message.setContents(rs.getString("conteudo"));
				message.setStart(rs.getDate("date_inicio"));
				message.setEnd(rs.getDate("date_fim"));
	
				listOfMessageByDateInterval.add(message);
			}

			return listOfMessageByDateInterval;

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public List<Message> findByDate(String date) {

		List<Message> messageContainer = new ArrayList<Message>();
		String r = null;

		try {
			SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			java.util.Date dateParse = newDate.parse(date);

			long dateFinal = dateParse.getTime();
			java.util.Date resultDate = new Date(dateFinal);
			r = newDate.format(resultDate);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		try {
			String sql = "SELECT * FROM mensagem WHERE DATE_FORMAT(date_inicio, '%Y-%m-%d %H:%i') = '" + r + "'";

			ResultSet rs = db.selectFrom(sql);
			while (rs.next()) {
				Message message = new Message();
				message.setID(rs.getString("id"));
				message.setSubject(rs.getString("assunto"));
				message.setContents(rs.getString("conteudo"));
				message.setStart(rs.getDate("date_inicio"));
				message.setEnd(rs.getDate("date_fim"));

				messageContainer.add(message);
			}

			//disconnect();

			return messageContainer;

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
