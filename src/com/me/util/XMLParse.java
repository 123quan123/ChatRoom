package com.me.util;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public abstract class XMLParse {
	private static DocumentBuilder db;
	
	static {
		try {
			db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public XMLParse() {
	}
	
	public static Document loadXml(String xmlPath) throws SAXException, IOException {
		InputStream is = Class.class.getResourceAsStream(xmlPath);

		return loadXml(xmlPath, is);
	}
	
	public static Document loadXml(String xmlPath, InputStream is) throws SAXException, IOException {
		is = Class.class.getResourceAsStream(xmlPath);
		Document doc = db.parse(is);
		
		return doc;
	}
	
	public abstract void parseElement(Element element, int index);
	
	public void parseTag(Document document, String tagName) {
		NodeList nodeList = document.getElementsByTagName(tagName);
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element node = (Element) nodeList.item(i);
			//由用户决定
			parseElement(node, i);
		}
	}
	
	public void parseTag(Element element, String tagName) {
		NodeList nodeList = element.getElementsByTagName(tagName);
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element node = (Element) nodeList.item(i);
			//由用户决定
			parseElement(node, i);
		}
	}
	
}
