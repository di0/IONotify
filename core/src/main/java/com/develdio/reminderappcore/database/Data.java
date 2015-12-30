package com.develdio.reminderappcore.database;

import java.util.List;

public interface Data<T> {
	public List<T> findAll();
}
