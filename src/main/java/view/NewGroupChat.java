/*
 * Created by JFormDesigner on Fri Oct 30 15:21:42 ICT 2020
 */

package view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.jgoodies.forms.layout.*;
import dao.MessageDao;

/**
 * @author Thanh Hai
 */
public class NewGroupChat extends JDialog {
    public NewGroupChat(JFrame owner) {
        super(owner, true);
        initComponents();
    }

    private void button2ActionPerformed(ActionEvent e) {
        this.dispose();
    }

    private void newGroupChatActionPerformed(ActionEvent e) {
        new MessageDao().newGroupChat(nameGroup.getText());
        this.dispose();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        panel2 = new JPanel();
        label1 = new JLabel();
        nameGroup = new JTextField();
        panel1 = new JPanel();
        button1 = new JButton();
        button2 = new JButton();

        //======== this ========
        setTitle("New Group Chat");
        var contentPane = getContentPane();
        contentPane.setLayout(new GridBagLayout());
        ((GridBagLayout)contentPane.getLayout()).columnWidths = new int[] {389, 0};
        ((GridBagLayout)contentPane.getLayout()).rowHeights = new int[] {82, 43, 0};
        ((GridBagLayout)contentPane.getLayout()).columnWeights = new double[] {0.0, 1.0E-4};
        ((GridBagLayout)contentPane.getLayout()).rowWeights = new double[] {0.0, 0.0, 1.0E-4};

        //======== panel2 ========
        {
            panel2.setLayout(new GridBagLayout());
            ((GridBagLayout)panel2.getLayout()).columnWidths = new int[] {104, 291, 0};
            ((GridBagLayout)panel2.getLayout()).rowHeights = new int[] {37, 0};
            ((GridBagLayout)panel2.getLayout()).columnWeights = new double[] {0.0, 0.0, 1.0E-4};
            ((GridBagLayout)panel2.getLayout()).rowWeights = new double[] {0.0, 1.0E-4};

            //---- label1 ----
            label1.setText("Name Group:");
            label1.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            panel2.add(label1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                new Insets(0, 0, 0, 5), 0, 0));

            //---- nameGroup ----
            nameGroup.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            panel2.add(nameGroup, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
        }
        contentPane.add(panel2, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets(0, 0, 5, 0), 0, 0));

        //======== panel1 ========
        {
            panel1.setLayout(new FlowLayout(FlowLayout.RIGHT));

            //---- button1 ----
            button1.setText("Create Group Chat");
            button1.addActionListener(e -> newGroupChatActionPerformed(e));
            panel1.add(button1);

            //---- button2 ----
            button2.setText("Close");
            button2.addActionListener(e -> button2ActionPerformed(e));
            panel1.add(button2);
        }
        contentPane.add(panel1, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 0), 0, 0));
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel panel2;
    private JLabel label1;
    private JTextField nameGroup;
    private JPanel panel1;
    private JButton button1;
    private JButton button2;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

}
