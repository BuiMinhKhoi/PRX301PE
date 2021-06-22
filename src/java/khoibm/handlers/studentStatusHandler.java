package khoibm.handlers;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import khoibm.dtos.StudentDTO;

public class studentStatusHandler extends DefaultHandler {

    private String search;
    private String tagName;
    private List<StudentDTO> list;
    private StudentDTO student;
    boolean found;

    public List<StudentDTO> getList() {
        return list;
    }

    public studentStatusHandler() {
        this.search = "";
        this.tagName = "";
        this.found = false;
    }

    public studentStatusHandler(String search) {
        this.search = search;
        this.tagName = "";
        this.found = false;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        this.tagName = qName;
        if (qName.equals("student")) {
            String id = attributes.getValue("id");
            this.student = new StudentDTO();
            student.setId(id);
            String aClass = attributes.getValue("class");
            student.setsClass(aClass);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("student")) {
            if (this.found) {
                if (this.list == null) {
                    this.list = new ArrayList<>();
                }
                this.list.add(student);
                this.student = null;
            }
            this.found = false;
        }
        this.tagName = "";
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String str = new String(ch, start, length);
        if (this.tagName.equals("lastname")) {
            this.student.setLastname(str.trim());
        } else if (this.tagName.equals("middlename")) {
            this.student.setMiddlename(str.trim());
        } else if (this.tagName.equals("firstname")) {
            this.student.setFirstname(str.trim());
        } else if (this.tagName.equals("gender")) {
            if (str.trim().equals("1")) {
                this.student.setSex(1);
            } else if (str.trim().equals("0")) {
                this.student.setSex(0);
            }
        } else if (this.tagName.equals("address")) {
            this.student.setAddress(str.trim());
        } else if (this.tagName.equals("status")) {
            if (str.trim().equals(this.search)) {
                this.found = true;
                this.student.setStatus(search);
            }
        }

    }
}
