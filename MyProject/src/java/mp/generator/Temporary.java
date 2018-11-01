/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mp.generator;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ASUS
 */
public class Temporary {
    public static void main(String[] args) {
        try {
            
            String s1 = "â";
            byte[] bytes = s1.getBytes("UTF-8");
            String s2 = new String(bytes, "UTF-8");
            System.out.println("result: " + s2);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Temporary.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
