/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package proyecto;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Vector;
import javax.swing.JButton;

/**
 *
 * @author jorge
 */
public class VistaFIFO extends javax.swing.JDialog {

    private LinkedList<Page> ramList;
    private LinkedList<Page> virtualList;
    private Color noColor;

    /**
     * Creates new form VistaFIFO
     */
    public VistaFIFO(java.awt.Frame parent, boolean modal, int pages) throws InterruptedException {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(parent);
        ramList = new LinkedList();
        virtualList = new LinkedList();
        addLists(pages);
    }

    /**
     * This method add the pages to the RAMList and if (pages > 8) the method
     * add the remaining pages to the VirtialList (randomly)
     *
     * @param pages
     * @throws InterruptedException
     */
    private void addLists(int pages) throws InterruptedException {
        int pageNumber;
        Vector<Integer> vector = new Vector(pages);
        jTextArea1.setText("Utilizando algoritmo FIFO");

        for (int i = 1; i <= 8; i++) {
            pageNumber = (int) (Math.random() * pages + 1);
            while (vector.contains(pageNumber)) {
                pageNumber = (int) (Math.random() * pages + 1);
            }
            vector.add(pageNumber);
            ramList.add(new Page(pageNumber, 0));
            Thread.sleep(5);
        }
        for (int i = 9; i <= pages; i++) {
            pageNumber = (int) (Math.random() * pages + 1);
            while (vector.contains(pageNumber)) {
                pageNumber = (int) (Math.random() * pages + 1);
            }
            vector.add(pageNumber);
            virtualList.add(new Page(pageNumber, 0));
            Thread.sleep(5);
        }
        addPanel();
    }

    /**
     * This method get buttons from the vectors(RAMList & VirtualList) then add
     * panels to the main frame
     */
    public void addPanel() {
        jpMarcoPagina.setLayout(new GridLayout(9, 1, 5, 5));
        jpRam.setLayout(new GridLayout(ramList.size() + 1, 1, 5, 5));
        jpVirtual.setLayout(new GridLayout(virtualList.size(), 1, 5, 5));

        JButton button;
        Label label;

        jpMarcoPagina.add(new Label("Marco de pagina"));
        jpRam.add(new Label("Proceso"));

        for (int i = 0; i < ramList.size(); i++) {
            //MARCO DE PAGINA
            label = new Label(i + "");
            jpMarcoPagina.add(label);
            //NUMERO DE PAGINA
            String nPag = ramList.get(i).getPageNumber() + "";
            button = new JButton(nPag);
            button.setName(nPag);
            addRAM(button);
            noColor = button.getBackground();
        }

        for (int i = 0; i < virtualList.size(); i++) {
            String nPag = virtualList.get(i).getPageNumber() + "";
            button = new JButton(nPag);
            button.setName(nPag);
            addVirtual(button);
        }
    }

    /**
     * this method add the buttons to the RAMList
     * @param button 
     */
    private void addRAM(JButton button) {
        jpRam.add(button);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton bPress = (JButton) e.getSource();
                addText("\nLa pagina " + bPress.getText() + " fue utilizada");
            }
        });
    }
    
    /**
     * this method add the buttons to the VirtualList
     * @param button 
     */
    private void addVirtual(JButton button) {
     
        jpVirtual.add(button);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton bPress = (JButton) e.getSource();
                addText("\nLa pagina " + bPress.getName() + " fue seleccionada y hubo fallo de pagina");
                modify(bPress.getText());
            }
        });
    }

    /**
     * This method write the processes of the FIFO in the text area
     *
     * @param txt
     */
    private void addText(String txt) {
        jTextArea1.setText(jTextArea1.getText() + txt);
    }

    
     /**
     * This method modify the pages of the virtual list and assign the page to
     * the RAM
     *
     * @param pageNumber
     */
    private void modify(String pageNumber) {
        JButton virtual = null;
        for (Component component : jpVirtual.getComponents()) {
            if (component instanceof JButton jButton) {
                if (component.getName().equals(pageNumber)) {
                    virtual = jButton;
                    break;
                }
            }
        }

        JButton RAM = (JButton) jpRam.getComponent(1);
        addText("\nLa pagina " + RAM.getName() + " fue seleccionada para el intercambio");
        addText("\nSe intercambio la pagina " + RAM.getName() + " con la pagina " + virtual.getName());
        addText("\nLa pagina " + virtual.getName() + " fue utilizada");
        
        ThreadColor threadColor = new ThreadColor(RAM, virtual, noColor);
        
        jpRam.remove(RAM);
        jpVirtual.remove(virtual);
        
        
        jpRam.revalidate();
        jpVirtual.revalidate();

        virtual.removeActionListener(virtual.getActionListeners()[0]);
        RAM.removeActionListener(RAM.getActionListeners()[0]);

        addRAM(virtual);
        addVirtual(RAM);
       

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpRam = new javax.swing.JPanel();
        jpVirtual = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jpMarcoPagina = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jpRam.setBackground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout jpRamLayout = new javax.swing.GroupLayout(jpRam);
        jpRam.setLayout(jpRamLayout);
        jpRamLayout.setHorizontalGroup(
            jpRamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 93, Short.MAX_VALUE)
        );
        jpRamLayout.setVerticalGroup(
            jpRamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 188, Short.MAX_VALUE)
        );

        jpVirtual.setBackground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout jpVirtualLayout = new javax.swing.GroupLayout(jpVirtual);
        jpVirtual.setLayout(jpVirtualLayout);
        jpVirtualLayout.setHorizontalGroup(
            jpVirtualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jpVirtualLayout.setVerticalGroup(
            jpVirtualLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 15, Short.MAX_VALUE)
        );

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jLabel1.setText("VIRTUAL");

        jLabel2.setText("RAM");

        jpMarcoPagina.setBackground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout jpMarcoPaginaLayout = new javax.swing.GroupLayout(jpMarcoPagina);
        jpMarcoPagina.setLayout(jpMarcoPaginaLayout);
        jpMarcoPaginaLayout.setHorizontalGroup(
            jpMarcoPaginaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jpMarcoPaginaLayout.setVerticalGroup(
            jpMarcoPaginaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 388, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(jpMarcoPagina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jpRam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                        .addComponent(jpVirtual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel1)))
                .addGap(76, 76, 76))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jpRam, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jpMarcoPagina, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jpVirtual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(107, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        getParent().setVisible(true);
    }//GEN-LAST:event_formWindowClosed

    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JPanel jpMarcoPagina;
    private javax.swing.JPanel jpRam;
    private javax.swing.JPanel jpVirtual;
    // End of variables declaration//GEN-END:variables
}
