package com.bytearrays.onecthings.bean;

import com.bytearrays.onecthings.dao.MessageDao;
import com.bytearrays.onecthings.model.Message;
import com.bytearrays.onecthings.model.MessageStatus;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.trello4j.Trello;
import org.trello4j.TrelloImpl;
import org.trello4j.model.Card;

import javax.mail.MessagingException;
import java.util.List;

/**
 * Created by dobrescu on 11/3/14.
 */
@Component
public class TrelloService {

    @Autowired
    ApplicationConfigBean applicationConfigBean;

    @Autowired
    MailService mailService;

    @Autowired
    MessageDao messageDao;

    private Logger LOG = Logger.getLogger(TrelloService.class);
    private Integer CURRENT_NUMBER = 3;

    @Transactional(propagation = Propagation.REQUIRED)
    public boolean searchForFreshCards(){
        Trello trello = new TrelloImpl(applicationConfigBean.getTrelloKey(), applicationConfigBean.getTrelloToken());
        List<Card> cards = trello.getCardsByList(trello.getListByBoard(applicationConfigBean.getTrelloBoardId()).get(0).getId());
        if (cards.isEmpty()){
            LOG.error("There are no fresh cards!!!");
            return false;
        }
        Card selectedCard = cards.get(0);
        LOG.info("The selected card's title is: " + selectedCard.getName());

        try {
            mailService.send("100 Things About YOU, episode "+CURRENT_NUMBER, selectedCard.getName());
        } catch (MessagingException e) {
            LOG.error(e.getMessage());
            return false;
        }
//        selectedCard.setName(CURRENT_NUMBER+"# "+selectedCard.getName());
//        LOG.info("Title has changed to "+selectedCard.getName());

        trello.changeListOfCard(selectedCard.getId() ,trello.getListByBoard(applicationConfigBean.getTrelloBoardId()).get(1).getId());
        LOG.info("The card has been moved to PENDING");

        Message message = new Message();
        message.setTitle("100 Things About YOU, episode "+CURRENT_NUMBER);
        message.setDescription(selectedCard.getName());
        message.setStatus(MessageStatus.PENDING);
        messageDao.saveOrUpdate(message);

        LOG.info("A message object has been successfully created!");

        return true;
    }


}
