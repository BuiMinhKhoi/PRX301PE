package khoibm.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import khoibm.dtos.StudentDTO;

public class XMLUtilitiesStAX {

    public static XMLStreamReader parseFileToStAXCursor(InputStream is) throws Exception {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(is);
        return reader;
    }

    public static String getNodeStaXValue(XMLStreamReader reader, String elementName, String namespaceURI, String attrName) throws Exception {
        if (reader != null) {
            while (reader.hasNext()) {
                int cursor = reader.getEventType();
                if (cursor == XMLStreamConstants.START_ELEMENT) {
                    String tagName = reader.getLocalName();
                    if (tagName.equals(elementName)) {
                        String result = reader.getAttributeValue(namespaceURI, attrName);

                        return result;
                    }
                }
                reader.next();
            }
        }
        return null;
    }

    public static String getTexStAXContex(XMLStreamReader reader, String elementName) throws Exception {
        if (reader != null) {
            while (reader.hasNext()) {
                int cursor = reader.getEventType();
                if (cursor == XMLStreamConstants.START_ELEMENT) {
                    String tagName = reader.getLocalName();
                    if (tagName.equals(elementName)) {
                        reader.next();
                        String result = reader.getText();
                        reader.nextTag();
                        return result;
                    }
                }
                reader.next();
            }
        }
        return null;
    }

    public static XMLEventReader parseFileToStAXIterator(InputStream is) throws Exception {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLEventReader reader = factory.createXMLEventReader(is);
        return reader;
    }

    public static String getNodeStaXValue(XMLEventReader reader, String elementName, String nameSpaceURI, String attrName) throws Exception {
        if (reader != null) {
            while (reader.hasNext()) {
                XMLEvent event = reader.peek();
                if (event.isStartElement()) {
                    StartElement start = (StartElement) event;
                    if (start.getName().getLocalPart().equals(elementName)) {
                        Attribute attr = start.getAttributeByName(new QName(nameSpaceURI, attrName));
                        if (attr != null) {
                            String value = attr.getValue();
                            return value;
                        }
                    }
                }
                reader.nextEvent();
            }
        }
        return null;
    }

    public static String getTexStAXContext(XMLEventReader reader, String elementName) throws Exception {
        if (reader != null) {
            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();
                if (event.isStartElement()) {
                    StartElement start = (StartElement) event;
                    if (start.getName().getLocalPart().equals(elementName)) {
                        event = reader.nextEvent();
                        Characters chars = (Characters) event;
                        String value = chars.getData();
                        reader.nextEvent();

                        return value;
                    }
                }

            }
        }
        return "";
    }

    public static void deleteNodeinStAX(String id, String XmlFileName, String realPath) {
        InputStream is = null;
        XMLEventReader reader = null;
        OutputStream os = null;
        XMLEventWriter writer = null;

        try {
            is = new FileInputStream(realPath + XmlFileName);

            reader = parseFileToStAXIterator(is);

            XMLOutputFactory xof = XMLOutputFactory.newInstance();
            os = new FileOutputStream(realPath + XmlFileName + ".new");
            writer = xof.createXMLEventWriter(os);

            boolean delete = false;

            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();
                if (event.getEventType() == XMLStreamConstants.START_ELEMENT && event.asStartElement().getName().getLocalPart().equals("student")) {
                    Attribute attr = event.asStartElement().getAttributeByName(new QName("id"));
                    String tmp = attr.getValue();
                    if (tmp.equals(id)) {
                        delete = true;
                        continue;
                    }
                } else if (event.getEventType() == XMLStreamConstants.END_ELEMENT && event.asEndElement().getName().getLocalPart().equals("student")) {
                    if (delete) {
                        delete = false;
                        continue;
                    }
                } else if (delete) {
                    continue;
                }
                writer.add(event);
            }
            writer.flush();
            writer.close();
            is.close();
            os.close();

            File file = new File(realPath + XmlFileName);
            file.delete();
            file = null;

            file = new File(realPath + XmlFileName + ".new");
            file.renameTo(new File(realPath + XmlFileName));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void updateNodeinStAXUsingJAXB(String id, String sClass, String address, String xmlFileName, String realPath) {
        InputStream is = null;
        XMLEventReader reader = null;
        OutputStream os = null;
        XMLEventWriter writer = null;

        try {
            XMLInputFactory xif = XMLInputFactory.newInstance();
            is = new FileInputStream(realPath + xmlFileName);
            reader = xif.createXMLEventReader(is);

            XMLOutputFactory xof = XMLOutputFactory.newInstance();
            os = new FileOutputStream(realPath + xmlFileName + ".new");
            writer = xof.createXMLEventWriter(os);
            
            JAXBContext jaxb = JAXBContext.newInstance(StudentDTO.class);
            Unmarshaller unmarshaller = jaxb.createUnmarshaller();
            Marshaller marshall = jaxb.createMarshaller();
            marshall.setProperty(Marshaller.JAXB_FRAGMENT, true);

            while (reader.hasNext()) {
                if (reader.peek().isStartElement() && reader.peek().asStartElement().getName().getLocalPart().equals("student")) {
                    StudentDTO dto = (StudentDTO) unmarshaller.unmarshal(reader);

                    if (dto.getId().equals(id)) {
                        dto.setAddress(address);
                        dto.setsClass(sClass);
                    }
                    marshall.marshal(dto, writer);
                } else {
                    writer.add(reader.nextEvent());
                }
            }
            writer.flush();
            writer.close();
            is.close();
            os.close();

            File file = new File(realPath + xmlFileName);
            file.delete();
            file = null;

            file = new File(realPath + xmlFileName + ".new");
            file.renameTo(new File(realPath + xmlFileName));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
