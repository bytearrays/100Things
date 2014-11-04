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

    public String getTrelloToken() {
        return trelloToken;
    }

    public String getTrelloKey() {
        return trelloKey;
    }

    public String getTrelloBoardId() {
        return trelloBoardId;
    }
}
