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
import java.time.LocalTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Vector;
import javax.swing.JButton;

public class VistaLRU extends javax.swing.JDialog {

    /**
     * Creates new form VistaLRU
     */
    private LinkedList<Page> ramList;
    private LinkedList<Page> virtualList;
    private Color noColor;

    public VistaLRU(java.awt.Frame parent, boolean modal, int pages) throws InterruptedException {
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
        Vector vector = new Vector(pages);
        jTextArea1.setText("Utilizando algoritmo LRU");

        for (int i = 1; i <= 8; i++) {
            pageNumber = (int) (Math.random() * pages + 1);
            while (vector.contains(pageNumber)) {
                pageNumber = (int) (Math.random() * pages + 1);
            }
            vector.add(pageNumber);
            ramList.add(new Page(i, pageNumber, LocalTime.now().toString()));
            Thread.sleep(5);
        }
        for (int i = 9; i <= pages; i++) {
            pageNumber = (int) (Math.random() * pages + 1);
            while (vector.contains(pageNumber)) {
                pageNumber = (int) (Math.random() * pages + 1);
            }

            vector.add(pageNumber);
            virtualList.add(new Page(pageNumber, LocalTime.now().toString()));
            Thread.sleep(5);
        }

        addPanel();
    }

    /**
     * This method get buttons from the vectors(RAMList & VirtualList) then add
     * panels to the main frame
     */
    public void addPanel() {

        jpRam.setLayout(new GridLayout(ramList.size() + 1, 3, 5, 5));
        jpVirtual.setLayout(new GridLayout(virtualList.size(), 3, 5, 5));

        JButton button;
        Label label;

        jpRam.add(new Label("Marco de pagina"));
        jpRam.add(new Label("Proceso"));
        jpRam.add(new Label("Tiempo - Orden"));

        for (int i = 0; i < ramList.size(); i++) {

            //MARCO DE PAGINA
            label = new Label(ramList.get(i).getPageFrame() + "");

            jpRam.add(label);

            //NUMERO DE PAGINA
            String nPag = ramList.get(i).getPageNumber() + "";
            button = new JButton(nPag);
            button.setName(nPag);

            noColor = button.getBackground();

            jpRam.add(button);

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JButton bPress = (JButton) e.getSource();
//                    addText("\nLa pagina " + bPress.getText() + " fue seleccionada " );
                    modifyTime(Integer.parseInt(bPress.getName()));
                }
            });

            //COMENTARIOS
            jpRam.add(new Label(ramList.get(i).getComments() + ", posicion #" + i));
        }

        for (int i = 0; i < virtualList.size(); i++) {
            String nPag = virtualList.get(i).getPageNumber() + "";
            button = new JButton(nPag);
            button.setName(nPag);

            jpVirtual.add(button);

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JButton bPress = (JButton) e.getSource();
                    addText("\nLa pagina " + bPress.getName() + " fue seleccionada y hubo fallo de pagina");
                    modify(Integer.parseInt(bPress.getText()));
                }
            });
        }

    }

    /**
     * This method write the processes of the LRU in the text area
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
    private void modify(int pageNumber) {

        Page page1 = ramList.getFirst();
        LocalTime oldTime = LocalTime.parse(page1.getComments().split(",")[0]);
        LocalTime actualTime;

        for (int i = 1; i < ramList.size(); i++) {
            actualTime = LocalTime.parse(ramList.get(i).getComments().split(",")[0]);
            if (actualTime.compareTo(oldTime) < 0) {
                oldTime = actualTime;
                page1 = ramList.get(i);
            }
        }

        int nComp = 0;
        JButton ram = null;
        JButton virtual = null;

        for (Component componenteR : jpRam.getComponents()) {

            if (componenteR instanceof JButton && Integer.parseInt(componenteR.getName()) == page1.getPageNumber()) {
                ram = (JButton) componenteR;

                for (Component componenteV : jpVirtual.getComponents()) {
                    if (componenteV instanceof JButton && Integer.parseInt(componenteV.getName()) == pageNumber) {
                        virtual = (JButton) componenteV;

                        String nameR = ram.getName();
                        String nameV = virtual.getName();
                        ram.setName(nameV);
                        virtual.setName(nameR);

                        ram.setText(nameV);
                        virtual.setText(nameR);

                        for (int i = 0; i < virtualList.size(); i++) {
                            Page pagV = virtualList.get(i);
                            if (pagV.getPageNumber() == pageNumber) {
                                ramList.remove(page1);
                                ramList.addLast(pagV);

                                virtualList.remove(pagV);
                                virtualList.addLast(page1);

                                pagV.setPageFrame(page1.getPageFrame());
                                page1.setPageFrame(-1);
                                break;
                            }
                        }

                        addText("\nLa pagina " + virtual.getName() + " fue seleccionada para el intercambio");
                        addText("\nSe intercambio la pagina " + virtual.getName() + " con la pagina " + ram.getName());
                        modifyTime(Integer.parseInt(ram.getName()));
                        new ThreadColor(virtual, ram, noColor);
                        return;
                    }
                }
            }
            nComp++;
        }
    }

    /**
     * This class implements the class Comparator 
     * Is used for sort by time the pages
     */
    static class SortByTime implements Comparator<Page> {

        @Override
        public int compare(Page o1, Page o2) {
            return o1.getComments().split(",")[0].compareTo(o2.getComments().split(",")[0]);
        }
    }

    /**
     * This method change the identifier of each page
     * If the page was be used lastly then the number will be 7
     * The id 0 will be change to the VirtualList 
     */
    private void orderPages() {
        Collections.sort(ramList, new SortByTime());
        Page pagina;
        int nComp = 0;

        for (int i = 0; i < ramList.size(); i++) {

            pagina = ramList.get(i);
            pagina.setComments(pagina.getComments().split(",")[0] + ", posicion #" + i);
            for (Component componente : jpRam.getComponents()) {

                if (componente instanceof JButton && Integer.parseInt(componente.getName()) == pagina.getPageNumber()) {
                    Label label = (Label) jpRam.getComponent(nComp + 1);
                    label.setText(pagina.getComments());
                }
                nComp++;
            }
            nComp = 0;
        }
        return;
    }

    /**
     * This method change the allotment time of the pages in the frame
     *
     * @param nPag
     */
    private void modifyTime(int nPag) {
        Page pagina;
        int nComp = 0;

        for (int i = 0; i < ramList.size(); i++) {
            pagina = ramList.get(i);
            if (pagina.getPageNumber() == nPag) {
                String time = LocalTime.now().toString();
                pagina.setComments(time);
                for (Component component : jpRam.getComponents()) {

                    if (component instanceof JButton && Integer.parseInt(component.getName()) == nPag) {
                        Label label = (Label) jpRam.getComponent(nComp + 1);
                        addText("\nLa pagina " + component.getName()+ " utilizada a las " + time );
                        label.setText(time);
                        orderPages();
                        return;
                    }
                    nComp++;
                }
                return;
            }
        }
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
            .addGap(0, 100, Short.MAX_VALUE)
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
            .addGap(0, 29, Short.MAX_VALUE)
        );

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jLabel1.setText("VIRTUAL");

        jLabel2.setText("RAM");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(77, 77, 77)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jpRam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jpVirtual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(79, 79, 79))
            .addGroup(layout.createSequentialGroup()
                .addGap(197, 197, 197)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 369, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(217, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jLabel1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jpRam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jpVirtual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(125, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        getParent().setVisible(true);        // TODO add your handling code here:
    }//GEN-LAST:event_formWindowClosed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JPanel jpRam;
    private javax.swing.JPanel jpVirtual;
    // End of variables declaration//GEN-END:variables
}
