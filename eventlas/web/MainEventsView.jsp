<%-- 
    Document   : MainEventsView
    Created on : 18-Feb-2011, 19:56:34
    Author     : craig
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">


<html>
    <%@include file="/header.jsp" %>
    <body>
        <div id="central-column">
            <div id="content-area">
                <%@include file="/partials/navigation.jsp" %>
                <div class="listcolumn left">
                     <c:forEach var="eventitem" items="${requestScope['eventitems']}">
                         <div class="left event">
                             <h2>${eventitem["title"]}</h2>
                             <p>
                                 ${eventitem["summary"]}
                             </p>
                         </div>

                    </c:forEach>
                </div>
                <div class="attending left">
                    <h2>Whose Attending</h2>
                </div>
            </div>
        </div>
    </body>
</html>
