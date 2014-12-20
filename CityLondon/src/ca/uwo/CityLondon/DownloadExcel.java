/*
 * download file from given url to eclipse workplace
 * return download file
 */

package ca.uwo.CityLondon;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URL;
import java.net.URLConnection;

public class DownloadExcel {

	public static File downLoadFile(String category, URL url) {
        //***obsolete***URL url;
        File file = null;
        
        try {
        	
        	// First set the default cookie manager.
        	CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
        	
        		//connect to given url then get input stream (excel file)
                URLConnection uc = url.openConnection();
                InputStream is = uc.getInputStream();

                //format file name as category name
                file = new File(category);
                FileOutputStream out = new FileOutputStream(file);

                //download file from input stream then write to output stream
                int i = 0;
                while ((i = is.read()) != -1) {
                        out.write(i);
                }
                out.flush();
                
                //close connection
                is.close();
                } catch (Exception e) {
                e.printStackTrace();
                }

        return file;
        }

}

