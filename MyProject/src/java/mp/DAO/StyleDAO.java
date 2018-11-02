/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mp.DAO;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.NamingException;
import mp.generatedObj.Style;
import mp.utils.DBUtil;

/**
 *
 * @author ASUS
 */
public class StyleDAO implements Serializable{
    public boolean insert(Style style) throws SQLException, NamingException{
        Connection con = null;
        PreparedStatement smt = null;
        ResultSet rs = null;
        
        try {
            con = DBUtil.createConnection();
            String sql = "INSERT INTO Style "
                    + "(Content) "
                    + "VALUES (?)";
            smt = con.prepareStatement(sql);
            smt.setString(1, style.getContent());
            int result = smt.executeUpdate();
            
            if(result > 0) {
                System.out.println("Style DAO inserted");
                return true;
            }
        } finally {
            if(rs != null) {
                rs.close();
            }
            if(smt != null) {
                smt.close();
            }
            if(con != null) {
                con.close();
            }
        }
        return false;
    }
    
    public boolean isExist(String content) throws SQLException, NamingException{
        Connection con = null;
        PreparedStatement smt = null;
        ResultSet rs = null;
        
        try {
            con = DBUtil.createConnection();
            String sql = "SELECT Content FROM Style WHERE Content=?";
            smt = con.prepareStatement(sql);
            smt.setString(1, content);
            rs = smt.executeQuery();
            
            if(rs.next()) {
                return true;
            }
        } finally {
            if(rs != null) {
                rs.close();
            }
            if(smt != null) {
                smt.close();
            }
            if(con != null) {
                con.close();
            }
        }
        return false;
    }
    
    public int getId(String content) throws SQLException, NamingException{
        Connection con = null;
        PreparedStatement smt = null;
        ResultSet rs = null;
        
        try {
            con = DBUtil.createConnection();
            String sql = "SELECT Id FROM Style WHERE Content=?";
            smt = con.prepareStatement(sql);
            smt.setString(1, content);
            rs = smt.executeQuery();
            
            if(rs.next()) {
                return rs.getInt("Id");
            }
        } finally {
            if(rs != null) {
                rs.close();
            }
            if(smt != null) {
                smt.close();
            }
            if(con != null) {
                con.close();
            }
        }
        return 0;
    }
}
