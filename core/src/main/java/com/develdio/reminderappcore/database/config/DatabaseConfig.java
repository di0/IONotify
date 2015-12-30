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
package com.develdio.reminderappcore.database.config;

import java.util.Properties;

public class DatabaseConfig {
	private static String user = "root";
	private static String passwd = "$$__1";
	private static int wait = 10000;
	private static int active = 300;
	private static boolean autoReconnect = true;

	private static final Properties cProperties = new Properties();

	public static Properties getInfo() {
		return cProperties;
	}

	public static void setConfig(String user, String password) {
		cProperties.put("User", DatabaseConfig.user);
		cProperties.put("Password", DatabaseConfig.passwd);
		cProperties.put("autoReconnect", DatabaseConfig.autoReconnect);
		cProperties.put("maxActive", DatabaseConfig.active);
		cProperties.put("maxWait", DatabaseConfig.wait);
	}
}
