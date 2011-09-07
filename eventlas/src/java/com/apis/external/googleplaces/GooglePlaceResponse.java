/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apis.external.googleplaces;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author craigbrookes
 */
public class GooglePlaceResponse {
    
    String status;
    List<Place> results;

    public List<Place> getResults() {
        return results;
    }

    public void setResults(ArrayList<Place> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    
}
