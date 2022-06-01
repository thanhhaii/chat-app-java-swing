/*
 * Created by JFormDesigner on Sun Oct 11 22:38:34 ICT 2020
 */

package view;

import util.Server;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * @author Thanh Hai
 */
public class RunServer extends JDialog {
    public RunServer(Window owner) {
        super(owner);
        initComponents();
    }

    private void button1ActionPerformed(ActionEvent e) {
        var t = new Thread(){
            @Override
            public void run() {
                try {
                    new Server();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        };
        t.start();

        button1.setEnabled(false);
        label1.setVisible(true);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        button1 = new JButton();
        label1 = new JLabel();

        //======== this ========
        setTitle("Server");
        var contentPane = getContentPane();

        //---- button1 ----
        button1.setText("Start Server");
        button1.addActionListener(e -> button1ActionPerformed(e));

        //---- label1 ----
        label1.setText("Start server successfult");
        label1.setForeground(new Color(255, 51, 51));
        label1.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        label1.setVisible(false);

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addGap(137, 137, 137)
                    .addComponent(button1, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(142, Short.MAX_VALUE))
                .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                    .addContainerGap(106, Short.MAX_VALUE)
                    .addComponent(label1)
                    .addGap(96, 96, 96))
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addGap(93, 93, 93)
                    .addComponent(button1, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE)
                    .addGap(36, 36, 36)
                    .addComponent(label1)
                    .addContainerGap(51, Short.MAX_VALUE))
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JButton button1;
    private JLabel label1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RunServer(null).setVisible(true);
            }
        });
    }

}
