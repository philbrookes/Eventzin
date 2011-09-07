/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.dao;


/**
 *
 * @author craigbrookes
 */
public class HibernateDAOFactory extends DAOFactory{

    @Override
    public IconsDAO getIconsDAO() {
       return new HibernateIconsDAO();
    }

    @Override
    public LocationDAO getLocationDAO() {
        return new HibernateLocationDAO();
    }

    @Override
    public VisibiltyDAO getVisibilityDAO() {

        return new HibernateVisibilityDAO();

    }

    @Override
    public VenuesDAO getVenuesDAO() {
        return new HibernateVenuesDAO();

    }

    @Override
    public UsersDAO getUserDAO() {
       return new HibernateUsersDAO();
    }
    @Override
    public EventsDAO getEventDAO(){
        return new HibernateEventsDAO();
    }

    @Override
    public ApiKeysDAO getKeysDAO() {
       return new HibernateKeysDAO();
    }

    @Override
    public GroupDAO getGroupDAO() {
        return new HibernateGroupDAO();
    }

    @Override
    public InvitesDAO getInvitesDAO() {
            return new HibernateInvitesDAO();
    }

    public EventscommentDAO getEventsCommentDAO(){
        return new HibernateEventcommentsDAO();
    }

    @Override
    public VenuescommentsDAO getVenuesCommentDAO() {
        return new HibernateVenuescommentsDAO();
    }

    @Override
    public FansDAO getFansDAO() {
      return new HibernateFansDAO();
    }

    @Override
    public MobilekeysDAO getTempMobileKeysDAO(){
        return new HibernateMobileKeysDAO();
    }

    @Override
    public UserEventStatusDAO getUserEventStatusDAO(){
        return new HibernateUserEventStatusDAO();
    }

    @Override
    public MethodsDAO getMethodsDAO() {
      return new HibernateMethodsDAO();
    }

    @Override
    public ParametersDAO getParametersDAO() {
        return new HibernateParametersDAO();
    }

    @Override
    public TempInvitesDAO getTempInvitesDAO() {
        return new HibernateTempInvitesDAO();
    }

    @Override
    public EventCategorysDAO getEventCategoryDAO() {
       return new  HibernateEventCategorysDAO();
    }

    @Override
    public EventQuestionsAnswersDAO getEventQuestionsAnswersDAO() {
        return new HibernateEventQuestionsAnswersDAO();
    }

    @Override
    public EventQuestionsDAO getEventQuestionsDAO() {
        return new HibernateEventQuestionsDAO();
    }

    @Override
    public UserFriendsDAO getUserFriendsDAO() {
        return new HibernateUserFriendsDAO();
    }

    @Override
    public AlphasDAO getAlphasDAO() {
        return new HibernateAlphasDAO();
    }

    @Override
    public ProfileDAO getProfileDAO() {
      return new HibernateProfileDAO();
    }

    @Override
    public UsersGroupsDAO getUsersGroupsDAO() {
        return new HibernateUsersGroupsDAO();
    }

    @Override
    public CityDAO getCityDAO() {
       return new HibernateCityDAO();
    }

    @Override
    public TwitterDAO getTwitterDAO() {
        return new HibernateTwitterDAO();
    }

    @Override
    public AppInvitesDAO getAppInvitesDAO() {
        return new HibernateAppInvitesDAO();
    }

    @Override
    public TwitterEventStatusDAO getTwitterEventStatusDAO() {
        return new HibernateTwitterEventStatusDAO();
    }


    
}
