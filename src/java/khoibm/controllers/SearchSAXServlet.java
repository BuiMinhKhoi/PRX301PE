package khoibm.controllers;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import khoibm.dtos.StudentDTO;
import khoibm.handlers.studentStatusHandler;
import khoibm.utils.XMLUtilitiesSAX;

@WebServlet(name = "SearchSAXServlet", urlPatterns = {"/SearchSAXServlet"})
public class SearchSAXServlet extends HttpServlet {

    private static final String XML_FILE = "/WEB-INF/studentAccounts.xml";
    private final String showResultPage = "search.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            String search = request.getParameter("txtStatus");
            String realPath = getServletContext().getRealPath("/");
            String filePath = realPath + XML_FILE;

            studentStatusHandler handler = new studentStatusHandler(search);
            boolean result = XMLUtilitiesSAX.parseFileToSAX(filePath, handler);
            if (result) {
                List<StudentDTO> list = handler.getList();
                request.setAttribute("SEARCHRESULT", list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            request.getRequestDispatcher(showResultPage).forward(request, response);
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
