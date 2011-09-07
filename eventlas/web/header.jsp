<%-- 
    Document   : header
    Created on : Dec 5, 2010, 7:26:18 PM
    Author     : craigbrookes
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="sf" uri="/WEB-INF/tlds/stringFunctions" %>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${requestScope["meta"]["title"]}</title>
         <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/reset.css" />
          <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css" />
          <script src="http://code.jquery.com/jquery-1.4.4.min.js" type="text/javascript"></script>
    </head>


