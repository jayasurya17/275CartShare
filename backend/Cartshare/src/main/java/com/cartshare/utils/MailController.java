package com.cartshare.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import javax.mail.internet.MimeMessage;

@Component
public class MailController{

    @Autowired
    public JavaMailSender emailSender;

    public boolean send(String toEmail, String subject, String body){
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        try{
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(body, true);
            emailSender.send(mimeMessage);
            return true;
        } catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean sendHTML(String toEmail, String subject, String body){
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        try{
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(body, true);
            emailSender.send(mimeMessage);
            return true;
        } catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    
    // MimeMessage msg = emailSender.createMimeMessage();
    // public boolean send(String toEmail, String subject, String message){
    //     String myEmail = "cmpe275.cartshare@gmail.com";
    //     MimeMessage msg = emailSender.createMimeMessage();

    //     try{
    //         msg.setFrom(new InternetAddress(myEmail));
    //         msg.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
    //         msg.setSubject(subject);
    //         msg.setText(message);
    //         emailSender.send(msg);
    //         return true;
    //     } catch(MessagingException e){
    //         e.printStackTrace();
    //         return false;
    //     }
    // }

    // public boolean sendHTML(String toEmail, String subject, String message){
    //     String myEmail = "cmpe275.cartshare@gmail.com";
    //     MimeMessage msg = emailSender.createMimeMessage();

    //     try{
    //         msg.setFrom(new InternetAddress(myEmail));
    //         msg.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
    //         msg.setSubject(subject);
    //         msg.setContent(message, "text/html");
    //         emailSender.send(msg);
    //         return true;
    //     } catch(MessagingException e){
    //         e.printStackTrace();
    //         return false;
    //     }
    // }



    // public boolean send(String toEmail, String subject, String message){
    //     Properties prop = System.getProperties();
    //     prop.put("mail.smtp.host", "smtp.gmail.com"); //optional, defined in SMTPTransport
    //     prop.put("mail.smtp.auth", "true");
    //     prop.put("mail.smtp.port", "587"); // default port 25
    //     prop.put("mail.smtp.starttls.enable", "true");

    //     Session session = Session.getInstance(prop, null);
        // Message msg = new MimeMessage(session);

    //     String myEmail = "cmpe275.cartshare@gmail.com";
    //     String myPass = "cartsharePass@123";

    //     try {
		
	// 		// from
    //         msg.setFrom(new InternetAddress(myEmail));

	// 		// to 
    //         msg.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));

	// 		// subject
    //         msg.setSubject(subject);
			
	// 		// content 
    //         msg.setText(message);

	// 		// Get SMTPTransport
    //         SMTPTransport t = (SMTPTransport) session.getTransport("smtp");
			
	// 		// connect
    //         t.connect("smtp.gmail.com", myEmail, myPass);
			
	// 		// send
    //         t.sendMessage(msg, msg.getAllRecipients());

    //         // System.out.println("Response: " + t.getLastServerResponse());

    //         t.close();

    //         return true;

    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         return false;
    //     }

    // }

    // public boolean sendHTML(String toEmail, String subject, String message){
    //     Properties prop = System.getProperties();
    //     prop.put("mail.smtp.host", "smtp.gmail.com"); //optional, defined in SMTPTransport
    //     prop.put("mail.smtp.auth", "true");
    //     prop.put("mail.smtp.port", "587"); // default port 25
    //     prop.put("mail.smtp.starttls.enable", "true");

    //     Session session = Session.getInstance(prop, null);
    //     Message msg = new MimeMessage(session);

    //     String myEmail = "cmpe275.cartshare@gmail.com";
    //     String myPass = "cartsharePass@123";

    //     try {
		
	// 		// from
    //         msg.setFrom(new InternetAddress(myEmail));

	// 		// to 
    //         msg.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));

	// 		// subject
    //         msg.setSubject(subject);
			
	// 		// content 
    //         msg.setContent(message, "text/html");

	// 		// Get SMTPTransport
    //         SMTPTransport t = (SMTPTransport) session.getTransport("smtp");
			
	// 		// connect
    //         t.connect("smtp.gmail.com", myEmail, myPass);
			
	// 		// send
    //         t.sendMessage(msg, msg.getAllRecipients());

    //         // System.out.println("Response: " + t.getLastServerResponse());

    //         t.close();

    //         return true;

    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         return false;
    //     }

    // }
}