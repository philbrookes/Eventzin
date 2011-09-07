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
public class GetPlacesResponse extends Response{
    List<Venue> Results;

    public List<Venue> getResults() {
	return Results;
    }

    public void setResults(List<Venue> Results) {
	this.Results = Results;
    }
    
}
