package com.example.mailapp.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.http.HttpCredentialsAdapter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Optional;
import java.util.Properties;

@Service
public class MailService {

    private static final String APPLICATION_NAME = "MailSenderApp";
    private static final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    public void sendMail(String subject, String bodyText, MultipartFile file, String accessToken, String toEmail) throws Exception {
        MimeMessage email = createEmailWithAttachment(toEmail, "me", subject, bodyText, file);
        Gmail service = getGmailService(accessToken);
        sendMessage(service, "me", email);
    }

    private Gmail getGmailService(String accessToken) throws Exception {
        GoogleCredentials credentials = GoogleCredentials.create(new AccessToken(accessToken, null));
        return new Gmail.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                new HttpCredentialsAdapter(credentials)
        ).setApplicationName(APPLICATION_NAME).build();
    }

    private MimeMessage createEmailWithAttachment(String to, String from, String subject, String bodyText, MultipartFile file) throws Exception {
        Session session = Session.getDefaultInstance(new Properties(), null);
        MimeMessage email = new MimeMessage(session);

        email.setFrom(new InternetAddress(from));
        email.addRecipient(jakarta.mail.Message.RecipientType.TO, new InternetAddress(to));

        email.setSubject(subject);

        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText(bodyText);

        MimeBodyPart attachmentPart = new MimeBodyPart();
        attachmentPart.setFileName(file.getOriginalFilename());
        attachmentPart.setContent(
                file.getBytes(),
                Optional.ofNullable(file.getContentType()).orElse("application/octet-stream")
        );

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(textPart);
        multipart.addBodyPart(attachmentPart);

        email.setContent(multipart);
        return email;
    }

    private void sendMessage(Gmail service, String userId, MimeMessage email) throws Exception {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        String encodedEmail = Base64.getUrlEncoder().encodeToString(buffer.toByteArray());

        Message message = new Message();
        message.setRaw(encodedEmail);

        service.users().messages().send(userId, message).execute();
    }
}
