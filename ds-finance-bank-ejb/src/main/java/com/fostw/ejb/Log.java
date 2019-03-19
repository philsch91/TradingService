/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fostw.ejb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author A4938
 */
public class Log {
    
    Path path=null;
    private Log(Path filePath){
        this.path=filePath;
    }
    
    public static Log Open(String filename){
        Path path=Paths.get(filename);
        if(!Files.exists(path, LinkOption.NOFOLLOW_LINKS)){
            try {
                Files.createFile(path);
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
        }
        if(!Files.isWritable(path)){
            return null;
        }
        Log log = new Log(path);
        return log;
    }
        
    public Boolean write(String text){
        try {
            Files.write(this.path, text.getBytes(),StandardOpenOption.APPEND);
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
        /*
        try {
            this.writer = new PrintWriter(this.filename,"UTF-8");
        } catch (FileNotFoundException fe) {
            //System.out.println(fe.getMessage());
            //System.out.println(fe.getLocalizedMessage());
            fe.printStackTrace();
            return false;
        } catch (UnsupportedEncodingException ue) {
            //System.out.println(ue.getMessage());
            ue.printStackTrace();
            return false;
        }
        this.writer.println(text);
        this.writer.close();
        */
        return true;
    }
}
