<%-- 
    Document   : index
    Created on : 16-Oct-2010, 12:42:13
    Author     : philip
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Eventzin - social and location based Events discovery and promotion </title>
        <meta name="description" content="Discover, share and manage events. Gather Fans and get the word out about your event. Find the best events happening and see who is attending." />
        <style>

            .centeredform{
                position:  relative;
                margin-left: 250px;


            }

            .generalForm{

            }

            .generalinput{
                width: 250px;
                height: 30px;
                margin: 3px;
                font-size: 14px;
                font-weight: bold;
                padding-top: 2px;
            }

            .left{
                float: left;
            }

            .right{
                float: right;
            }

            #central-column{

                width: 1200px;
                padding-top: 100px;
                padding-bottom: 1px;
                margin: auto;
                min-height: 1000px;
                height: auto;
            }


            #content-area{
                position: relative;
                margin: auto;
                width:911px;
            }
            .central-para{
                background-color:#b4e2ec;
                width:600px;
                margin:auto;
                border:1px solid #6a888e;
                padding:10px;
                font-size:18px;
                font-family arial, monaco;
            }
            form{
                background-color:#cbcdcd;
            }
        </style>
    </head>
    <body style="background-image: url('http://eventzin.com/images/silhouette-city.jpg'); background-repeat: repeat-x; background-color: #000000;">
        <div id="central-column">
            <div id="content-area">
                <div class="central-para">
                    <p>
            	        Eventzin is a location based, social, event discovery and sharing tool. It enables you find events that are happening
                        in your area, see which events your friends are attending and also invite your friends to attend events.
                        If you really enjoy an event, you can then become a fan of the host and if they are setting up any
                        other events, you will be notified.
                    </p>
                    <p>
                        You can build the excitement around an upcoming event by asking the event host questions and adding your comments
                        to the event's notice board.
                    </p>
                    <p>
                        You can also add your own events to Eventzin, send out invites to your fans and friends  and see who will and wont be attending.
                        Use Eventzin to gather more fans by hosting great events, and find more people to attend your events.
                    </p>
                    <p>
                        Eventzin is not just for public events though. You can use it to set up private events as well.
                        These events are only viewable by those that you invite to the event.
                    </p>
                    <p>
                        There will be both an Iphone and Android app released this year.
                    </p>

                    The Alpha release is due to happen in Mid April 2011. Sign up to be notified about the alpha release.<br/>

                    <form action="/signup/alpha" method=POST >
                        <table>
                            <span style="color:red;">${sessionScope["error"]}</span>
                            <tr><td>Email:</td></tr>
                            <tr><td><input type="text" name="email" class="generalinput" placeholder="Email Address" /></td></tr>
                            <tr><td></td></tr>
                            <tr><td>	<input type="submit" value="register" /> </td></tr>
                        </table>
                    </form>

                            <p>Follow us on Twitter <a href="http://twitter.com/eventzin">@Eventzin</a></p>

                </div>

            </div>
        </div>
    </body>
</html>
