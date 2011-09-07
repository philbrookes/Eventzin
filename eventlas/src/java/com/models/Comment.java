/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.models;

import java.util.Date;

/**
 *
 * @author craigbrookes
 */
public interface Comment {
    public String getComment();
    public int getRating();
    public Users getUser();
    public Integer getId();
    public Date getDateAdded();
}
