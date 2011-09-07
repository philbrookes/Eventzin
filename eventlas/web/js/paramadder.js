/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
$('document').ready(function(){
    $('.addparam').click(function(){
        
       $('.addtable').append("<tr><td>Add Param & type</td><td><input name=\"param[]\" type=\"text\" class=\"generalinput\" placeholder=\"param,type\" /></td> </tr>");
       return false;
    });
});

