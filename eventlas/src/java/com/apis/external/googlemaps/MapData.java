/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.apis.external.googlemaps;

import com.tests.api.*;
import java.util.List;

/**
 *
 * @author craigbrookes
 */
public class MapData<T> {



        String status;
        List<Object>results;

        public List<Object> getResults() {
            return results;
        }

        public void setResults(List<Object> results) {
            this.results = results;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

}
