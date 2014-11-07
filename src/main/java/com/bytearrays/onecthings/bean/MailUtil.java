package com.bytearrays.onecthings.bean;

import com.bytearrays.onecthings.dao.MessageDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.SearchTerm;
import javax.mail.search.SubjectTerm;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dobrescu on 11/4/14.
 */
@Component
public class MailUtil {

    @Autowired
    ApplicationConfigBean applicationConfigBean;

    @Autowired
    MessageDao messageDao;

    private Logger LOG = Logger.getLogger(MailUtil.class);

    public Session getSMTPSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(applicationConfigBean.getGmailUser(), applicationConfigBean.getGmailPassword());
                    }
                });
        return session;
    }

    public Session getIMAPSession() {
        Properties props = new Properties();
        props.put("mail.imap.host", "imap.gmail.com");
        props.setProperty("mail.store.protocol", "imap");
        props.setProperty("mail.imap.port", "993");
        props.put("mail.imap.ssl.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.imap.socketFactory.port", 993);
        props.put("mail.imap.connectiontimeout", 1000);
        Session session = Session.getDefaultInstance(props);
        return session;
    }

    public String searchForReply(final String keyword) throws MessagingException, IOException {
        Folder folderInbox = null;
        Store store = null;
        try {
            Session session = getIMAPSession();
            store = session.getStore("imaps");
            store.connect("imap.gmail.com", applicationConfigBean.getGmailUser(), applicationConfigBean.getGmailPassword());
            // opens the inbox folder
            folderInbox = store.getFolder("INBOX");
            folderInbox.open(Folder.READ_ONLY);
            System.out.println(folderInbox.getMessageCount());
            SearchTerm st = new SubjectTerm(keyword);

            // performs search through the folder
            Message[] foundMessages = folderInbox.search(st);

            if (foundMessages.length<=0){
                return null;
            }

            Message message = foundMessages[foundMessages.length - 1];
            if (!message.isMimeType("multipart/*")) {
                return null;
            }
            int numberOfParts = ((MimeMultipart) message.getContent()).getCount();
            String actualMessage = getText(((MimeMultipart) message.getContent()).getBodyPart(numberOfParts - 1));
            Pattern pattern = Pattern.compile("<div dir=\"ltr\">(.*)</div>");
            Matcher matcher = pattern.matcher(actualMessage);
            if (matcher.find()) {
                return matcher.group(1);
            }
        } finally {
            // disconnect
            folderInbox.close(false);
            store.close();
        }
        return null;
    }

    private static String getText(Part p) throws
            MessagingException, IOException {
        if (p.isMimeType("text/*")) {
            String s = (String) p.getContent();
            boolean textIsHtml = p.isMimeType("text/html");
            return s;
        }

        if (p.isMimeType("multipart/alternative")) {
            // prefer html text over plain text
            Multipart mp = (Multipart) p.getContent();
            String text = null;
            for (int i = 0; i < mp.getCount(); i++) {
                Part bp = mp.getBodyPart(i);
                if (bp.isMimeType("text/plain")) {
                    if (text == null)
                        text = getText(bp);
                    continue;
                } else if (bp.isMimeType("text/html")) {
                    String s = getText(bp);
                    if (s != null)
                        return s;
                } else {
                    return getText(bp);
                }
            }
            return text;
        } else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                String s = getText(mp.getBodyPart(i));
                if (s != null)
                    return s;
            }
        }

        return null;
    }

    public void send(String subject, String body) throws MessagingException {
        Session session = getSMTPSession();
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(applicationConfigBean.getSenderEmail()));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(applicationConfigBean.getRecipientEmail()));
        message.setSubject(subject);
        message.setText(body);
        Transport.send(message);
    }

}
