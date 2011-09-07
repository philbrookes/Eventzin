/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tests.modeltests;

import api.dao.DAOFactory;
import api.dao.EventsDAO;
import api.dao.HibernateUsersDAO;
import api.dao.HibernateUtil;
import api.dao.LocationDAO;
import com.models.EventFullDetail;
import com.models.Events;
import com.models.Location;
import java.util.List;

/**
 *
 * @author craigbrookes
 */
public class Eventstest {

    public static void main(String[] args){
        DAOFactory factory = DAOFactory.getFactory();
        EventsDAO edao = factory.getEventDAO();
        LocationDAO ldao = factory.getLocationDAO();

        edao.beginTransaction();
        List<Location>llist = ldao.findNearbyLocations(new Float(-7.129100), new Float(52.259800));

        List<EventFullDetail>elist = edao.getEventsByLocationIds(llist);

        for(EventFullDetail event : elist){
            event.getLatitude();
        }

        edao.commitTransaction();
    }

}
