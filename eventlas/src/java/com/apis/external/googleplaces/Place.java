/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apis.external.googleplaces;


import com.apis.external.googlemaps.Geom;
import java.util.List;

/**
 *
 * @author craigbrookes
 */
public class Place {
    String id;
    String name;
    String formatted_phone_number;
    String icon;
    String vicinity;
    List<String> types;
    Geom geometry;
    String reference;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    
    
    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
    
    
    
    public String getFormatted_phone_number() {
        return formatted_phone_number;
    }

    public void setFormatted_phone_number(String formatted_phone_number) {
        this.formatted_phone_number = formatted_phone_number;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public String getVicinity() {
        return vicinity;
    }

    public Geom getGeometry() {
        return geometry;
    }

    public void setGeometry(Geom geometry) {
        this.geometry = geometry;
    }
    
    
    
    

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Place other = (Place) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if ((this.formatted_phone_number == null) ? (other.formatted_phone_number != null) : !this.formatted_phone_number.equals(other.formatted_phone_number)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 71 * hash + (this.formatted_phone_number != null ? this.formatted_phone_number.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Place{" + "name=" + name + ", formatted_phone_number=" + formatted_phone_number + '}';
    }
    
    
    
}
