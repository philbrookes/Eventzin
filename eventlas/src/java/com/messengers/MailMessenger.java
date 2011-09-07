/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.messengers;

import javax.mail.MessagingException;

/**
 *
 * @author craigbrookes
 */
public interface MailMessenger {
    public void setHtmlContent(String content)throws MessagingException;
    public void setTo(String to)throws MessagingException;
    public void setSubject(String subject)throws MessagingException;
    public boolean send()throws MessagingException;
    
}
