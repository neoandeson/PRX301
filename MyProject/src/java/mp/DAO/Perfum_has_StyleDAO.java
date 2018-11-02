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
import mp.utils.DBUtil;

/**
 *
 * @author ASUS
 */
public class Perfum_has_StyleDAO implements Serializable{
    public boolean insert(int perfumeId, int styleId) throws SQLException, NamingException{
        Connection con = null;
        PreparedStatement smt = null;
        ResultSet rs = null;
        
        try {
            con = DBUtil.createConnection();
            String sql = "INSERT INTO Perfume_has_Style "
                    + "(PerfumeId, StyleId) "
                    + "VALUES (?, ?)";
            smt = con.prepareStatement(sql);
            smt.setInt(1, perfumeId);
            smt.setInt(2, styleId);
            int result = smt.executeUpdate();
            
            if(result > 0) {
                System.out.println("Perfume_has_Style DAO inserted");
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
    
    public boolean isExist(int perfumeId, int styleId) throws SQLException, NamingException{
        Connection con = null;
        PreparedStatement smt = null;
        ResultSet rs = null;
        
        try {
            con = DBUtil.createConnection();
            String sql = "SELECT PerfumeId FROM Perfume_has_Style WHERE PerfumeId=? AND StyleId=?";
            smt = con.prepareStatement(sql);
            smt.setInt(1, perfumeId);
            smt.setInt(2, styleId);
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
}
