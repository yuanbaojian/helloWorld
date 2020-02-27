package com.ybj.utils.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class StreamHandler extends Thread{
	InputStream is;  
    OutputStream os;  

    public StreamHandler(InputStream is) {  
        this.is = is;  
    }  

    public void run() {  
        InputStreamReader isr = null;  
        BufferedReader br = null;  
        try {  
            isr = new InputStreamReader(is);  
            br = new BufferedReader(isr);  
            String line=null;  
            while ( (line = br.readLine()) != null) {  
//            	System.out.println(line);
            }  
            
        } catch (IOException ioe) {  
            ioe.printStackTrace();    
        } finally{  
            try {  
                br.close();  
                isr.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
}
