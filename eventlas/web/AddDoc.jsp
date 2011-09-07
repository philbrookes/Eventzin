<%-- 
    Document   : AddDoc
    Created on : Dec 18, 2010, 8:30:48 PM
    Author     : craigbrookes
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <%@include  file="header.jsp"  %>
    <script src="${pageContext.request.contextPath}/js/paramadder.js" type="text/javascript" ></script>
    <body>
        <div id="central-column">
            <div id="content-area">
                <form class="centeredform" action="/api/documentation/process" method="POST">
                    <table width="350" class="addtable">
                        ${error}
                        <tr>
                            <td>Method Name:</td>
                            <td><input class="generalinput" name="method" type="text" placeholder="method"/></td>
                        </tr>
                        <tr class="example">
                            <td>Example</td>
                            <td><textarea cols="12" rows="20" name="example" class="generalinput" placeholder="example" ></textarea></td>
                        </tr>
                        <tr class="notes">
                            <td>Notes</td>
                            <td><textarea cols="12" rows="20" name="notes" class="generalinput" placeholder="notes" ></textarea></td>
                        </tr>
                        
                       
                    </table>
                    <table width="350">
                        <tr>
                            <td>Add Param</td>
                            <td><a href="#" class="addparam" >add param</a></td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td><input type="submit" value="add" /></td>
                        </tr>
                            
                     </table>

                    
                    

                </form>
            </div>
        </div>
    </body>
</html>
