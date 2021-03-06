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
package com.develdio.reminderappcore.message.exception;

public class MessageException extends Exception {

	private static final long serialVersionUID = 9077174868952830199L;

	public MessageException(String message) {
		super(message);
	}
}
