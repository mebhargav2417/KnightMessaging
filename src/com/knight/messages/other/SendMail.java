package com.knight.messages.other;


import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;


public class SendMail {
	
	public String sendMail(String toEmail, String subject, String body){
		String response = "";
		try {
			String fromEmail, password;
			Properties prop = new Properties();
			String propFileName = "config.properties";
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}
			fromEmail = prop.getProperty("idmail");
			password = prop.getProperty("maildwp");
			System.out.println("TLSEmail Start");
			Properties props = new Properties();
			if(fromEmail.contains("gmail.com")){
				props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
				props.put("mail.smtp.port", "587"); //TLS Port
				props.put("mail.smtp.auth", "true"); //enable authentication
				props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
				props.put("mail.smtp.ssl.trust", "*");
			} else if(fromEmail.contains("outlook.com")){
				props.put("mail.smtp.host", "smtp-mail.outlook.com");
				props.put("mail.smtp.port", "587");
				props.put("mail.smtp.starttls.enable","true");
				props.put("mail.smtp.auth", "true"); 
				props.put("mail.smtp.ssl.trust", "*");
			}
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
			message.setText(body);

			Transport.send(message);

			System.out.println("Done");
			return response;
		} catch(Exception e){
			return e.toString();
		}
	}

}
