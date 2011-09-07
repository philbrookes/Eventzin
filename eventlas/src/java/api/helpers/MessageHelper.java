/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.helpers;

import java.util.StringTokenizer;

/**
 *
 * @author craigbrookes
 */
public class MessageHelper {

    public static String str_replace(String from, String to, String source) {
        StringBuilder bf = new StringBuilder("");
        StringTokenizer st = new StringTokenizer(source, from, true);
        while (st.hasMoreTokens()) {
            String tmp = st.nextToken();
            System.out.println("*" + tmp);
            if (tmp.equals(from)) {
                bf.append(to);
            } else {
                bf.append(tmp);
            }
        }
        return bf.toString();
    }
}
