package khoibm.controllers;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import khoibm.utils.XMLUtilitiesStAX;

@WebServlet(name = "LoginStAXServlet", urlPatterns = {"/LoginStAXServlet"})
public class LoginStAXServlet extends HttpServlet {

    private final String xmlFile = "WEB-INF/studentAccounts.xml";
    private final String invalidPage = "invalid.html";
    private final String searchPage = "search.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        InputStream is = null;
        XMLStreamReader reader = null;
        String url = invalidPage;
        try {
            String username = request.getParameter("txtUsername");
            String password = request.getParameter("txtPassword");

            String realPath = this.getServletContext().getRealPath("/");
            System.out.println("Real Path is: " + realPath);
            String filePath = realPath + xmlFile;
            System.out.println("File Path is: " + filePath);

            is = new FileInputStream(filePath);

            reader = XMLUtilitiesStAX.parseFileToStAXCursor(is);

            String fullName = "";

            while (reader.hasNext()) {
                int cursor = reader.next();
                if (cursor == XMLStreamConstants.START_ELEMENT) {
                    String tagName = reader.getLocalName();
                    System.out.println("Tag Name is: " + tagName);
                    if (tagName.equals("student")) {
                        String id = XMLUtilitiesStAX.getNodeStaXValue(reader, "student", "", "id");
                        if (id.equals(username)) {
                            String lastname = XMLUtilitiesStAX.getTexStAXContex(reader, "lastname");
                            fullName = lastname.trim();
                            String middleName = XMLUtilitiesStAX.getTexStAXContex(reader, "middlename");
                            fullName = fullName + " " + middleName.trim();
                            String firstName = XMLUtilitiesStAX.getTexStAXContex(reader, "firstname");
                            fullName = fullName + " " + firstName.trim();
                            
                            String pass = XMLUtilitiesStAX.getTexStAXContex(reader, "password");
                            if(pass.trim().equals(password)){
                                String status = XMLUtilitiesStAX.getTexStAXContex(reader, "status");
                                if(!status.trim().equals("dropout")){
                                    url = searchPage;
                                    HttpSession session = request.getSession();
                                    session.setAttribute("FULLNAME", fullName);
                                    session.setAttribute("USER", username);
                                    break;
                                }
                            }
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(reader != null){
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if(is != null){
                is.close();
            }
            RequestDispatcher rd = request.getRequestDispatcher(url);
            rd.forward(request, response);
            out.close();
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
