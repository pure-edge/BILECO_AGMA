/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bileco_agma.model;

import java.sql.Connection;
import java.sql.SQLException;

public class DAOFactory {
    private Connection connection;
    
    public void beginConnectionScope() {
        connection = ConnectionFactory.getConnection();
    }
    
    public void endConnectionScope() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                /*log or print or ignore*/
            }
        }
    }
    
    public MemberConsumerDAO createMemberConsumerDAO() {
        return new MemberConsumerDAO(connection);
    }

    public TicketDAO createTicketDAO() {
        return new TicketDAO(connection);
    }
}
