<%-- 
    Document   : search
    Created on : Jun 22, 2021, 6:18:35 PM
    Author     : bmk
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Search</title>
    </head>
    <body>
        <font color="red">Welcome, ${sessionScope.FULLNAME}</font>
        <h1>Search Page</h1>
        <form action="ProcessServlet">
            Status <input type="text" name="txtStatus" value="${param.txtStatus}"/><br/>
            <input type="submit" value="Search" name="btAction"/>
        </form>
        <br/>
        <c:set var="status" value="${param.txtStatus}"/>
        <c:if test="${not empty status}">
            <c:set var="result" value="${requestScope.SEARCHRESULT}"/>
            <c:if test="${not empty result}">
                <table border="1">
                    <thead>
                        <tr>
                            <th>No.</th>
                            <th>ID</th>
                            <th>Class</th>
                            <th>Full name</th>
                            <th>Sex</th>
                            <th>Address</th>
                            <th>Delete</th>
                            <th>Update</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="dto" items="${result}" varStatus="counter">
                        <form action="ProcessServlet">
                            <tr>
                                <td>
                                    ${counter.count}
                                    .</td>
                                <td>
                                    ${dto.id}
                                    <input type="hidden" name="txtId" value="${dto.id}"/>
                                </td>
                                <td>
                                    <input type="text" name="txtClass" value="${dto.sClass}"/>
                                </td>
                                <td>
                                    ${dto.lastname} ${dto.middlename} ${dto.lastname}
                                </td>
                                <td>
                                    ${dto.sex}
                                </td>
                                <td>
                                    <input type="text" name="txtAddress" value="${dto.address}"/>
                                </td>
                                <td>
                                    <c:url var="delLink" value="ProcessServlet">
                                        <c:param name="btnAction" value="del"/>
                                        <c:param name="id" value="${dto.id}"/>
                                        <c:param name="lastSearchValue" value="${param.txtStatus}"/>
                                    </c:url>
                                    <a href="${delLink}">Delete</a>
                                </td>
                                <td>
                                    <input type="submit" value="Update" name="btnAction"/>
                                    <input type="hidden" name="lastSearchValue" value="${param.txtStatus}"/>
                                </td>
                            </tr>
                        </form>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>
        <c:if test="${empty result}">
            <h2>No record is matched!!!</h2>
        </c:if>
    </c:if>
</body>
</html>
