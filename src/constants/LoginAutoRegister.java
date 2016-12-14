/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package constants;

import client.LoginCrypto;
import tools.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author 몽키프
 */
public class LoginAutoRegister {
    
    public static void registerAccount(String name, String password) {
        Connection connect = DatabaseConnection.getConnection();
        PreparedStatement query = null;
        try {
           query = connect.prepareStatement("INSERT INTO accounts (name, password) VALUES (?, ?)", DatabaseConnection.RETURN_GENERATED_KEYS);
           query.setString(1, name);
           query.setString(2, LoginCrypto.hexSha1(password));
           query.executeUpdate();
        } catch (Exception error) {
                System.err.println("[1] Register Exception Error : " + error);
        } finally {
            try {
                if (query != null) {
                    query.close();
                }
            } catch (Exception E) {
                System.err.println("[2] Register Exception Error : " + E);
            }
        }
    }

    public static boolean checkIP(String ip) {
        Connection connect = DatabaseConnection.getConnection();
        PreparedStatement query = null;
        ResultSet result = null;

        try {
           query = connect.prepareStatement("SELECT SessionIP FROM accounts WHERE SessionIP = ?");
           query.setString(1, ip);
           result = query.executeQuery();
           if (result.getRow() < 3) {
               return true;
           }
        } catch (Exception error) {
                System.err.println("[1] Register Exception Error : " + error);
        } finally {
            try {
                if (query != null) {
                    query.close();
                }
                if (result != null) {
                    result.close();
                }
            } catch (Exception E) {
                System.err.println("[2] Register Exception Error : " + E);
            }
        }
        return false;
    }        
}
