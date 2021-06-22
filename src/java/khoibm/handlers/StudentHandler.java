package khoibm.handlers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class StudentHandler extends DefaultHandler {

    private String username, password, tagName, fullName;
    private boolean foundUsername, foundPassword, found;

    public StudentHandler(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public StudentHandler() {
        this.foundUsername = false;
        this.tagName = "";
        this.fullName = "";
        this.foundPassword = false;
        this.found = false;
    }

    public boolean isFound() {
        return found;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (!this.found) {
            this.tagName = qName;
            if (qName.equals("student")) {
                String id = attributes.getValue("id");
                if (username.equals(id)) {
                    this.foundUsername = true;
                }
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        this.tagName = "";

    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (!found) {
            if (this.foundUsername) {
                String str = new String(ch, start, length);

                if (this.tagName.equals("lastname")) {
                    this.fullName = str.trim();
                } else if (this.tagName.equals("middlename")) {
                    this.fullName = this.fullName + " " + str.trim();
                } else if (this.tagName.equals("firstname")) {
                    this.fullName = this.fullName + " " + str.trim();
                } else if (this.tagName.equals("password")) {
                    if (this.password.equals(str.trim())) {
                        this.foundPassword = true;
                    }
                    this.foundUsername = false;
                }
            }

            if (this.foundPassword) {
                if (this.tagName.equals("status")) {
                    String str = new String(ch, start, length);

                    if (!str.trim().equals("dropout")) {
                        this.found = true;
                    }
                    this.foundPassword = false;
                }
            }
        }
    }

}
