/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package web.models;

/**
 *
 * @author craigbrookes
 */
public class NavigationItem {



    private String href;
    private String title;

    public NavigationItem(String href, String title) {
        this.href = href;
        this.title = title;
    }

    

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }




}
