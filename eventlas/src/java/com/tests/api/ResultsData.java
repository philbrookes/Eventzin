/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tests.api;

/**
 *
 * @author craigbrookes
 */
public class ResultsData {


        String formatted_address;
        Geom geometry;

        public String getFormatted_address() {
            return formatted_address;
        }

        public void setFormatted_address(String formatted_address) {
            this.formatted_address = formatted_address;
        }

        public Geom getGeometry() {
            return geometry;
        }

        public void setGeometry(Geom geometry) {
            this.geometry = geometry;
        }

}
