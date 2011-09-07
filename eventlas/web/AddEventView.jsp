<%-- 
    Document   : AddEventView
    Created on : Feb 18, 2011, 10:59:03 PM
    Author     : craigbrookes
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

                 <div class="siteform left" >
                     <table border="0" width="450" cellspacing="2">
                         <form action="/content/newEvent" method="POST" enctype="" id="addeventform">
                         <thead>
                             <tr>
                                 <th></th>
                                 <th></th>
                             </tr>
                         </thead>
                         <tbody>
                             <tr>
                                 <td>Title</td>
                                 <td><input class="formtext" type="text" name="title" placeholder="Event Title" /></td>
                             </tr>
                             <tr>
                                 <td>Event Date</td>
                                 <td><input class="formtext" type="text" name="date" placeholder="Event Date" /></td>
                             </tr>
                             <tr>
                                 <td>Event OverView</td>
                                 <td><textarea  name="overview" rows="19" cols="40" placeholder="Overview"></textarea></td>
                             </tr>
                             <tr>
                                 <td>Event Location</td>
                                 <td><select name="venue">
                                         <option value="">pick a venue</option>
                                         <option value ="5">Geofs</option>
                                         <option value="6">Kazbar</option>
                                     </select></td>
                             </tr>
                             <tr>
                                 <td>Visibility</td>
                                 <td><label>Private</label><input type="radio" name="visibility" value="private" />
                                     <label>Public</label><input type="radio" name="visibility" value="public" checked="checked" /></td>
                             </tr>
                             <tr>
                                 <td></td>
                                 <td><input class="right" type="submit" value="Submit" name="submit" style="margin-right: 6px;" /></td>
                             </tr>
                         </tbody>
                         </form>
                     </table>


                 </div>
                 <%@include file="/partials/footer.jsp" %>
            </div>
                 
        </div>
    </body>
</html>
