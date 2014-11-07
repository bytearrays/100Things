package com.bytearrays.onecthings.model;

import javax.persistence.*;
import java.sql.Date;

/**
 * Created by dobrescu on 11/4/14.
 */
@Entity

@Table(name = "messages")
public class Message {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "description",columnDefinition="TEXT")
    private String description;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private MessageStatus status;

    @Column(name = "date")
    private Date date;

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus messageStatus) {
        this.status = messageStatus;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

