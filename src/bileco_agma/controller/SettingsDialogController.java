/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bileco_agma.controller;

import bileco_agma.model.DAOFactory;
import bileco_agma.model.TicketDAO;
import bileco_agma.view.SettingsDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author Balo Family
 */
public class SettingsDialogController {
    private MainScreenController2 msController2;
    private SettingsDialog settingsDialog;
    private final DAOFactory daoFactory;
    private final TicketDAO ticketDAO;
    private JTextField maxNumTicketsTextfield;
    private final JTextField animationDelayTextfield;
    
    public SettingsDialogController(MainScreenController2 msc2, SettingsDialog dialog) {
        msController2 = msc2;
        settingsDialog = dialog;
        daoFactory = new DAOFactory();
        daoFactory.beginConnectionScope();
        ticketDAO = daoFactory.createTicketDAO();
        
        maxNumTicketsTextfield = settingsDialog.getMaxNumTicketsTextfield();
        try {
            maxNumTicketsTextfield.setText(
                    String.valueOf(ticketDAO.getTicketListSize()));
        } catch (SQLException ex) {
            Logger.getLogger(SettingsDialogController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        animationDelayTextfield = settingsDialog.getAnimationDelayTextfield();
        animationDelayTextfield.setText(String.valueOf(msController2.getAnimationDelay()));
        
        JButton cancelBtn = settingsDialog.getCancelBtn();
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settingsDialog.dispose();
            }
        });
        
        JButton okBtn = settingsDialog.getOKBtn();
        okBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String animationDelayStr = animationDelayTextfield.getText();
                int animationDelay = Integer.parseInt(animationDelayStr);
                
                msController2.getNonSelectedTickets();
                msController2.setAnimationDelay(animationDelay);
                
                settingsDialog.dispose();
            }
        });
        
        JButton resetRaffleDrawBtn = settingsDialog.getResetRaffleDrawBtn();
        resetRaffleDrawBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choiceNo = JOptionPane.showConfirmDialog(settingsDialog, 
                            "All raffle draw changes will be erased. Are you sure?", 
                            "Reset Raffle Draw?",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE);
                if (choiceNo == 0) { // if yes
                    try {
                        ticketDAO.deleteAllRaffleTickets();
                        ticketDAO.deleteAllRaffleTicketWinners();
                        JOptionPane.showMessageDialog(settingsDialog,
                                "Raffle draw will initialize.",
                                "Reset Finished",
                                JOptionPane.INFORMATION_MESSAGE);
                        maxNumTicketsTextfield.setText(
                                String.valueOf(ticketDAO.getTicketListSize()));
                        initializeRaffleDraw();
                        // TODO: retrieve tickets again for raffle draw
                    } catch (SQLException ex) {
                        Logger.getLogger(SettingsDialogController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    }

    public void run() {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SettingsDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SettingsDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SettingsDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SettingsDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                settingsDialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                       settingsDialog.dispose();
                    }
                });
                settingsDialog.setVisible(true);
            }
        });
    }
    
    private void initializeRaffleDraw() throws SQLException {
        int ticketCount = 0;
        String ticketCountStr = JOptionPane.showInputDialog(null,
                "Max. number of tickets: (maxvalue=99999)",
                "Initialize Raffle Draw",
                JOptionPane.QUESTION_MESSAGE);
        try {
            ticketCount = Integer.parseInt(ticketCountStr);
        } catch (NumberFormatException e) {
            System.exit(1);
        }
        ticketDAO.createEntries(ticketCount);
        maxNumTicketsTextfield.setText(
                                String.valueOf(ticketDAO.getTicketListSize()));
        JOptionPane.showMessageDialog(settingsDialog, 
                "E-Raffle Draw is ready to use.", 
                "Initialization Success!",
                JOptionPane.INFORMATION_MESSAGE);     
    }
}
