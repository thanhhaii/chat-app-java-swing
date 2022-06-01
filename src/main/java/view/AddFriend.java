/*
 * Created by JFormDesigner on Wed Oct 14 14:11:25 ICT 2020
 */

package view;

import dao.AccountDao;
import entity.Account;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.*;
import javax.swing.GroupLayout;

/**
 * @author Thanh Hai
 */
public class AddFriend extends JDialog {
    public AddFriend(JFrame owner) {
        super(owner, true);
        initComponents();
        var model = new DefaultListModel<String>();
        var dao = new AccountDao();
        if(comboBox1.getSelectedIndex() == 0) {
            dao.getRequestAccount("received").forEach(Account -> {
                model.addElement(Account.getFullname());
            });
            list1.setModel(model);
        }
    }

    private void searchActionPerformed(ActionEvent e) {
        var model = new DefaultListModel<String>();
        new AccountDao().findFriends(findFriend.getText()).forEach(Account -> {model.addElement(Account.getFullname());});
        list2.setModel(model);
    }

    private void list2MouseClicked(MouseEvent e) {
        var dao = new AccountDao();
        if(e.getClickCount() == 2){
            for (int i = 0; i < dao.findFriends(findFriend.getText()).size(); i++) {
                if(i == list2.getSelectedIndex()){
                    new ViewProfileRequest(null, dao.findFriends(findFriend.getText()).get(i).getUsername()).setVisible(true);
                }
            }
        }
    }

    private void comboBox1ItemStateChanged(ItemEvent e) {
        var dao = new AccountDao();
        var model = new DefaultListModel<String>();
        int choice = comboBox1.getSelectedIndex();
        if(choice == 0){
            dao.getRequestAccount("received").forEach(Account->{
                model.addElement(Account.getFullname());
            });
            list1.setModel(model);
        }else if(choice == 1){
            dao.getRequestAccount("send").forEach(Account->{
                model.addElement(Account.getFullname());
            });
            list1.setModel(model);
        }
    }

    private void list1MouseClicked(MouseEvent e) {
        var dao = new AccountDao();
        if(e.getClickCount() == 2){
            if(comboBox1.getSelectedIndex() == 0){
                for (int i = 0; i < dao.getRequestAccount("received").size(); i++) {
                    if(i == list1.getSelectedIndex()){
                        new ViewProfileRequest(null, dao.getRequestAccount("received").get(i).getUsername()).setVisible(true);
                    }
                }
            }
            if(comboBox1.getSelectedIndex() == 1){
                for (int i = 0; i < dao.getRequestAccount("send").size(); i++) {
                    if(i == list1.getSelectedIndex()){
                        new ViewProfileRequest(null, dao.getRequestAccount("send").get(i).getUsername()).setVisible(true);
                    }
                }
            }
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        tabbedPane1 = new JTabbedPane();
        panel1 = new JPanel();
        scrollPane1 = new JScrollPane();
        list1 = new JList();
        comboBox1 = new JComboBox<>();
        panel2 = new JPanel();
        findFriend = new JTextField();
        button1 = new JButton();
        scrollPane2 = new JScrollPane();
        list2 = new JList();

        //======== this ========
        setTitle("Friend");
        var contentPane = getContentPane();

        //======== tabbedPane1 ========
        {
            tabbedPane1.setBackground(new Color(54, 62, 71));
            tabbedPane1.setForeground(Color.white);

            //======== panel1 ========
            {

                //======== scrollPane1 ========
                {

                    //---- list1 ----
                    list1.setFont(new Font("Segoe UI", Font.PLAIN, 18));
                    list1.setFixedCellHeight(50);
                    list1.setFixedCellWidth(10);
                    list1.setBackground(new Color(54, 62, 71));
                    list1.setSelectionBackground(new Color(48, 56, 65));
                    list1.setForeground(Color.white);
                    list1.setSelectionForeground(Color.white);
                    list1.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            list1MouseClicked(e);
                        }
                    });
                    scrollPane1.setViewportView(list1);
                }

                //---- comboBox1 ----
                comboBox1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                comboBox1.setModel(new DefaultComboBoxModel<>(new String[] {
                    "Request I received",
                    "Request I send"
                }));
                comboBox1.setBackground(new Color(54, 62, 71));
                comboBox1.setForeground(Color.white);
                comboBox1.addItemListener(e -> comboBox1ItemStateChanged(e));

                GroupLayout panel1Layout = new GroupLayout(panel1);
                panel1.setLayout(panel1Layout);
                panel1Layout.setHorizontalGroup(
                    panel1Layout.createParallelGroup()
                        .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
                        .addGroup(panel1Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(comboBox1, GroupLayout.PREFERRED_SIZE, 411, GroupLayout.PREFERRED_SIZE)
                            .addContainerGap(21, Short.MAX_VALUE))
                );
                panel1Layout.setVerticalGroup(
                    panel1Layout.createParallelGroup()
                        .addGroup(GroupLayout.Alignment.TRAILING, panel1Layout.createSequentialGroup()
                            .addGap(14, 14, 14)
                            .addComponent(comboBox1, GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                            .addGap(18, 18, 18)
                            .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 546, GroupLayout.PREFERRED_SIZE))
                );
            }
            tabbedPane1.addTab("Friend request", panel1);

            //======== panel2 ========
            {
                panel2.setBackground(new Color(48, 56, 65));

                //---- findFriend ----
                findFriend.setFont(new Font("Segoe UI", Font.PLAIN, 18));
                findFriend.setBackground(new Color(54, 62, 71));
                findFriend.setForeground(Color.white);

                //---- button1 ----
                button1.setIcon(new ImageIcon(getClass().getResource("/image/search.png")));
                button1.setBackground(new Color(48, 56, 65));
                button1.addActionListener(e -> searchActionPerformed(e));

                //======== scrollPane2 ========
                {

                    //---- list2 ----
                    list2.setFixedCellHeight(40);
                    list2.setBackground(new Color(54, 62, 71));
                    list2.setSelectionBackground(new Color(48, 56, 65));
                    list2.setForeground(Color.white);
                    list2.setSelectionForeground(Color.white);
                    list2.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            list2MouseClicked(e);
                        }
                    });
                    scrollPane2.setViewportView(list2);
                }

                GroupLayout panel2Layout = new GroupLayout(panel2);
                panel2.setLayout(panel2Layout);
                panel2Layout.setHorizontalGroup(
                    panel2Layout.createParallelGroup()
                        .addGroup(GroupLayout.Alignment.TRAILING, panel2Layout.createSequentialGroup()
                            .addGap(28, 28, 28)
                            .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(scrollPane2, GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE)
                                .addGroup(panel2Layout.createSequentialGroup()
                                    .addComponent(findFriend, GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
                                    .addGap(18, 18, 18)
                                    .addComponent(button1, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE)))
                            .addGap(37, 37, 37))
                );
                panel2Layout.setVerticalGroup(
                    panel2Layout.createParallelGroup()
                        .addGroup(panel2Layout.createSequentialGroup()
                            .addGap(26, 26, 26)
                            .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                .addComponent(findFriend)
                                .addComponent(button1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGap(18, 18, 18)
                            .addComponent(scrollPane2, GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE)
                            .addGap(26, 26, 26))
                );
            }
            tabbedPane1.addTab("Find new friends", panel2);
        }

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addComponent(tabbedPane1)
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addComponent(tabbedPane1)
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JScrollPane scrollPane1;
    private JList list1;
    private JComboBox<String> comboBox1;
    private JPanel panel2;
    private JTextField findFriend;
    private JButton button1;
    private JScrollPane scrollPane2;
    private JList list2;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
