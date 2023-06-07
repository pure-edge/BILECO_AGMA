/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bileco_agma.controller;

import bileco_agma.model.DAOFactory;
import bileco_agma.model.MemberConsumer;
import bileco_agma.model.MemberConsumerDAO;
import bileco_agma.view.Congratulations;
import bileco_agma.view.MainScreen;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.Timer;

/**
 *
 * @author Balo Family
 */
/*
        trace the official who registered the member-consumer

        give feedback to add, edit, delete
        'Stephen Balo' registered successfully with ticket number '1'.

manual write and scan
advantage:
-fast and easy write
-accurate signatures

disadvantage:
-consumes many papers
-tendency for signatures to overlap
-tedious work in extracting every person's signature

pen device
advantage:
-easy retrieval of person's signature

disadvantage:
-inaccurate signatures
-takes a few minutes to save signature as image file

get user's identity (ip, pc name, or user account) for liability purposes
        */
public class MainScreenController {
    private final MainScreen mainScreen;
    private final JButton drawRaffleTicketBtn;
    private String ticketNumStr;
    private ArrayList<MemberConsumer> memberConsumers;
    private final DAOFactory daoFactory;
    private final MemberConsumerDAO mcDAO;
            
    public MainScreenController(MainScreen ms) {
        mainScreen = ms;
        daoFactory = new DAOFactory();
        daoFactory.beginConnectionScope();
        
        mcDAO = daoFactory.createMemberConsumerDAO();
        try {                    
            memberConsumers = mcDAO.getNonSelectedConsumersList();
        } catch (SQLException ex) {
            Logger.getLogger(MainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
                    
        drawRaffleTicketBtn = mainScreen.getDrawRaffleTicketBtn();
        drawRaffleTicketBtn.addActionListener(new java.awt.event.ActionListener() {
            private String selectedConsumerName;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    System.out.println("mouseClicked");
                    
                    int listCount = memberConsumers.size();
                    System.out.println("listCount: " + listCount);
                    SecureRandom rndm = new SecureRandom();
                    int selectedListNo = rndm.nextInt(listCount);
                    
                    MemberConsumer selectedConsumer = memberConsumers.get(selectedListNo);
                    int ticketNum = selectedConsumer.getTicketNo();
                    selectedConsumerName = selectedConsumer.getName();
                    
                    mcDAO.markMemberConsumerSelected(ticketNum); // TODO: observe this line
                    memberConsumers.remove(selectedListNo);
                    
                    ticketNumStr = String.format("%05d", ticketNum);
                    System.out.println("ticketNumStr: " + ticketNumStr);
                    
                    mainScreen.unhighlightDigits();
                    // TODO: disable button and change its apperance
                    drawRaffleTicketBtn.setText("DRAWING...");
                    drawRaffleTicketBtn.setEnabled(false);
                    drawRaffleTicketBtn.setBackground(new Color(39, 242, 59));
                    drawRaffleTicketBtn.setIcon(null);
                    
                    spinRaffleSlot(5, 3000);
                    spinRaffleSlot(4, 6000);
                    spinRaffleSlot(3, 9000);
                    spinRaffleSlot(2, 12000);
                    spinRaffleSlot(1, 15000);
                    
                    Timer t = new Timer(17000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // TODO: enable button and change its apperance
                            drawRaffleTicketBtn.setEnabled(true);
                            drawRaffleTicketBtn.setBackground(new Color(25, 71, 150));
                            drawRaffleTicketBtn.setIcon(new javax.swing.ImageIcon(
                                    getClass().getResource("/bileco_agma/images/ic_repeat.png"))); // NOI18N
                            drawRaffleTicketBtn.setText("DRAW AGAIN");
                            
                            Congratulations c =
                                    new Congratulations(mainScreen, true, selectedConsumerName);
                            new CongratulationsController(c).run();
                        }
                    });
                    t.setRepeats(false);
                    t.start();
                    // TODO: blink raffle slots
                } catch (SQLException ex) {
                    Logger.getLogger(MainScreenController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        mainScreen.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                daoFactory.endConnectionScope();
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
        } catch (ClassNotFoundException | InstantiationException | 
                IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                mainScreen.setVisible(true);
            }
        });
    }
    
    /*
    5-ten thousand, 4-thousands, 3-hundreds, 2-tens, 1-ones
    */
    private void spinRaffleSlot(final int placeValue, int millisec_spinDuration) {
        final int MILLISEC_DELAY_DURATION = 50;
        
        final int SPIN_RAFFLE_REPS = millisec_spinDuration / MILLISEC_DELAY_DURATION;
        final Timer timer = new Timer(MILLISEC_DELAY_DURATION, null);
        timer.addActionListener( new ActionListener() {
            int count = 0;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                SecureRandom r = new SecureRandom();
                int randomDigit = r.nextInt(10);
                String randomDigitStr = String.valueOf(randomDigit);
                
                switch(placeValue) {
                    case 5:
                        mainScreen.setTenThousandsDigit(randomDigitStr);
                        break;
                    case 4:
                        mainScreen.setThousandsDigit(randomDigitStr);
                        break;
                    case 3:
                        mainScreen.setHundredsDigit(randomDigitStr);
                        break;
                    case 2:
                        mainScreen.setTensDigit(randomDigitStr);
                        break;
                    case 1:
                        mainScreen.setOnesDigit(randomDigitStr);
                        break;
                }
                
                count++;
                
                if (count >= SPIN_RAFFLE_REPS) {
                    timer.stop();
                    
                    //after spinning, place the real digit in place
                    switch(placeValue) {
                        case 5:
                            mainScreen.highlightTenThousandsDigit();
                            mainScreen.setTenThousandsDigit(ticketNumStr.substring(0, 1));
                            break;
                        case 4:
                            mainScreen.highlightThousandsDigit();
                            mainScreen.setThousandsDigit(ticketNumStr.substring(1, 2));
                            break;
                        case 3:
                            mainScreen.highlightHundredsDigit();
                            mainScreen.setHundredsDigit(ticketNumStr.substring(2, 3));
                            break;
                        case 2:
                            mainScreen.highlightTensDigit();
                            mainScreen.setTensDigit(ticketNumStr.substring(3, 4));
                            break;
                        case 1:
                            mainScreen.highlightOnesDigit();
                            mainScreen.setOnesDigit(ticketNumStr.substring(4));
                            break;
                    }
                }
            }
        } );
        timer.start();
    }
}
