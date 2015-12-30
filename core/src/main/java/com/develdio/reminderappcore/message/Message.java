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

public class Message
{
	private String id;
	private String subject;
	private String contents;
	Date start;
	Date end;

	public String getID() { return id; }
	public String getSubject() { return subject; }
	public String getContent() { return contents; }
	public Date getStart() { return start; }
	public Date getEnd() { return end; }

	public void setID(String id) { this.id = id; }
	public void setSubject(String subject) { this.subject = subject; }
	public void setContents(String contents) { this.contents = contents; }
	public void setStart(Date start) { this.start = start; }
	public void setEnd(Date end) { this.end = end; }
}
