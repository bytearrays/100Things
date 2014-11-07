package com.bytearrays.onecthings.bean;

import com.bytearrays.onecthings.dao.MessageDao;
import com.bytearrays.onecthings.model.Message;
import com.bytearrays.onecthings.model.MessageStatus;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.trello4j.Trello;
import org.trello4j.TrelloImpl;
import org.trello4j.model.Card;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

/**
 * Created by dobrescu on 11/4/14.
 */
@Component
public class MailService {

    @Autowired
    ApplicationConfigBean applicationConfigBean;

    @Autowired
    MessageDao messageDao;

    @Autowired
    MailUtil mailUtil;

    private Logger LOG = Logger.getLogger(MailService.class);


    public void send(String subject, String body) throws MessagingException {
        mailUtil.send(subject, body);
    }

    @Transactional
    public void searchForPendingMails() {
        List<Message> pendingMessages = messageDao.getAllPending();
        int counter = messageDao.findAll().size();
        for (Message message : pendingMessages) {
            try {
                String reply = mailUtil.searchForReply(message.getTitle());
                if (reply != null) {
                    LOG.info("Message title: " + message.getDescription());
                    LOG.info("Message body: " + reply);
                    message.setTitle("#" + counter + " " + message.getDescription());
                    message.setDescription(reply);

                    Trello trello = new TrelloImpl(applicationConfigBean.getTrelloKey(), applicationConfigBean.getTrelloToken());
                    List<Card> cardList = trello.getCardsByList(trello.getListByBoard(applicationConfigBean.getTrelloBoardId()).get(1).getId());
                    Card selectedCard = null;
                    for (Card card : cardList) {
                        if (card.getDesc().contains(message.getTitle())) {
                            selectedCard = card;
                            message.setStatus(MessageStatus.DELIVERED);
                            messageDao.saveOrUpdate(message);
                            trello.overwriteCardDesc(selectedCard.getId(),message.getDescription());
                            trello.overwriteCardName(selectedCard.getId(),message.getTitle());
                            LOG.info("Card 1:1 with GMail found");
                            LOG.info("Name has been changed to: " + selectedCard.getName());
                        }
                    }
                    if (selectedCard == null) {
                        LOG.warn("No card has been found for the GMail message!");
                        continue;
                    }
                    trello.changeListOfCard(selectedCard.getId(), trello.getListByBoard(applicationConfigBean.getTrelloBoardId()).get(2).getId());
                    LOG.info("Card has been moved to DELIVERED");
                    int numberOfFinishedCards = trello.getCardsByList(trello.getListByBoard(applicationConfigBean.getTrelloBoardId()).get(2).getId()).size();

                }
            } catch (MessagingException e) {
                LOG.error(e.getMessage());
            } catch (IOException e) {
                LOG.error(e.getMessage());
            }
        }

    }
}
