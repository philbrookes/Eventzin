<%-- 
    Document   : ApiDocs
    Created on : Dec 18, 2010, 3:10:55 PM
    Author     : craigbrookes
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
     <%@include  file="header.jsp"  %>
    <body>
        <c:choose>
            <c:when test="${not empty requestScope['methoddef']['name']}">

                 <h2>${requestScope["methoddef"]["name"]}</h2>
                 
                 <c:choose>
                     <c:when test="${requestScope['methoddef']['auth'] == 0}">
                        <h3>Authorization</h3>
                        <p>
                            None
                        </p>
                     </c:when>
                        <c:otherwise>

                            <h3>Authorization</h3>
                            <p>
                            ApiKey
                            </p>
                        </c:otherwise>
                 </c:choose>


                <h3>Parameters</h3>
                <c:if test="${empty requestScope['params']}">
                    None
                </c:if>
                <c:forEach var="parm"items="${requestScope.params}">
                    <c:if test="${parm.required == 1}">
                        ${parm.name}&nbsp; ${parm.type} <span style="color:red;"> Required</span><br/>
                    </c:if>
                    <c:if test="${parm.required == 0}">
                        ${parm.name}&nbsp; ${parm.type}<span style="color: green"> Optional</span><br/>
                    </c:if>
                   
                </c:forEach>

                    <h3>Notes</h3>
                    <p>
                    ${requestScope["methoddef"]["notes"]}
                    </p>


                    <h3>Example Call</h3>
                    <p>
                        ${requestScope["methoddef"]["example"]}
                        </p>

                        <p>
                            <a href="http://eventlas.com/api/documentation/">Back To Index</a>
                        </p>

            </c:when>
            <c:otherwise>
                <h2>Api Methods</h2>
                <p>
                    Click link to view more detail
                </p><br/>
                <c:forEach var="method" items="${requestScope['methods']}">
                    <a href="${pageContext.request.contextPath}${method.name}" >${method.name}</a><br/><hr/>
                </c:forEach>
            </c:otherwise>

        </c:choose>
               
            
            
        

    </body>
</html>
