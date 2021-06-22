package khoibm.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import khoibm.dtos.StudentDTO;
import khoibm.utils.XMLUtilitiesDOM;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@WebServlet(name = "SearchDOMServlet", urlPatterns = {"/SearchDOMServlet"})
public class SearchDOMServlet extends HttpServlet {

    private static final String XML_FILE = "/WEB-INF/studentAccounts.xml";
    private final String showResultPage = "search.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        try {

            String realPath = getServletContext().getRealPath("/");
            String filePath = realPath + XML_FILE;

            String txtAddress = request.getParameter("txtStatus");
            Document doc = XMLUtilitiesDOM.parseFileToDOM(filePath);

            if (doc != null) {
                XPath xPath = XMLUtilitiesDOM.createXPath();

                String exp = "//student[contains(address,'" + txtAddress + "')]";
                NodeList studentList = (NodeList) xPath.evaluate(exp, doc, XPathConstants.NODESET);

                if (studentList != null) {
                    List<StudentDTO> theStudentList = new ArrayList();

                    for (int i = 0; i < studentList.getLength(); i++) {
                        Node node = studentList.item(i);

                        String id = node.getAttributes().getNamedItem("id").getNodeValue();
                        String aClass = node.getAttributes().getNamedItem("class").getNodeValue();

                        StudentDTO studentDTO = new StudentDTO();
                        studentDTO.setId(id);
                        studentDTO.setsClass(aClass);

                        NodeList children = node.getChildNodes();

                        for (int j = 0; j < children.getLength(); j++) {
                            Node tmp = children.item(j);

                            if (tmp.getNodeName().equals("firstname")) {
                                studentDTO.setFirstname(tmp.getTextContent().toString());
                            } else if (tmp.getNodeName().equals("middlename")) {
                                studentDTO.setMiddlename(tmp.getTextContent().toString());
                            } else if (tmp.getNodeName().equals("lastname")) {
                                studentDTO.setLastname(tmp.getTextContent().toString());
                            } else if (tmp.getNodeName().equals("status")) {
                                studentDTO.setStatus(tmp.getTextContent().toString());
                            } else if (tmp.getNodeName().equals("address")) {
                                studentDTO.setAddress(tmp.getTextContent().toString());
                            }
                        } // end for j 

                        theStudentList.add(studentDTO);
                    } // end for i 

                    request.setAttribute("SEARCHRESULT", theStudentList);
                }

            }
        } catch (Exception e) {
            System.out.println("Search Controller: " + e.getMessage());

        } finally {
            RequestDispatcher rd = request.getRequestDispatcher(showResultPage);
            rd.forward(request, response);
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
