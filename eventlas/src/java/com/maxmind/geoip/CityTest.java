/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxmind.geoip;

import java.io.IOException;

/**
 *
 * @author craigbrookes
 */
public class CityTest {
     public static void main(String[] args) {
	try {
	    LookupService cl = new LookupService("/Users/craigbrookes/NetBeansProjects/Eventzin/web-app/GeoIPCity.dat",
					LookupService.GEOIP_MEMORY_CACHE );
            Location l1 = cl.getLocation("213.52.50.8");
            Location l2 = cl.getLocation(args[0]);
	    System.out.println("countryCode: " + l2.countryCode +
                               "\n countryName: " + l2.countryName +
                               "\n region: " + l2.region +
                               "\n regionName: " + regionName.regionNameByCode(l2.countryCode, l2.region) +
                               "\n city: " + l2.city +
                               "\n postalCode: " + l2.postalCode +
                               "\n latitude: " + l2.latitude +
                               "\n longitude: " + l2.longitude +
                               "\n distance: " + l2.distance(l1) +
                               "\n distance: " + l1.distance(l2) + 
 			       "\n metro code: " + l2.metro_code +
 			       "\n area code: " + l2.area_code +
                               "\n timezone: " + timeZone.timeZoneByCountryAndRegion(l2.countryCode, l2.region));

	    cl.close();
	}
	catch (IOException e) {
	    System.out.println("IO Exception");
	}
    }
}
