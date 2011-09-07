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

                         <div class="left fullevent">
                             <h2>${requestScope["event"]["title"]}</h2>
                             <p>
                                 ${requestScope["event"]["summary"]}
                             </p>
                         </div>

                         <div class="eventmap">
                            <h1>Event Map</h1>
                        </div>
                </div>
                 
                <div class="attending left">
                    <h2>Whose Attending</h2>
                </div>
                
            </div>
        </div>
    </body>
</html>
