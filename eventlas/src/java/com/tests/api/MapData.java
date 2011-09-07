/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tests.api;

import java.util.List;

/**
 *
 * @author craigbrookes
 */
public class MapData {



        String status;
        List<ResultsData>results;

        public List<ResultsData> getResults() {
            return results;
        }

        public void setResults(List<ResultsData> results) {
            this.results = results;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

}
