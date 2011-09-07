/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.messengers;

import api.helpers.FileHelper;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author craigbrookes
 */
public abstract class Messenger implements MailMessenger {

    protected Properties props;
    private Session mailSession;
    private String from;
    private MimeMessage message;
    public static final String FROM_NO_REPLY = "noreply@eventzin.com";
    public static final String FROM_MESSAGES = "messages@eventzin.com";
    public static final String FROM_INVITES = "invites@eventzin.com";
    private static final String pass = "yg!-bD5]+62Q";

    public Messenger() {
        this(FROM_NO_REPLY);

    }

    public Messenger(String from) {

        this.from = from;
        props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", "95.154.246.7");
        props.setProperty("mail.user", from);
        props.setProperty("mail.password", "yg!-bD5]+62Q");
        props.put("mail.smtp.auth", "true");
        Authenticator auth = new SMTPAuth();
        mailSession = Session.getDefaultInstance(props, auth);
        message = new MimeMessage(mailSession);

        mailSession.setDebug(true);
    }

    protected Session getMailSession() {
        return mailSession;
    }

    protected MimeMessage getMessage() {

        return message;
    }

    private class SMTPAuth extends Authenticator {

        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            String username = from;
            String password = pass;
            return new PasswordAuthentication(username, password);
        }
    }

    @Override
    public boolean send() throws MessagingException {
        message.setFrom(new InternetAddress(from));
        Transport transport = getMailSession().getTransport();
        transport.connect();
        transport.sendMessage(
                getMessage(),
                getMessage().getRecipients(Message.RecipientType.TO));
        transport.close();

        return true;
    }

    @Override
    public void setTo(String to) throws MessagingException {

        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

    }

    public void setMultipleRecipients(ArrayList<String> recipients) throws MessagingException {
        if (recipients.isEmpty()) {
            throw new MessagingException("no recipients found");
        }
        InternetAddress[] addresses = new InternetAddress[recipients.size()];
        for (int i = 0; i < recipients.size(); i++) {
            addresses[i] = new InternetAddress(recipients.get(i));
        }
        message.setRecipients(Message.RecipientType.TO, addresses);
    }

    @Override
    public void setHtmlContent(String content) throws MessagingException {
        message.setContent(content, "text/html; charset=ISO-8859-1");
    }

    @Override
    public void setSubject(String subject) throws MessagingException {
        message.setSubject(subject);
    }

    public void setContentFromTemplate(String template) throws MessagingException {
        StringBuilder text = new StringBuilder();
        String NL = System.getProperty("line.separator");
        try {
            Scanner scanner = new Scanner(new FileInputStream(FileHelper.getRootDir() + "emailTemplates/" + template + ".template"), "UTF-8");

            try {
                while (scanner.hasNextLine()) {
                    text.append(scanner.nextLine() + NL);
                }
            } finally {
                scanner.close();
            }
        } catch (FileNotFoundException e) {
            throw new MessagingException(e.toString());
        }

        setHtmlContent(text.toString());

    }

    public void replaceTagsInContent(HashMap<String, String> tagsandreplacements) throws MessagingException {

        String mes = "";
        try {
            mes = (String) message.getContent();
        } catch (IOException e) {
            throw new MessagingException("content has not been set " + e.getMessage());
        }


        Set<String> keys = tagsandreplacements.keySet();

        Iterator<String> keyIt = keys.iterator();
        String finalMess = "";
        while (keyIt.hasNext()) {
            String cKey = keyIt.next();
            mes = mes.replaceAll(cKey, tagsandreplacements.get(cKey));
        }

        setHtmlContent(mes);
    }
}
