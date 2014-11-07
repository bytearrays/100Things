package com.bytearrays.onecthings.endpoints;

import com.bytearrays.onecthings.dao.MessageDao;
import com.bytearrays.onecthings.model.Message;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dobrescu on 11/7/14.
 */

@Path("/")
@Produces("application/json")
@Service
public class DailyThing {
    @Autowired
    MessageDao messageDao;

    private Logger LOG = Logger.getLogger(DailyThing.class);

    @GET
    @Produces("application/json")
    @Path("/ask")
    @Transactional
    public Response getQuestion(@QueryParam("date") String dateInString) {
        LOG.info("Listing the answer for: " + dateInString);


        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        try {

            Date date = formatter.parse(dateInString);
            LOG.info("Util Date: "+date);
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            LOG.info("SQL Date: "+sqlDate);
            Message m = messageDao.findByDate(sqlDate);
            if (m != null) {
                Map<String, String> response = new HashMap<String, String>();
                response.put("title", m.getTitle());
                response.put("description", m.getDescription());
                response.put("status", m.getStatus().toString());
                return Response.ok(response, MediaType.APPLICATION_JSON).build();
            } else {
                return Response.status(Response.Status.NOT_IMPLEMENTED).build();

            }
        } catch (ParseException e) {
            LOG.error(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

    }


}
