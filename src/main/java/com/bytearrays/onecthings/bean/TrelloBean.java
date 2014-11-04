package com.bytearrays.onecthings.bean;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.trello4j.Trello;
import org.trello4j.TrelloImpl;
import org.trello4j.model.Card;

import java.util.List;

/**
 * Created by dobrescu on 11/3/14.
 */
@Component
public class TrelloBean {

    @Autowired
    ApplicationConfigBean applicationConfigBean;
    private Logger LOG = Logger.getLogger(TrelloBean.class);


    public void fetchFirstFresh(){
        Trello trello = new TrelloImpl(applicationConfigBean.getTrelloKey(), applicationConfigBean.getTrelloToken());
        List<Card> cards = trello.getCardsByList(trello.getListByBoard(applicationConfigBean.getTrelloBoardId()).get(0).getId());

        System.out.println(cards.size());
    }


}
