package com.bytearrays.onecthings.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Created by dobrescu on 11/3/14.
 */
@Configuration
public class ApplicationConfigBean {

    @Value("${trello.token}")
    String trelloToken;

    @Value("${trello.key}")
    String trelloKey;

    @Value("${trello.board.id}")
    String trelloBoardId;

    @Value("${gmail.user}")
    String gmailUser;

    @Value("${gmail.password}")
    String gmailPassword;

    @Value("${sender.email}")
    String senderEmail;

    @Value("${recipient.email}")
    String recipientEmail;

    public String getTrelloToken() {
        return trelloToken;
    }

    public String getTrelloKey() {
        return trelloKey;
    }

    public String getTrelloBoardId() {
        return trelloBoardId;
    }

    public String getGmailUser() { return gmailUser; }

    public String getGmailPassword() { return gmailPassword; }

    public String getSenderEmail() { return senderEmail; }

    public String getRecipientEmail() { return recipientEmail; }
}
