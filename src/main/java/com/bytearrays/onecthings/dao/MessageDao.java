package com.bytearrays.onecthings.dao;

import com.bytearrays.onecthings.dao.AbstractDao;
import com.bytearrays.onecthings.model.Message;

import java.sql.Date;
import java.util.List;

/**
 * Created by dobrescu on 11/4/14.
 */
public interface MessageDao extends AbstractDao<Message> {
    public Message findByTitle(String title);

    public List<Message> getAllPending();

    public Message findByDate(Date date);
}
