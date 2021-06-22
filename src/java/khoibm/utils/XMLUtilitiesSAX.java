/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package khoibm.utils;

import java.io.File;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.helpers.DefaultHandler;

public class XMLUtilitiesSAX {

    public static void parseFileWithSAX(String filePath, DefaultHandler handler) throws Exception {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser parser = spf.newSAXParser();
        File f = new File(filePath);
        parser.parse(f, handler);
    }

    public static boolean parseFileToSAX(String filePath, DefaultHandler handler) throws Exception {
        if (handler == null) {
            return false;
        }
        SAXParserFactory factory = SAXParserFactory.newDefaultInstance();
        SAXParser sax = factory.newSAXParser();

        sax.parse(filePath, handler);
        return true;
    }

}
