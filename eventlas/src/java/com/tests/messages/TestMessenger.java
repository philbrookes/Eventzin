/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tests.messages;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class TestMessenger{



    public static void main(String[] args){

      TestMessenger mess = new TestMessenger();
      mess.test();
    }
    
    
    public void test(){
        Properties props = new Properties();
      props.setProperty("mail.transport.protocol", "smtp");
      props.setProperty("mail.host", "95.154.246.7");
      props.setProperty("mail.user", "messages@eventlas.com");
      props.setProperty("mail.password", "asdfg135");
       props.put("mail.smtp.auth", "true");

       Authenticator auth = new Authenticate();
        Session mailSession = Session.getDefaultInstance(props,auth);
        try{
            
        Transport transport = mailSession.getTransport();
        MimeMessage message = new MimeMessage(mailSession);
        mailSession.setDebug(true);

        message.setSubject("test email");
        message.setContent("this is my first java mail", "text/html; charset=ISO-8859-1");
        message.addRecipient(Message.RecipientType.TO, new InternetAddress("craig@mywebdesign.ie"));
        message.setFrom(new InternetAddress("messages@eventlas.com"));
        transport.connect();
      transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
      transport.close();


        }catch(NoSuchProviderException e){
         e.printStackTrace();
        }
        catch(MessagingException e){
            e.printStackTrace();
        }
    }



    private class Authenticate extends Authenticator{
        @Override
         public PasswordAuthentication getPasswordAuthentication() {
           String username = "messages@eventlas.com";
           String password = "yg!-bD5]+62Q";
           return new PasswordAuthentication(username, password);
        }

    }

}
