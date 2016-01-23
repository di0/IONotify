package com.develdio.reminderappcore.events;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.develdio.reminderappcore.message.Message;
import com.develdio.reminderappcore.message.MessageDisplay;
import com.develdio.reminderappcore.message.MessageDisplayImpl;
import com.develdio.reminderappcore.message.MessageReader;
import com.develdio.reminderappcore.message.MessageReaderImpl;
import com.develdio.reminderappcore.message.exception.MessageException;

public class GenerateEventHandle {

	final private MessageReader messageReader;

	final private MessageDisplay messageDisplay;

	final private  List< EventHandler< ? > > listOfEventHandle =
			new ArrayList< EventHandler< ? > >();

	private GenerateEventHandle() {
		messageReader = new MessageReaderImpl();
		messageDisplay = new MessageDisplayImpl( messageReader );
	}

	private GenerateEventHandle( DateFormat dateFormat ) {
		this();

		try
		{
			List< Message > listOfMessage = messageDisplay
					.displayListMessageByDate( dateFormat.format( new Date() ) );
			for ( final Message message : listOfMessage )
			{
				EventHandler< ? > event = new EventHandler< String >() {

					@Override
					public String capture() {
						return message.getContent();
					}

					@Override
					public String getDescription() {
						return message.getSubject();
					}

					@Override
					public String getIdentification() {
						return message.getID();
					}
				};

				listOfEventHandle.add( event );
			}
		}
		catch ( MessageException me )
		{
		}
	}

	public static GenerateEventHandle byTime( EventTime eventTime ) throws Exception {
		switch ( eventTime )
		{
			case NOW:
				DateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
				return new GenerateEventHandle( dateFormat );
			default:
				throw new Exception( "Time not found" );
		}
	}

	public List< EventHandler< ? > > getList() {
		return listOfEventHandle;
	}
}
