package com.knight.messages.other;


import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;


public class SendMail {
	
	public String sendMail(String toEmail, String subject){
		String response = "";
		try {
			final String fromEmail = "asktaf@outlook.com"; //requires valid gmail id
			final String password = "n2bv2222"; // correct password for gmail id
			
			System.out.println("TLSEmail Start");
			Properties props = new Properties();
//			props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
//			props.put("mail.smtp.port", "587"); //TLS Port
//			props.put("mail.smtp.auth", "true"); //enable authentication
//			props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
			
			props.put("mail.smtp.host", "smtp-mail.outlook.com");
			props.put("mail.smtp.port", "587");
			props.put("mail.smtp.starttls.enable","true");
			props.put("mail.smtp.auth", "true"); 
			
			Session session = Session.getInstance(props,
					  new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(fromEmail, password);
						}
					  });
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromEmail));
			message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(toEmail));
			message.setSubject(subject);
			message.setText("Dear Mail Crawler,"
				+ "\n\n No spam to my email, please!");

			Transport.send(message);

			System.out.println("Done");
			return response;
		} catch(Exception e){
			return e.toString();
		}
	}

}
