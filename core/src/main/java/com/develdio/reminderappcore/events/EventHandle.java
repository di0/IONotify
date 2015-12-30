package com.develdio.reminderappcore.events;

public interface EventHandle<T> {
		public T capture();

		public String getDescription();
		public String getIdentification();
}
