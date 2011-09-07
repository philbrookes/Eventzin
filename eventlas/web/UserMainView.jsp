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
            <%@include file="/partials/logo.jsp" %>
            <div id="content-area">
                <%@include file="/partials/navigation.jsp" %>
                <div class="listcolumn left">
                     <c:forEach var="eventitem" items="${requestScope['eventitems']}">
                         <div class="left event">
                             <h2>${eventitem["event"]["title"]}</h2>
                             <p>
                                 ${eventitem["event"]["summary"]}<br/>

                                 <a href="/${eventitem["user"]["username"]}/hosting/${sf:stripSpaces(eventitem['event']['title'])}" >View Event</a>
                             </p>
                         </div>

                    </c:forEach>
                </div>
                <div class="attending left textcentered">
                    <h2 class="textcentered">${requestScope["user"]["username"]} is a Fan of</h2>
                </div>
                <%@include file="/partials/footer.jsp" %>
            </div>
        </div>
    </body>
</html>
