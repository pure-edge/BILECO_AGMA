/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bileco_agma.model;

/**
 *
 * @author Balo Family
 */
public class MemberConsumer {
    private int ticketNo;
    private String name;

    public MemberConsumer(int ticketNo, String name) {
        this.ticketNo = ticketNo;
        this.name = name;
    }
    
    public int getTicketNo() {
        return ticketNo;
    }

    public void setTicketNo(int ticketNo) {
        this.ticketNo = ticketNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
