/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bileco_agma;

import bileco_agma.controller.MainScreenController2;
import bileco_agma.view.MainScreen;

/**
 *
 * @author Balo Family
 */
public class Main {
    public static void main(String[] args) {
        new MainScreenController2(new MainScreen()).run();
    }
}
