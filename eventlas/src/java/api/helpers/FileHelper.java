/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package api.helpers;




import api.logger.Logger;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author craigbrookes
 */
public class FileHelper implements ServletContextListener{


    private static String rootDir;
    public static String uploadedEventImgDir;
    public static String uploadedIconImgDir;
    public static final String TMP_DIR =  "/usr/tmp";
    public static ArrayList<String> allowed = new ArrayList<String>(Arrays.asList("image/jpg", "image/png"));

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("CONTEXT HAS BEEN LOADED CONTEXT HAS BEEN LOADED *******************************");
        ServletContext se = sce.getServletContext();
        rootDir = se.getRealPath("/");
        uploadedEventImgDir = rootDir+"eventimages";
        uploadedIconImgDir = rootDir+"icons";
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
       
    }

    public static String getRootDir(){
        return rootDir;
    }

    public static String getTMP_DIR() {
        return TMP_DIR;
    }

    public static String getUploadedEventImgDir() {
        return uploadedEventImgDir;
    }

    public static String getUploadedIconImgDir() {
        return uploadedIconImgDir;
    }

    public static ArrayList<File> uploadFiles(HttpServletRequest req, String location)throws Exception, FileUploadException{
        ArrayList<File>uploaded = new ArrayList<File>();
        File tmpDir = new File(FileHelper.TMP_DIR);
            if (tmpDir.isDirectory()) {
                api.logger.Logger.debug(FileHelper.class.getName(), location);
                File storeDir = new File(location);

                if (storeDir.isDirectory()) {

                    DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
                    fileItemFactory.setSizeThreshold((1 * 1024 * 1024) * 5);
                    fileItemFactory.setRepository(storeDir);
                    ServletFileUpload uploadHandler = new ServletFileUpload(fileItemFactory);

                    /*
                     * Parse the request to get the file out
                     */

                    List items = uploadHandler.parseRequest(req);
                    Iterator<FileItem> itr = items.iterator();

                     while (itr.hasNext()) {
                        FileItem item = itr.next();

                        /*
                         * Handle Form Fields.
                         */
                        if (item.isFormField() != true) {

                            if(allowed.contains(item.getContentType())){
                                File file = new File(storeDir, item.getName());
                                item.write(file);
                                uploaded.add(file);
                            }
                         }
                    }
                }else{
            throw new Exception(storeDir + "is not a directory");
        }
        }else{
            throw new Exception(tmpDir + "is not a directory");
        }
        return uploaded;
    }

    


}
