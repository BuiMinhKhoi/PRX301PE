package khoibm.controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import khoibm.utils.XMLUtilitiesDOM;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

@WebServlet(name = "DeleteDOMServlet", urlPatterns = {"/DeleteDOMServlet"})
public class DeleteDOMServlet extends HttpServlet {

    private static final String XML_FILE = "/WEB-INF/studentAccounts.xml";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String url = "";

        try {
            String id = request.getParameter("id");
            String lastSearchValue = request.getParameter("lastSearchValue");

            String realPath = getServletContext().getRealPath("/");
            String filePath = realPath + XML_FILE;

            Document doc = XMLUtilitiesDOM.parseFileToDOM(filePath);
            if (doc != null) {
                XPath xPath = XMLUtilitiesDOM.createXPath();
                String exp = "//student[@id='" + id + "']";

                Node student = (Node) xPath.evaluate(exp, doc, XPathConstants.NODE);

                if (student != null) {
                    Node parent = student.getParentNode();

                    parent.removeChild(student);
                    boolean result = XMLUtilitiesDOM.transformDomToStreamResult(doc, filePath);

                    if (result) {
                        url = "ProcessServlet?btAction=Search&txtSearch=" + lastSearchValue;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            response.sendRedirect(url);
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
