<%-- 
    Document   : navigation
    Created on : Feb 17, 2011, 5:00:26 PM
    Author     : craigbrookes
--%>

<div id="navigationbar" class="left">
    <ul id="navigationlist">
        <c:forEach var="navitem"items="${requestScope['navitems']}">
                    <li><a href="${navitem["href"]}">${navitem["title"]}</a></li>

       </c:forEach>
      
    </ul>
</div>

