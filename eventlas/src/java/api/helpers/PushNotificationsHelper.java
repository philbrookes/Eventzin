/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.helpers;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import java.util.Map;

/**
 *
 * @author craigbrookes
 */
public class PushNotificationsHelper {

   private ApnsService serv;

    public PushNotificationsHelper() {
        System.out.println(FileHelper.getRootDir() + "eventzin_push_cert.p12");
        this.serv = APNS.newService().withCert(FileHelper.getRootDir() + "eventzin_push_cert.p12", "aw3se4dr5").withSandboxDestination().build();
    }




    public void sendPushNotification(String deviceToken, String alertbody, int badgeNum, Map<String, ? extends Object> customFields) {

        String payload = APNS.newPayload().alertBody(alertbody).badge(badgeNum).customFields(customFields).build();

        String token = deviceToken;
        this.serv.push(token, payload);
    }

    public void sendPushNotification(String deviceToken, String alertbody) {
        String payload = APNS.newPayload().alertBody(alertbody).build();
        this.serv.push(deviceToken, payload);
    }

    public void sendPushNotification(String deviceToken, String alertbody, int badgeNum) {
        String payLoad = APNS.newPayload().alertBody(alertbody).badge(badgeNum).build();
        this.serv.push(deviceToken,payLoad);
        
    }
}
