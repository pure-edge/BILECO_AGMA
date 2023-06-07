/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bileco_agma.controller;

import bileco_agma.model.AudioPlayer;
import bileco_agma.model.DAOFactory;
import bileco_agma.model.Ticket;
import bileco_agma.model.TicketDAO;
import bileco_agma.view.MainScreen;
import bileco_agma.view.SettingsDialog;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 *
 * @author Balo Family
 */
public class MainScreenController2 {
    private final MainScreen mainScreen;
    private final JButton drawRaffleTicketBtn;
    private String ticketNumStr;
    private ArrayList<Ticket> tickets;
    private final DAOFactory daoFactory;
    private final TicketDAO ticketDAO;
    private int millisec_animationDelay;
    
    private final int MILLISEC_DEFAULT_DELAY = 3000;
    
    public MainScreenController2(MainScreen ms) {
        millisec_animationDelay = MILLISEC_DEFAULT_DELAY;
        mainScreen = ms;
        daoFactory = new DAOFactory();
        daoFactory.beginConnectionScope();
        ticketDAO = daoFactory.createTicketDAO();
                    
        drawRaffleTicketBtn = mainScreen.getDrawRaffleTicketBtn();
        drawRaffleTicketBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String audioFilePath = "/bileco_agma/audio/raffle_selection.wav";
                    final AudioPlayer player = new AudioPlayer(audioFilePath);
                    player.play(AudioPlayer.LOOP_CONTINUOUSLY);
                    
                    int ticketCount = tickets.size();
                    System.out.println("listCount: " + ticketCount);
                    SecureRandom rndm = new SecureRandom();
                    int selectedListNo = rndm.nextInt(ticketCount);
                    
                    Ticket selectedTicket = tickets.get(selectedListNo);
                    int ticketNum = selectedTicket.getNumber();
                    
                    ticketDAO.markTicketAsSelected(ticketNum); // TODO: observe this line
                    ticketDAO.addRaffleTicketWinner(ticketNum);
                    tickets.remove(selectedListNo);
                    
                    ticketNumStr = String.format("%05d", ticketNum);
                    System.out.println("ticketNumStr: " + ticketNumStr);
                    
                    mainScreen.unhighlightDigits();
                    // TODO: disable button and change its apperance
                    drawRaffleTicketBtn.setText("DRAWING...");
                    drawRaffleTicketBtn.setEnabled(false);
                    drawRaffleTicketBtn.setBackground(new Color(39, 242, 59));
                    drawRaffleTicketBtn.setIcon(null);
                    
                    //spinRaffleSlot(5, millisec_animationDelay);
                    //spinRaffleSlot(4, millisec_animationDelay * 2);
                    spinRaffleSlot(3, millisec_animationDelay * 1);
                    spinRaffleSlot(2, millisec_animationDelay * 2);
                    spinRaffleSlot(1, millisec_animationDelay * 3);
                    
                    Timer t1 = new Timer(millisec_animationDelay * 3 + 1000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            player.stop();
                        }
                    });
                    t1.setRepeats(false);
                    t1.start();
                    
                    Timer t2 = new Timer(millisec_animationDelay * 3 + 2000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            drawRaffleTicketBtn.setEnabled(true);
                            drawRaffleTicketBtn.setBackground(new Color(25, 71, 150));
                            drawRaffleTicketBtn.setIcon(new javax.swing.ImageIcon(
                                    getClass().getResource("/bileco_agma/images/ic_repeat.png"))); // NOI18N
                            drawRaffleTicketBtn.setText("DRAW AGAIN");
                        }
                    });
                    t2.setRepeats(false);
                    t2.start();
                    // TODO: blink raffle slots
                } catch (SQLException ex) {
                    Logger.getLogger(MainScreenController2.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    ex.printStackTrace(pw);
                    JOptionPane.showMessageDialog(mainScreen, sw.toString(), "Exception ex", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        JButton settingsBtn = mainScreen.getSettingsBtn();
        settingsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SettingsDialog dialog = new SettingsDialog(mainScreen, true);
                new SettingsDialogController(MainScreenController2.this, dialog).run();
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
                try {
                    mainScreen.setVisible(true);
                    
                    int listSize = ticketDAO.getTicketListSize();
                    if (listSize == 0) {
                        initializeRaffleDraw();
                    }
                    
                    tickets = ticketDAO.getNonSelectedTickets();
                } catch (SQLException ex) {
                    Logger.getLogger(MainScreenController2.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null, ex.toString(), 
                            "SQLException", JOptionPane.ERROR_MESSAGE);
                }
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
        
        JOptionPane.showMessageDialog(mainScreen, 
                "E-Raffle Draw is ready to use.", 
                "Initialization Success!",
                JOptionPane.INFORMATION_MESSAGE);        
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
                    
                    String audioFilePath = "/bileco_agma/audio/selected_digit.wav";
                    AudioPlayer player = new AudioPlayer(audioFilePath);
                    player.play(0);
                } // end if
            } // end actionPerformed
        } );
        timer.start();
    }

    void getNonSelectedTickets() {
        try {
            tickets = ticketDAO.getNonSelectedTickets();
        } catch (SQLException ex) {
            Logger.getLogger(MainScreenController2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    int getAnimationDelay() {
        return millisec_animationDelay;
    }
    
    void setAnimationDelay(int millisec_delay) {
        millisec_animationDelay = millisec_delay;
    }
}
