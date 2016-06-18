package com.develdio.reminderappcore.xml;

import com.develdio.reminderappcore.logs.Log;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.HashMap;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public final class XMLEngine implements XMLSchema {

	// Class with methods converted into XML info
	private Class<?> clazz;

	private Object o;

	// Output XML Factory
	private XMLOutputFactory outXml;

	// Writer Event
	private XMLEventWriter eventWriter;

	// Factory Event
	private XMLEventFactory eventFactory;

	// Tags default open and close
	private String tagOpen = "Default";
	private String tagClose = "Default";

	// Special Event
	private XMLEvent end;
	private XMLEvent tab;

	private ByteArrayOutputStream baos = new ByteArrayOutputStream();

	public XMLEngine( Object o ) {
		try {
			// Structure class to XML construct
			this.clazz = o.getClass();
			this.o = o;

			// create XMLEventWriter
			outXml = XMLOutputFactory.newInstance();
			eventWriter = outXml
					.createXMLEventWriter(new PrintStream(this.baos));

			// create an EventFactory
			eventFactory = XMLEventFactory.newInstance();
			end = eventFactory.createDTD("\n");
			tab = eventFactory.createDTD("\t");

			// create and write Start Tag
			StartDocument startDocument = eventFactory.createStartDocument();
			eventWriter.add(startDocument);
			eventWriter.add(end);
		}
		catch (XMLStreamException xse)
		{
			log( xse.getMessage() );
		}
	}

	public String generateOutputXml() {
		return baos.toString();
	}

	final public void createTagOpen( String tagOpen ) {
		try
		{
			this.tagOpen = tagOpen;

			// create config open tag
			StartElement startElement = eventFactory
					.createStartElement( "", "", this.tagOpen );
			eventWriter.add( startElement );
			eventWriter.add( end);
		}
		catch ( XMLStreamException xse )
		{
			log( xse.getMessage() );
		}
	}

	final public void createTagClose( String tagClose ) {
		try
		{
			this.tagClose = tagClose;

			// create confi close tag
			EndElement endElement = eventFactory
					.createEndElement( "", "", this.tagClose );
			eventWriter.add( endElement );
			eventWriter.add( end );
			eventWriter.add( eventFactory.createEndDocument() );
			eventWriter.close();
		} catch ( XMLStreamException xse ) {
			log( xse.getMessage() );
		}
	}

	final public void createNodesDocument() {
		try
		{
			Map< String, Method > mapOfNameMethod = createMapNameAndMethods();
			for ( Map.Entry<String, Method> entry : mapOfNameMethod.entrySet() )
			{
				String node = entry.getKey();
				String content = (String) entry.getValue()
						.invoke( this.o );
				createNode(node, content);
			}
		}
		catch ( java.lang.IllegalAccessException iae )
		{
			log( iae.getMessage() );
		}
		catch ( java.lang.reflect.InvocationTargetException ite )
		{
			log( ite.getMessage() );
		}
	}

	private void createNode( String node, String content ) {
		try 
		{
			// Open node child
			StartElement sElement = eventFactory.createStartElement( "", "", node );

			eventWriter.add( tab );
			eventWriter.add( sElement );

			 // Input content
			Characters characters = eventFactory.createCharacters( content );
			this.eventWriter.add( characters );

			// Close node child
			EndElement eElement = eventFactory.createEndElement( "", "", node );
			this.eventWriter.add( eElement );
			this.eventWriter.add( end );

		}
		catch ( XMLStreamException e ) {}
	}

	private Map< String, Method > createMapNameAndMethods() {
		Map< String, Method > mapOfNameMethod = new HashMap< String, Method >();

		Method[] methods = this.clazz.getMethods();
		for ( Method method : methods )
		{
			String nameMethod = replaceNameFromMethod( method.getName() );
			if ( ! ( "".equals( nameMethod ) ) )
				mapOfNameMethod.put( nameMethod, method );
		}

		return mapOfNameMethod;
	}

	private String replaceNameFromMethod(String nameMethod) {
		if ( nameMethod.startsWith( "get" ) && ! "getClass".equalsIgnoreCase( nameMethod ) )
		{
			nameMethod = nameMethod.replaceAll( "get", "" );
			while ( nameMethod.startsWith( "_" ) )
			{
				nameMethod = nameMethod.replaceAll( "_", "" );
			}

			return nameMethod;
		}

		return "";
	}

	private void log( String message ) {
		Log.getLogInstance(XMLEngine.class).logDebug( message );
	}
}
