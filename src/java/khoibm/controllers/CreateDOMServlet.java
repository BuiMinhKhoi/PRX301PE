package khoibm.controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import khoibm.utils.XMLUtilitiesDOM;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

@WebServlet(name = "CreateDOMServlet", urlPatterns = {"/CreateDOMServlet"})
public class CreateDOMServlet extends HttpServlet {

    private static final String XML_FILE = "/WEB-INF/studentAccounts.xml";
    private static final String SUCCESS_PAGE = "show.jsp";
    private static final String ERROR_PAGE = "index.html";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = ERROR_PAGE;

        try {

            String realPath = getServletContext().getRealPath("/");
            String filePath = realPath + XML_FILE;

            Document doc = XMLUtilitiesDOM.parseFileToDOM(filePath);

            String id = "SE04";
            String aClass = "SE1410";
            String firstName = "Jame";
            String middleName = "Child";
            String lastName = "Cullen";

            String address = "123";
            String status = "studying";
            String gender = "1";
            String password = "123";

            if (doc != null) {
                Element student = doc.createElement("student");

                student.setAttribute("id", id);
                student.setAttribute("class", aClass);

                Element lastNameEl = doc.createElement("lastname");
                lastNameEl.setTextContent(lastName);

                Element middleNameEl = doc.createElement("middlename");
                middleNameEl.setTextContent(middleName);

                Element firstNameEl = doc.createElement("firstname");
                firstNameEl.setTextContent(firstName);

                Element genderEl = doc.createElement("gender");
                genderEl.setTextContent(gender);

                Element addressEl = doc.createElement("address");
                addressEl.setTextContent(address);

                Element statusEl = doc.createElement("status");
                statusEl.setTextContent(status);

                Element passwordEl = doc.createElement("password");
                passwordEl.setTextContent(password);

                student.appendChild(lastNameEl);
                student.appendChild(middleNameEl);
                student.appendChild(firstNameEl);

                student.appendChild(genderEl);
                student.appendChild(passwordEl);

                student.appendChild(addressEl);
                student.appendChild(statusEl);

                NodeList list = doc.getElementsByTagName("students");
                if (list != null) {
                    if (list.getLength() > 0) {

                        list.item(0).appendChild(student);
                        boolean insertStatus = XMLUtilitiesDOM.transformDomToStreamResult(doc, filePath);

                        if (insertStatus) {
                            url = SUCCESS_PAGE;
                        }

                    }
                }

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
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
