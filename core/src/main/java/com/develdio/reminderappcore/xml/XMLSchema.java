package com.develdio.reminderappcore.xml;

public interface XMLSchema {
	public void createTagOpen(String tagOpen);
	public void createTagClose(String tagClose);
	public void createNodesDocument();
	public String generateOutputXml();	
}
