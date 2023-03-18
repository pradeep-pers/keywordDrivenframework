


import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;





public class Mail {

    private static final String SMTP_HOST_NAME = "smtp.gmail.com";
    private static final int SMTP_HOST_PORT = 587;
    private static final String SMTP_AUTH_USER = "result.automation@gmail.com"; // provide user
	private static final String SMTP_AUTH_PWD = "automation@1234"; // provide password

    public static void sendMail(String replyTo, String toEmail,String subjectEmail,String bodyEmail,String pathToAttachmentEmail) throws Exception{
    try {
        Properties props = new Properties();
 
        props.put("mail.smtp.host", SMTP_HOST_NAME);
		props.put("mail.smtp.socketFactory.port", SMTP_HOST_PORT);
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "587");
		
      /*  Session mailSession = Session.getDefaultInstance(props);
        mailSession.setDebug(true);
        Transport transport = mailSession.getTransport();*/
		
		
		Session mailSession = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(SMTP_AUTH_USER,SMTP_AUTH_PWD);
					}
				});
 
		Message message = new MimeMessage(mailSession);
        
        // Set From: header field of the header.
        message.setFrom(new InternetAddress(SMTP_AUTH_USER));
        
        // set To: recipents
        String [] mailRecipents = toEmail.split(",");
        for (int i=0;i<mailRecipents.length;i++)
        {
        message.addRecipient(Message.RecipientType.TO,
                new InternetAddress(mailRecipents[i]));
        }
       
        // set To: recipents
        String [] mailReplyTo = replyTo.split(",");
        InternetAddress[] replyToAddress = new InternetAddress[mailReplyTo.length];
        for (int i=0;i<mailReplyTo.length;i++)
        {
        		replyToAddress[i] = new InternetAddress(mailReplyTo[i]);
        }
        message.setReplyTo(replyToAddress);

        // Set Subject: header field
		message.setSubject(subjectEmail);
		
		// Create the message part 
		BodyPart messageBodyPart = new MimeBodyPart();
		
		// Fill the message
		messageBodyPart.setText(bodyEmail);
		
		// Create a multipart message
		Multipart multipart = new MimeMultipart();
		
		// Set text message part
		multipart.addBodyPart(messageBodyPart);
		
		// Part two is attachment
		messageBodyPart = new MimeBodyPart();
		String filename = TestSuitRunner.resultFileName+".html";
		DataSource source = new FileDataSource(pathToAttachmentEmail);
		messageBodyPart.setDataHandler(new DataHandler(source));
		messageBodyPart.setFileName(filename);
		multipart.addBodyPart(messageBodyPart);
		// Send the complete message parts
		message.setContent(multipart);
		        
		Transport.send(message);
		 
		System.out.println("Mail Sent successfully");
    	}
    catch (Exception e){
    	TestSuitRunner.logger.error("Mail|sendMail|Unable to send mail.",e);
    }
    	}
    

} //End of class