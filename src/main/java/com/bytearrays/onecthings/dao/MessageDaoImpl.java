package com.bytearrays.onecthings.dao;

import com.bytearrays.onecthings.model.Message;
import com.bytearrays.onecthings.model.MessageStatus;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public class MessageDaoImpl extends AbstractDaoImpl<Message> implements MessageDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Message findByTitle(String title) {
        Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Message.class);
        criteria.add(Restrictions.eq("title", title));
        Object rawResult = criteria.uniqueResult();
        if (rawResult == null) return null;
        return (Message) rawResult;
    }

    @Override
    public List<Message> getAllPending() {
        Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Message.class);
        criteria.add(Restrictions.eq("status", MessageStatus.PENDING));
        List<Message> result = criteria.list();
        return result;
    }

    @Override
    public Message findByDate(Date date) {
        Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Message.class);
        criteria.add(Restrictions.eq("date", date));
        Object rawResult = criteria.uniqueResult();
        if (rawResult == null) return null;
        return (Message) rawResult;
    }


}
