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
package com.develdio.reminderappcore.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.develdio.reminderappcore.database.config.DatabaseConfig;

public class ConnectionFactory {
	private static String driverdb = "mysql";
	private static Connection connection = null;

	/**
	 * Single instance for database
	 * 
	 * @return Connection single instance of driver
	 */
	public final static Connection getConnection() {
		if (connection != null) {
			try {
				if (!connection.isClosed() && connection.isValid(0))
					return connection;
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		} 

		else {
			try {
				if (driverdb.equals("mysql")) {
					connection = DriverManager
						.getConnection("jdbc:mysql://localhost/meuteste?user="
									+ "root&password=$$__1", DatabaseConfig.getInfo());
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}

		return connection;
	}
}
