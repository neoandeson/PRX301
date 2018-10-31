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
import mp.Utils.DBUtil;
import mp.generatedObj.Perfume;

/**
 *
 * @author ASUS
 */
public class PerfumeDAO implements Serializable{
    public boolean insert(Perfume perfume) throws SQLException, NamingException{
        Connection con = null;
        PreparedStatement smt = null;
        ResultSet rs = null;
        
        try {
            con = DBUtil.makeConnection();
            String sql = "INSERT INTO Perfume "
                    + "(Brand, Name, Sex, Origin, Price, ImageURL,"
                    + " Type, Concentration, Release, Incense,"
                    + " Style, Description) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,)";
            smt = con.prepareStatement(sql);
            smt.setString(1, perfume.getProduct().getBrand());
            smt.setString(2, perfume.getProduct().getName());
            smt.setBoolean(3, perfume.getProduct().isSex());
            smt.setString(4, perfume.getProduct().getOrigin());
            smt.setInt(5, perfume.getProduct().getPrice().intValue());
            smt.setString(6, perfume.getProduct().getImageURL());
            smt.setString(7, perfume.getType());
            smt.setString(8, perfume.getConcentration());
            smt.setInt(9, perfume.getRelease());
            smt.setString(10, perfume.getIncense());
            smt.setString(11, perfume.getStyle());
            smt.setString(12, perfume.getDescription());
            int result = smt.executeUpdate();
            
            if(result > 0) {
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
    
    public boolean isExist(String brand, String name, boolean sex, int price, int release, String incense) throws SQLException, NamingException{
        Connection con = null;
        PreparedStatement smt = null;
        ResultSet rs = null;
        
        try {
            con = DBUtil.makeConnection();
            String sql = "SELECT Brand, Name FROM Perfume WHERE Brand=? AND Name=? AND sex=? AND price=? AND Release=? AND Incense = ?";
            smt = con.prepareStatement(sql);
            smt.setString(1, brand);
            smt.setString(2, name);
            smt.setBoolean(3, sex);
            smt.setInt(4, price);
            smt.setInt(5, release);
            smt.setString(6, incense);
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
