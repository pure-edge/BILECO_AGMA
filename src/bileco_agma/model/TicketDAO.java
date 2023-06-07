/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bileco_agma.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Balo Family
 */
public class TicketDAO {
    private Connection connection;
    private Statement statement;

    TicketDAO(Connection connection) {
        this.connection = connection;
    }

    public void createEntries(int count) throws SQLException {
        for (int i = 0; i < count; i++) {
            String query = "INSERT INTO raffle_tickets (is_selected) VALUES(0)";
            statement = connection.createStatement();
            statement.execute(query);
        }
        
        if (statement != null) { 
            statement.close();
        }
    }
    
    public ArrayList<Ticket> getNonSelectedTickets() throws SQLException {
        String query = "SELECT ticket_no, is_selected from raffle_tickets WHERE is_selected = 0";
        ResultSet resultSet;
        
        statement = connection.createStatement();
        resultSet = statement.executeQuery(query);
        
        ArrayList<Ticket> list = new ArrayList<>();
        Ticket ticket;
        while (resultSet.next()) {
            int ticketNo = resultSet.getInt("ticket_no");
            boolean isSelected = resultSet.getBoolean("is_selected");
            ticket = new Ticket(ticketNo, isSelected);
            
            list.add(ticket);
        }
        
        if (statement != null) { 
            statement.close(); 
        }
        
        return list;
    }

    public void markTicketAsSelected(int ticketNum) throws SQLException {
        String query = "UPDATE raffle_tickets SET is_selected = 1 WHERE ticket_no = " + ticketNum + " LIMIT 1";
        statement = connection.createStatement();
        statement.executeUpdate(query);
        
        if (statement != null) { 
            statement.close();
        }
    }

    public void addRaffleTicketWinner(int ticketNum) throws SQLException {
        String query = "INSERT INTO raffle_ticket_winners (ticket_no) VALUES(" + ticketNum + ")";
        statement = connection.createStatement();
        statement.execute(query);
        
        if (statement != null) { 
            statement.close();
        }
    }

    public int getTicketListSize() throws SQLException {
        String query = "SELECT COUNT(*) FROM raffle_tickets";
        ResultSet resultSet;
        
        statement = connection.createStatement();
        resultSet = statement.executeQuery(query);
        
        int listSize = 0;
        if (resultSet.next()) {
            listSize = resultSet.getInt("COUNT(*)");
        }
        
        return listSize;
    }

    public void deleteAllRaffleTickets() throws SQLException {
        String query = "DELETE FROM raffle_tickets";
        statement = connection.createStatement();
        statement.executeUpdate(query);
        
        query = "ALTER TABLE raffle_tickets auto_increment = 1";
        statement.execute(query);
        
        if (statement != null) { 
            statement.close();
        }
    }

    public void deleteAllRaffleTicketWinners() throws SQLException {
        String query = "DELETE FROM raffle_ticket_winners";
        statement = connection.createStatement();
        statement.executeUpdate(query);
        
        query = "ALTER TABLE raffle_ticket_winners auto_increment = 1";
        statement.execute(query);
        
        if (statement != null) { 
            statement.close();
        }
    }

    public void setTicketNumberAsSelected(int ticketNum) throws SQLException {
        String query = "UPDATE raffle_tickets SET is_selected = 1 WHERE ticket_no = " + ticketNum;
        statement = connection.createStatement();
        statement.execute(query);
        
        if (statement != null) { 
            statement.close();
        }
    }
    
    public void setTicketNumberAsSelected(int ticketNumFirst, int ticketNumLast) throws SQLException {
        for (int i = ticketNumFirst; i <= ticketNumLast; i++) {
            System.out.println("ticketNum: " + i);
            String query = "UPDATE raffle_tickets SET is_selected = 1 WHERE ticket_no = " + i;
            statement = connection.createStatement();
            statement.execute(query);
        }
        
        if (statement != null) { 
            statement.close();
        }
    }

    // TO BE REMOVED
    public void updateID() throws SQLException {
        String query = "SELECT * FROM active_consumers ORDER by id ASC";
        statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
        ResultSet result = statement.executeQuery(query);
        //result.up
        
        int i = 1;
        while (result.next()) {
            System.out.print(i + "-");
            query = i + "-" + "UPDATE active_consumers SET id = " + i + " WHERE id = " + result.getInt("id");
            System.out.println(query);
            result.updateInt("id", i);
            result.updateRow();
            //statement.execute(query);
            
            i++;
        }
        
        if (statement != null) { 
            statement.close();
        }
    }
}
