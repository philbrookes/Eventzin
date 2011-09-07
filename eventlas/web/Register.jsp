<%-- 
    Document   : Register
    Created on : Dec 5, 2010, 1:19:23 PM
    Author     : craigbrookes
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <%@include  file="header.jsp"  %>
    <body>
        <div id="central-column">
            <div id="content-area">
                <form class="centeredform" action="/register" method="POST">
                    <table width="350">
                        ${error}
                        <tr>
                            <td>Username:</td>
                            <td><input class="generalinput" name="username" type="text" placeholder="username"/></td>
                        </tr>
                        <tr>
                            <td>Password</td>
                            <td><input name="password" type="password" class="generalinput" placeholder="password" /></td>
                        </tr>
                        <tr>
                            <td>Email Address</td>
                            <td><input name="email" type="text" class="generalinput" placeholder="email" /></td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td><input type="submit" value="register" /></td>
                        </tr>
                    </table>

                    <input type="hidden" name="action" value="register" />


                </form>
            </div>
        </div>
    </body>
</html>
