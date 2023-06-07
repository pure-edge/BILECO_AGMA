/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bileco_agma.model;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Balo Family
 */
public class UpdateRaffleTicketStatus {
    public static void main(String[] args) {
        DAOFactory dAOFactory = new DAOFactory();
        dAOFactory.beginConnectionScope();
        
        int ticketNumFirst = 1;
        int ticketNumLast = 5;
        TicketDAO dao = dAOFactory.createTicketDAO();
        try {
            //dao.createEntries(5000);
            
            /*dao.setTicketNumberAsSelected(101, 200);
            dao.setTicketNumberAsSelected(331, 400);
            dao.setTicketNumberAsSelected(701, 800);
            dao.setTicketNumberAsSelected(1162, 1200);*/
            
            /*dao.setTicketNumberAsSelected(81, 100);
            dao.setTicketNumberAsSelected(256, 300);
            dao.setTicketNumberAsSelected(528, 600);
            dao.setTicketNumberAsSelected(822, 900);
            dao.setTicketNumberAsSelected(999, 1000);
            dao.setTicketNumberAsSelected(1225, 1300);
            dao.setTicketNumberAsSelected(1522, 1600);
            dao.setTicketNumberAsSelected(1776, 1800);*/
            
            /*dao.setTicketNumberAsSelected(1407, 1500);*/
            
            /*dao.setTicketNumberAsSelected(662, 700);
            dao.setTicketNumberAsSelected(1657, 1660);
            dao.setTicketNumberAsSelected(1665, 1698);*/
            dao.updateID();
            
            dAOFactory.endConnectionScope();
        } catch (SQLException ex) {
            Logger.getLogger(UpdateRaffleTicketStatus.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
