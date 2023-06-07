/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bileco_agma.model;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 *
 * @author Balo Family
 */
public class MemberConsumerDAO {
    private Connection connection;
    private Statement statement;

    MemberConsumerDAO(Connection connection) {
        this.connection = connection;
    }
    
    public void createThounsandDummyEntries() throws SQLException {
        for (int i = 0; i < 10000; i++) {
            String query = "INSERT INTO registered_member_consumers (name) VALUES('Stephen " + i + "')";
            System.out.println("entry" + i);
            statement = connection.createStatement();
            statement.execute(query);
        }
        
        if (statement != null) { 
            statement.close();
        }
    }
    
    public ArrayList<MemberConsumer> getNonSelectedConsumersList() throws SQLException {
        String query = "SELECT ticket_no, name from registered_member_consumers WHERE isSelected = 0";
        ResultSet resultSet;
        
        statement = connection.createStatement();
        resultSet = statement.executeQuery(query);
        
        ArrayList<MemberConsumer> list = new ArrayList<>();
        MemberConsumer mc;
        while (resultSet.next()) {
            int ticketNo = resultSet.getInt("ticket_no");
            String name = resultSet.getString("name");
            
            mc = new MemberConsumer(ticketNo, name);
            list.add(mc);
        }
        
        if (statement != null) { 
            statement.close(); 
        }
        
        return list;
    }

    public void markMemberConsumerSelected(int ticketNum) throws SQLException {
        String query = "UPDATE registered_member_consumers SET isSelected = 1 WHERE ticket_no = " + ticketNum + " LIMIT 1";
        
        statement = connection.createStatement();
        statement.executeUpdate(query);
        
        if (statement != null) { 
            statement.close();
        }
    }
}
