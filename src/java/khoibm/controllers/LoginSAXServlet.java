package khoibm.controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import khoibm.handlers.StudentHandler;
import khoibm.utils.XMLUtilitiesSAX;

@WebServlet(name = "LoginSAXServlet", urlPatterns = {"/LoginSAXServlet"})
public class LoginSAXServlet extends HttpServlet {

   private final String xmlFile="/WEB-INF/studentAccounts.xml";
    private final String invalidPage = "invalid.html";
    private final String searchPage = "search.jsp";
    
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = invalidPage;
        try {
            String username = request.getParameter("txtUsername");
            String password = request.getParameter("txtPassword");
            
            String realPath = this.getServletContext().getRealPath("/");
            String filePath = realPath + xmlFile;
            StudentHandler handler = new StudentHandler(username, password);
            boolean result = XMLUtilitiesSAX.parseFileToSAX(filePath, handler);
            if(result){
                if(handler.isFound()){
                    url = searchPage;
                    HttpSession session = request.getSession();
                    session.setAttribute("USERNAME", username);
                    session.setAttribute("FULLNAME", handler.getFullName());
                }
            }
        } catch (Exception e) {
        } finally{
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
