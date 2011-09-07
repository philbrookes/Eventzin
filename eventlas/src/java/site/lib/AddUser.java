/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package site.lib;

import api.dao.DAOFactory;
import api.dao.UsersDAO;
import api.helpers.ParameterHelper;
import com.models.Users;
import java.util.Date;
import org.hibernate.HibernateException;

/**
 *
 * @author craigbrookes
 */
public class AddUser {

    private String username;
    private String email;
    private String password;



    public AddUser(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getMobileKey(){
        return ParameterHelper.md5(email+password+username+new Date().toString());
    }

    public boolean addUser()throws Exception{
        DAOFactory factory = DAOFactory.getFactory();
        UsersDAO udao = factory.getUserDAO();
        try{
            udao.beginTransaction();
                Users u = new Users(username);
                u.setEmail(email);
                u.setPassword(password);
                u.setMobileKey(this.getMobileKey());
                udao.save(u);
            udao.commitTransaction();
            }catch(HibernateException e){
                throw e;

            }
        return true;
        
    }

}
