/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.helpers;

import com.models.Comment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author phil
 */
public class CommentResponses extends Responses {
    public static HashMap makeCommentHashmap(Comment Comment){
        HashMap<String, Object> returns = new HashMap<String, Object>();
        returns.put("id", Comment.getId());
        returns.put("comment", Comment.getComment());
        returns.put("user", Comment.getUser().getUsername());
        returns.put("rating", Comment.getRating());
        returns.put("dateadded", Comment.getDateAdded().toString());
        return returns;
    }
    public static ArrayList makeCommentsArrayList(Set<Comment> comments){
         ArrayList<HashMap<String, Object>> commentlist = new ArrayList<HashMap<String, Object>>(comments.size());
           HashMap<String, Object>countMap = new HashMap<String, Object>(1);
           countMap.put("count", comments.size());
            for (Comment comment : comments) {

                commentlist.add(makeCommentHashmap(comment));
            }
            return commentlist;
    }

    
}
