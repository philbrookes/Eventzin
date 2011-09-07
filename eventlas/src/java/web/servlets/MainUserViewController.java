/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.servlets;

import api.dao.DAOFactory;
import api.dao.EventsDAO;
import api.dao.HibernateUtil;
import api.dao.UserFriendsDAO;
import api.dao.UsersDAO;
import com.models.EventFullDetail;
import com.models.Events;
import com.models.UserFriends;
import com.models.Users;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.ObjectNotFoundException;
import web.models.NavigationItem;

/**
 *
 * @author craigbrookes
 */
public class MainUserViewController extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = (String) request.getAttribute("username");
        username = username.replaceFirst("/", "");
        String action = (String) request.getAttribute("action");
        System.out.print(username);

        if (username != null && action != null) {
            DAOFactory factory = DAOFactory.getFactory();
            UsersDAO udao = factory.getUserDAO();
            UserFriendsDAO ufdao = factory.getUserFriendsDAO();
            try {
                udao.beginTransaction();
                Users user = udao.findUserByUserName(username);

                if (user != null) {
                    ArrayList<NavigationItem> navItems = new ArrayList<NavigationItem>(4);
                    navItems.add(new NavigationItem("/" + user.getUsername(), "Home"));
                    navItems.add(new NavigationItem("/" + user.getUsername() + "/invites", "Invites"));
                    navItems.add(new NavigationItem("/" + user.getUsername() + "/addevent", "Add Event"));
                    navItems.add(new NavigationItem("/" + user.getUsername() + "/myevents", "My Events"));
                    request.setAttribute("navitems", navItems);
                    request.setAttribute("user", user);
                    if (action.equals("index")) {
                        //should get nearby events and friends events
                        List<UserFriends> friends = ufdao.findFriendsByUsersId(user.getId());
                         ArrayList<HashMap<String, Object>> friendsEvents = new ArrayList<HashMap<String, Object>>(100);
                        if (friends != null && friends.isEmpty() != true) {
                           

                            for (UserFriends friend : friends) {
                                
                                Users usersFriend = friend.getFriend();
                                System.out.print("Getting Events For ** " + usersFriend.getUsername());
                                Set<Events> fevents = usersFriend.getEventses();
                                if (fevents != null && fevents.isEmpty() != true) {
                                   

                                    for (Events ev : fevents) {
                                        HashMap<String, Object> tempMap = new HashMap<String, Object>();
                                        System.out.print("adding event " + ev.getTitle());
                                        tempMap.put("user", usersFriend);
                                        tempMap.put("event", ev);
                                        friendsEvents.add(tempMap);
                                         System.out.print("adding event " + tempMap.toString());
                                        // tempMap = null;
                                    }
                                }

                            }
                        }
                        request.setAttribute("eventitems", friendsEvents);
                        RequestDispatcher dispatch = request.getRequestDispatcher("/UserMainView.jsp");
                        dispatch.forward(request, response);
                    } else if (action.equals("event")) {
                        EventsDAO edao = factory.getEventDAO();
                        String[] urlbits = request.getRequestURI().split("/");
                        if (urlbits.length >= 3) {
                            String eventName = urlbits[3].replace("-", " ");
                            EventFullDetail event = edao.getEventByEventNameAndUser(eventName, user);
                            request.setAttribute("event", event);
                            RequestDispatcher dispatch = request.getRequestDispatcher("/EventView.jsp");
                            dispatch.forward(request, response);
                        } else {
                            Set<Events> events = user.getEventses();

                            if (events != null) {
                                ArrayList<HashMap<String, Object>> eventsAndUser = new ArrayList<HashMap<String, Object>>(events.size());
                                for (Events event : events) {
                                    HashMap<String, Object> tempMap = new HashMap<String, Object>(2);
                                    tempMap.put("event", event);
                                    tempMap.put("user", user);
                                    tempMap = null;

                                }
                                request.setAttribute("eventitems", eventsAndUser);
                            }

                            RequestDispatcher dispatch = request.getRequestDispatcher("/UserMainView.jsp");
                            dispatch.forward(request, response);

                        }
                    } else if (action.equals("addEvent")) {
                        RequestDispatcher dispatch = request.getRequestDispatcher("/AddEventView.jsp");
                        dispatch.forward(request, response);
                    } else if (action.equals("myevents")) {

                        Set<Events> events = user.getEventses();

                        if (events != null) {
                            ArrayList<HashMap<String, Object>> eventsAndUser = new ArrayList<HashMap<String, Object>>(events.size());
                            for (Events event : events) {
                                HashMap<String, Object> tempMap = new HashMap<String, Object>(2);
                                tempMap.put("event", event);
                                tempMap.put("user", user);
                                eventsAndUser.add(tempMap);
                                //  tempMap = null;

                            }
                            request.setAttribute("eventitems", eventsAndUser);
                            RequestDispatcher dispatch = request.getRequestDispatcher("/UserMainView.jsp");
                            dispatch.forward(request, response);

                        }

                        udao.commitTransaction();
                    }
                }
            } catch (ObjectNotFoundException e) {
            }

        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
