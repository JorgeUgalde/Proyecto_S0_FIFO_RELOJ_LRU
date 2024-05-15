/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto;

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;

/**
 *
 * 
 */
public class ThreadColor extends Thread {
    
    private JButton b1;
    private JButton b2;
    private Color color;
    
    public ThreadColor(JButton b1, JButton b2, Color color) {
        this.b1 = b1;
        this.b2 = b2;
        this.color = color;
        start();
    }

    /**
     * This method change the color of the buttons that change from Virtual to RAM
     * Green for the page selected to enter in RAM(virtual to RAM)
     * Blue for the page selected to enter in virtual(RAM to Virtual)
     */
    @Override
    public void run() {
        b1.setBackground(Color.BLUE);
        b2.setBackground(Color.GREEN);
        
        
        try {
            sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(ThreadColor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        b1.setBackground(color);
        b2.setBackground(color);
    }
    
    
}
