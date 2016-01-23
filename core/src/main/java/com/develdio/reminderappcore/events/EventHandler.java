package com.develdio.reminderappcore.events;

public interface EventHandler<T> {
		public T capture();

		public String getDescription();
		public String getIdentification();
}
