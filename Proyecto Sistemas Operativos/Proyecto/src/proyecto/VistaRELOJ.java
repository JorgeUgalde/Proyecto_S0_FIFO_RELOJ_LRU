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
 * @author jerem
 */
public class VistaRELOJ extends javax.swing.JDialog {

    private LinkedList<Page> ramList;
    private LinkedList<Page> virtualList;
    private Color noColor;
    private JButton buttonSelected = null;

    /**
     * Creates new form VistaReloj
     */
    public VistaRELOJ(java.awt.Frame parent, boolean modal, int pages) throws InterruptedException {
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
        jpModificationBit.setLayout(new GridLayout(ramList.size() + 1, 1, 5, 5));

        JButton button;
        Label label;

        jpMarcoPagina.add(new Label("Marco de pagina"));
        jpRam.add(new Label("Proceso"));
        jpModificationBit.add(new Label("Bit de modificación"));

        for (int i = 0; i < ramList.size(); i++) {
            //MARCO DE PAGINA
            label = new Label(i + "");
            jpMarcoPagina.add(label);
            //NUMERO DE PAGINA
            String nPag = ramList.get(i).getPageNumber() + "";
            button = new JButton(nPag);
            button.setName(nPag);
            jpRam.add(button);
            addRAMListener(button);
            label = new Label("0");
            jpModificationBit.add(label);
            noColor = button.getBackground();
        }

        for (int i = 0; i < virtualList.size(); i++) {
            String nPag = virtualList.get(i).getPageNumber() + "";
            button = new JButton(nPag);
            button.setName(nPag);
            jpVirtual.add(button);
            addVirtualListener(button);
        }
       
    }

    /**
     * This method add a listener for the RAMList, if the page of the RAMList is
     * selected, the modify bit change
     * @param button 
     */
    private void addRAMListener(JButton button) {

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton bPress = (JButton) e.getSource();
                addText("\nLa pagina " + bPress.getText() + " fue utilizada");
                modifyBit(bPress.getText());
            }
        });
    }

    /**
     * This method add a listener for the VirtualList, if the button of that list
     * is selected then the page change to the RAMList
     * @param button 
     */
    private void addVirtualListener(JButton button) {

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton bPress = (JButton) e.getSource();
                addText("\nLa pagina " + bPress.getText() + " fue seleccionada y hubo fallo de pagina");
                modify(bPress.getText());
            }
        });
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
     * This method modify the bit of each page
     * if the page has been selected -> this bit will be 1
     * @param buttonName 
     */
    private void modifyBit(String buttonName) {
        int counter = 0;
        for (Component component : jpRam.getComponents()) {
            if (component instanceof JButton jbutton) {
                if (buttonName.equals(jbutton.getText())) {
                    Label label = (Label) jpModificationBit.getComponent(counter);
                    label.setText("1");
                    break;
                }
            }
            counter++;
        }
    }

    /**
     * This method modify the pages of the virtual list and assign the page to
     * the RAM
     *
     * @param pageNumber
     */
    private void modify(String pageNumber) {
        int virtualCounter = 0;
        JButton virtual = null;
        for (Component component : jpVirtual.getComponents()) {
            if (component instanceof JButton jButton) {
                if (((JButton) component).getText().equals(pageNumber)) {
                    virtual = jButton;
                    break;
                }
            }
            virtualCounter++;
        }

        int counter = 0;
        String unusedBit = "0";
        boolean buttonFlag = false;
        boolean whileFlag = true;
        JButton RAM = null;

        if (buttonSelected == null) {
            buttonSelected = (JButton) jpRam.getComponent(1);
            for (Component component : jpRam.getComponents()) {

                if (component instanceof JButton jButton) {
                    Label label = (Label) jpModificationBit.getComponent(counter);
                    if (label.getText().equals(unusedBit)) {
                        RAM = (JButton) component;
                        label.setText("1");
                        break;
                    } else {
                        label.setText("0");
                    }
                }
                counter++;
            }
        } else {
            while (whileFlag) {
                for (Component component : jpRam.getComponents()) {
                    if (component instanceof JButton jButton) {
                        if (jButton == buttonSelected) {
                            buttonFlag = true;
                        }
                        if (buttonFlag) {
                            Label label = (Label) jpModificationBit.getComponent(counter);
                            if (label.getText().equals(unusedBit)) {
                                RAM = jButton;
                                label.setText("1");
                                whileFlag = false;
                                break;
                            } else if (!label.getText().equals(unusedBit)) {
                                label.setText("0");
                            }
                        }
                    }
                    counter++;
                }
                counter = 0;
            }
        }

        addText("\nLa pagina " + RAM.getText() + " fue seleccionada para el intercambio");
        addText("\nSe intercambio la pagina " + RAM.getText() + " con la pagina " + virtual.getText());
        addText("\nLa pagina "  + virtual.getText() + " fue utilizada");
        

        String ramText = RAM.getText();
        String vText = virtual.getText();

        RAM.setText(vText);
        virtual.setText(ramText);

        buttonSelected.setForeground(Color.BLACK);
        buttonSelected = RAM;
        buttonSelected.setForeground(Color.RED);
        jpVirtual.revalidate();
        jpRam.revalidate();

        ThreadColor threadColor = new ThreadColor(virtual, buttonSelected, noColor);
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
        jpModificationBit = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
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
            .addGap(0, 18, Short.MAX_VALUE)
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

        jpModificationBit.setBackground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout jpModificationBitLayout = new javax.swing.GroupLayout(jpModificationBit);
        jpModificationBit.setLayout(jpModificationBitLayout);
        jpModificationBitLayout.setHorizontalGroup(
            jpModificationBitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jpModificationBitLayout.setVerticalGroup(
            jpModificationBitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jpMarcoPagina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jpRam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jpModificationBit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jpVirtual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel1))))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 429, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(75, Short.MAX_VALUE))
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
                        .addComponent(jpModificationBit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jpRam, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jpMarcoPagina, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jpVirtual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(110, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        getParent().setVisible(true);
    }//GEN-LAST:event_formWindowClosed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JPanel jpMarcoPagina;
    private javax.swing.JPanel jpModificationBit;
    private javax.swing.JPanel jpRam;
    private javax.swing.JPanel jpVirtual;
    // End of variables declaration//GEN-END:variables
}
