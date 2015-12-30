package com.develdio.reminderappcore.xml.helper;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.develdio.reminderappcore.message.Message;

public class XMLBuilder {

	private XMLBuilder(String xml) {
		try
		{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse( new InputSource( new StringReader( xml ) ) );
			dBuilder = dbFactory.newDocumentBuilder();
	
			doc.getDocumentElement().normalize();
	
			System.out.println( "Root element :" + doc.getDocumentElement().getNodeName() );
	
			NodeList nList = doc.getElementsByTagName( "Operation" );
			System.out.println( "Operacao: " + nList.item( 0 ).getTextContent() );
		}
		catch ( ParserConfigurationException pce)
		{
			pce.printStackTrace();
		}
		catch ( SAXException se )
		{
			se.printStackTrace();
		}
		catch ( IOException ie )
		{
			ie.printStackTrace();
		}
	}

	public static XMLBuilder fromMessage( String xml ) {
		return new XMLBuilder( xml );
	}
}
