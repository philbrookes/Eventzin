package api.dao;

import com.models.Alphas;
import com.models.EventCategorys;
import com.models.EventFullDetail;
import com.models.Eventcomments;
import com.models.Events;
import com.models.Fans;
import com.models.Groups;
import com.models.Icons;
import com.models.Invites;
import com.models.ApiKeys;
import com.models.AppInvites;
import com.models.City;
import com.models.EventQuestionAnswers;
import com.models.EventQuestions;
import com.models.Methods;
import com.models.Parameters;
import com.models.TempInvite;
import com.models.MobileKeys;
import com.models.Profile;
import com.models.Twitter;
import com.models.TwitterEventStatus;
import com.models.UserEventStatus;
import com.models.UserFriends;
import com.models.Users;
import com.models.Usersgroups;
import com.models.Venuecomments;
import com.models.Venues;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;


abstract public class HibernateUtil {
    

    private static SessionFactory factory;
    private static SessionFactory factory2;

    protected static Configuration getEventsConfig(){
	AnnotationConfiguration config = new AnnotationConfiguration();
	config.addAnnotatedClass(Events.class);
	config.addAnnotatedClass(Icons.class);
	config.addAnnotatedClass(Invites.class);
	config.addAnnotatedClass(Users.class);
	config.addAnnotatedClass(Venues.class);
        config.addAnnotatedClass(ApiKeys.class);
        config.addAnnotatedClass(Groups.class);
        config.addAnnotatedClass(Eventcomments.class);
        config.addAnnotatedClass(Venuecomments.class);
        config.addAnnotatedClass(EventFullDetail.class);
        config.addAnnotatedClass(Fans.class);
        config.addAnnotatedClass(MobileKeys.class);
        config.addAnnotatedClass(UserEventStatus.class);
        config.addAnnotatedClass(Parameters.class);
        config.addAnnotatedClass(Methods.class);
        config.addAnnotatedClass(TempInvite.class);
        config.addAnnotatedClass(EventCategorys.class);
        config.addAnnotatedClass(EventQuestions.class);
        config.addAnnotatedClass(EventQuestionAnswers.class);
        config.addAnnotatedClass(UserFriends.class);
        config.addAnnotatedClass(Alphas.class);
        config.addAnnotatedClass(Profile.class);
        config.addAnnotatedClass(Usersgroups.class);
        config.addAnnotatedClass(City.class);
        config.addAnnotatedClass(Twitter.class);
        config.addAnnotatedClass(AppInvites.class);
        config.addAnnotatedClass(TwitterEventStatus.class);
	config.configure();
	return config;
    }

   
    


    public static Session getSession(){
       
	if(factory == null){

                Configuration conf = getEventsConfig();

               factory = conf.buildSessionFactory();
               
            
	}
	Session hibSession = factory.getCurrentSession();
	return hibSession;
       
    }


   

    public static void closeSession(){
       //HibernateUtil.getSession().flush();
	HibernateUtil.getSession().close();
    }
    
    public static Session beginTransaction(){
	Session session = HibernateUtil.getSession();
	session.beginTransaction();
	return session;
    }
    public static void commitTransaction(){
	HibernateUtil.getSession().getTransaction().commit();
        
    }
    public static void rollbackTransaction(){
	HibernateUtil.getSession().getTransaction().rollback();
    }
}
