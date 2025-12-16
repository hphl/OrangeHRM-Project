package org.orangehrm.core;

/*****************************************************************/
/* Copyright 2009 avajava.com                                    */
/* This code may be freely used and distributed in any project.  */
/* However, please do not remove this credit if you publish this */
/* code in paper or electronic form, such as on a web site.      */
/*****************************************************************/

/*
 * Email class
 * 
 * Email(String mailMessage, List<String> fileNames)
 * 		Constructor to set message and attachments
 * 
 * sendEmail()
 * 		Put content (message + attachments) in e-mail
 * 		Send e-mail
 * 		No return
 */



import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class TestEmail
{
    List<String> mailTo;
    String mailFrom;
    String mailSubject;

    String mailMessage;
    List<String> fileNames;

    public TestEmail(List<String> emailAddresses, String mailMessage,
            List<String> fileNames)
    {
        mailTo = emailAddresses;
        this.mailMessage = mailMessage;
        this.fileNames = fileNames;

        mailFrom = "Test@gmail.com";
        mailSubject = "QA - Automation Results";
    }

    public void sendEmail()
    {
        try
        {
            Properties properties = new Properties();
            properties.put("mail.smtp.host", "smtp.freesmtpservers.com");
            Session emailSession = Session.getDefaultInstance(properties);

            // Email content
            MimeMessage email = new MimeMessage(emailSession);
            for (int i = 0; i < mailTo.size(); i++) 
            {
            	System.out.println("Email:  "+mailTo.get(i));
                email.addRecipient(Message.RecipientType.TO,
                        new InternetAddress(mailTo.get(i)));
            email.setFrom(new InternetAddress(mailFrom));
            email.setSubject(mailSubject);
            }

            Multipart mp = new MimeMultipart();

            // 1st part of email : Message body
            MimeBodyPart messageBody = new MimeBodyPart();
            messageBody.setText(mailMessage);
            mp.addBodyPart(messageBody);

            // Other parts of email : Attachment(s)
            for (int i = 0; i < fileNames.size(); i++)
            {
                // Create another message part
                MimeBodyPart attachment = new MimeBodyPart();

                // Attach the file to the message
                FileDataSource fds = new FileDataSource(fileNames.get(i));
                attachment.setDataHandler(new DataHandler(fds));
                attachment.setFileName(fds.getName());
                mp.addBodyPart(attachment);
            }

            // Set email with multiple parts (message and attachment(s))
            email.setContent(mp);

            // Print email log in console
            // emailSession.setDebug(true);
            System.out.println("Sending an email with results...");

            // Send email
            Transport.send(email);

            System.out.println("Email with results sent!");
        }
        catch (AddressException e)
        {
            e.printStackTrace();
        }
        catch (MessagingException e)
        {
            e.printStackTrace();
        }
    }
}
