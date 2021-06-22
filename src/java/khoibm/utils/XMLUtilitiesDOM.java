package khoibm.utils;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class XMLUtilitiesDOM {

    public static Document parseFileToDOM(String filePath) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        File f = new File(filePath);
        Document doc = db.parse(f);
        return doc;
    }

    public static XPath createXPath() {
        XPathFactory xpf = XPathFactory.newInstance();
        XPath xp = xpf.newXPath();

        return xp;
    }

    public static boolean transformDomToStreamResult(Node node, String filePath)
            throws Exception {
        Source source = new DOMSource(node);
        File file = new File(filePath);

        Result result = new StreamResult(file);
        TransformerFactory tf = TransformerFactory.newInstance();

        Transformer trans = tf.newTransformer();
        trans.transform(source, result);

        return true;

    }

}
