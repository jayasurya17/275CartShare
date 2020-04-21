package com.cartshare.utils;

import com.sun.mail.smtp.SMTPTransport;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailController {

    public boolean send(String toEmail, String subject, String message){
        Properties prop = System.getProperties();
        prop.put("mail.smtp.host", "smtp.gmail.com"); //optional, defined in SMTPTransport
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.port", "587"); // default port 25
        prop.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(prop, null);
        Message msg = new MimeMessage(session);

        String myEmail = "cmpe275.cartshare@gmail.com";
        String myPass = "cartsharePass@123";

        try {
		
			// from
            msg.setFrom(new InternetAddress(myEmail));

			// to 
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));

			// subject
            msg.setSubject(subject);
			
			// content 
            msg.setText(message);

			// Get SMTPTransport
            SMTPTransport t = (SMTPTransport) session.getTransport("smtp");
			
			// connect
            t.connect("smtp.gmail.com", myEmail, myPass);
			
			// send
            t.sendMessage(msg, msg.getAllRecipients());

            System.out.println("Response: " + t.getLastServerResponse());

            t.close();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}