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
public class Ticket {
    private int number;
    private boolean isSelected;

    public Ticket(int number, boolean isSelected) {
        this.number = number;
        this.isSelected = isSelected;
    }
    
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
