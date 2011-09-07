/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apis.external.googleplaces;

import api.dao.DAOFactory;
import api.dao.IconsDAO;
import api.dao.LocationDAO;
import api.dao.VenuesDAO;
import api.helpers.ParameterHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.models.Icons;
import com.models.Location;
import com.models.Venues;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;

/**
 *
 * @author craigbrookes
 */
public class GooglePlaces {

    Float latitude;
    Float longitude;
    Integer radius;
    private static final String PLACES_URI = "https://maps.googleapis.com/maps/api/place/search/json?key=AIzaSyBz0hAldjOckZNb3FltpsO9D9-pWaAkg8k&sensor=false";

    public GooglePlaces(Float latitude, Float longitude, Integer radius) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
    }

    List<Place> getPlaces() {
        List<Place> places = null;
        try {

            JsonParser parser = new JsonParser();
            String uri = PLACES_URI + "&radius=" + this.radius + "&location=" + this.latitude + "," + this.longitude;
            URL url = new URL(uri);
            JsonElement ele = parser.parse(new InputStreamReader(url.openConnection().getInputStream()));
            GooglePlaceResponse g = new Gson().fromJson(ele, GooglePlaceResponse.class);
            if (g.status.equals("OK")) {
                places = g.getResults();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return places;
    }

    public List<Venues> getPlacesConvertToVenuesAndSave() {
        List<Venues> venues = new ArrayList<Venues>();
        List<Place> p = this.getPlaces();
        if (p.isEmpty() != true) {
            try {
                DAOFactory factory = DAOFactory.getFactory();
                LocationDAO locd = factory.getLocationDAO();
                VenuesDAO vdao = factory.getVenuesDAO();
                IconsDAO icodao = factory.getIconsDAO();
                vdao.beginTransaction();
                for (Place pl : p) {
                    Venues ven = vdao.getByGoogleId(pl.getId());
                    if (pl.getVicinity() != null && ven == null) {
                        ven = new Venues();
                        System.out.println(pl.getName());

                        ven.setAddress(pl.getVicinity());

                        Icons icon = icodao.findByPrimaryKey(new Integer(15));
                        ven.setIcons(icon);

                        Float longi = pl.getGeometry().getLocation().get("lng");
                        Float lat = pl.getGeometry().getLocation().get("lat");
                        Location loc = locd.createLocation(longi, lat);
                        locd.save(loc);
                        ven.setRating(new Integer(0));
                        ven.setSummary("");
                        ven.setLocation(loc);
                        ven.setGoogleid(pl.getId());
                        ven.setName(pl.getName());
                        venues.add(ven);
                        vdao.save(ven);
                        
                    }
                }
                vdao.commitTransaction();
                
            } catch (HibernateException e) {
                e.printStackTrace();
            }

        }
        return venues;
    }

    
    /*
    public static void main(String[] args) {

        GooglePlaces gp = new GooglePlaces(new Float(52.25748), new Float(-7.111845), new Integer(150));

        List<Venues> p = gp.getPlacesConvertToVenuesAndSave();
        
        System.out.println(p);
        


    }
     * 
     */
}
