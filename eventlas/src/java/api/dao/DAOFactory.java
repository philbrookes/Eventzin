/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.dao;

import com.models.AppInvites;


/**
 *
 * @author craigbrookes
 */
public abstract class DAOFactory {

    public static final Class FACTORY_CLASS = api.dao.HibernateDAOFactory.class;

    public static DAOFactory getFactory(){
        try{
            return (DAOFactory)FACTORY_CLASS.newInstance();
        }catch(Exception e){
            throw new RuntimeException("could not create factory");
        }
    }


    public abstract UsersDAO getUserDAO();
    public abstract LocationDAO getLocationDAO();
    public abstract VisibiltyDAO getVisibilityDAO();
    public abstract VenuesDAO getVenuesDAO();
    public abstract IconsDAO getIconsDAO();
    public abstract EventsDAO getEventDAO();
    public abstract ApiKeysDAO getKeysDAO();
    public abstract GroupDAO getGroupDAO();
    public abstract InvitesDAO getInvitesDAO();
    public abstract EventscommentDAO getEventsCommentDAO();
    public abstract VenuescommentsDAO getVenuesCommentDAO();
    public abstract FansDAO getFansDAO();
    public abstract MobilekeysDAO getTempMobileKeysDAO();
    public abstract UserEventStatusDAO getUserEventStatusDAO();
    public abstract ParametersDAO getParametersDAO();
    public abstract MethodsDAO getMethodsDAO();
    public abstract TempInvitesDAO getTempInvitesDAO();
    public abstract EventCategorysDAO getEventCategoryDAO();
    public abstract EventQuestionsAnswersDAO getEventQuestionsAnswersDAO();
    public abstract EventQuestionsDAO getEventQuestionsDAO();
    public abstract UserFriendsDAO getUserFriendsDAO();
    public abstract AlphasDAO getAlphasDAO();
    public abstract ProfileDAO getProfileDAO();
    public abstract UsersGroupsDAO getUsersGroupsDAO();
    public abstract CityDAO getCityDAO();
    public abstract TwitterDAO getTwitterDAO();
    public abstract AppInvitesDAO getAppInvitesDAO();
    public abstract  TwitterEventStatusDAO getTwitterEventStatusDAO();
    

}
