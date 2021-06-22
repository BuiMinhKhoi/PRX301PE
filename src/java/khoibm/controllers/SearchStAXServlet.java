package khoibm.controllers;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLEventReader;
import khoibm.dtos.StudentDTO;
import khoibm.utils.XMLUtilitiesStAX;

@WebServlet(name = "SearchStAXServlet", urlPatterns = {"/SearchStAXServlet"})
public class SearchStAXServlet extends HttpServlet {

    private final String xmlFile = "WEB-INF/studentAccounts.xml";
    private final String showResultPage = "search.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();
        InputStream is = null;
        XMLEventReader reader = null;
        List<StudentDTO> students = null;

        try {
            String searchValue = request.getParameter("txtSearch");

            String realPath = this.getServletContext().getRealPath("/");
            String filePath = realPath + xmlFile;

            is = new FileInputStream(filePath);

            reader = XMLUtilitiesStAX.parseFileToStAXIterator(is);

            while (reader.hasNext()) {
                reader.nextEvent();
                String id = XMLUtilitiesStAX.getNodeStaXValue(reader, "student", "", "id");
                if (id != null) {
                    String sClass = XMLUtilitiesStAX.getNodeStaXValue(reader, "student", "", "class");
                    String lastname = XMLUtilitiesStAX.getTexStAXContext(reader, "lastname");
                    String middle = XMLUtilitiesStAX.getTexStAXContext(reader, "middlename");
                    String first = XMLUtilitiesStAX.getTexStAXContext(reader, "firstname");
                    int sex = Integer.parseInt(XMLUtilitiesStAX.getTexStAXContext(reader, "sex"));
                    String password = XMLUtilitiesStAX.getTexStAXContext(reader, "password");
                    String address = XMLUtilitiesStAX.getTexStAXContext(reader, "address");
                    String status = XMLUtilitiesStAX.getTexStAXContext(reader, "status");
                    if (status.trim().equals(searchValue)) {
                        StudentDTO dto = new StudentDTO(id, sClass, lastname, middle, lastname, password, address, status, sex);
                        if (students == null) {
                            students = new ArrayList<StudentDTO>();
                        }
                        students.add(dto);
                    }
                }
            }
            request.setAttribute("SEARCHRESULT", students);

            RequestDispatcher rd = request.getRequestDispatcher(showResultPage);
            rd.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
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
