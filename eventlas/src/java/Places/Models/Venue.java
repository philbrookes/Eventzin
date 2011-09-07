/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Places.Models;

import java.util.List;

/**
 *
 * @author philip
 */
public class Venue {
    public String name, icon, reference, id, formatted_address;
    List<String> types;
    Geometry geomotry;

    public Geometry getGeomotry() {
	return geomotry;
    }

    public void setGeomotry(Geometry geomotry) {
	this.geomotry = geomotry;
    }
    

    public String getIcon() {
	return icon;
    }

    public void setIcon(String icon) {
	this.icon = icon;
    }

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getReference() {
	return reference;
    }

    public void setReference(String reference) {
	this.reference = reference;
    }

    public List<String> getTypes() {
	return types;
    }

    public void setTypes(List<String> types) {
	this.types = types;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }
    
    
    
}
