/*
 * Created by JFormDesigner on Sun Nov 01 15:24:55 ICT 2020
 */

package view;

import custom.MemberGroupRenderer;
import dao.AccountDao;
import dao.MessageDao;
import entity.Account;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;

/**
 * @author Thanh Hai
 */
public class AddRemoveMemberGroup extends JDialog {
    public AddRemoveMemberGroup(JFrame owner) {
        super(owner, true);
        roomId = Main.roomId;
        AddRemoveMemberGroup.action = Main.action;
        initComponents();
        if(action.equals("add")) {
            loadFriendsList(roomId);
        }else {
            myfriend.setBorder(new TitledBorder("Member group"));
            loadMemberGroup();
        }
    }

    private void loadMemberGroup(){
        var model = new DefaultListModel<Account>();
        new AccountDao().getMemberGroupChat(roomId).forEach(member -> {
            if(!member.getUsername().equals(Login.usernameUser)) {
                model.addElement(new Account(member.getAvatar(), member.getFullname(), member.getUsername(), member.getId()));
            }
        });
        myfriend.setCellRenderer(new MemberGroupRenderer());
        myfriend.setModel(model);
    }

    private void loadFriendsList(int roomid){
        var accountDao = new AccountDao();
        var model = new DefaultListModel<Account>();
        accountDao.loadFriend().forEach(member -> {
            model.addElement(new Account(member.getAvatar(), member.getFullname(), member.getUsername(),member.getId()));
        });
        accountDao.getMemberGroupChat(roomid).forEach(friend -> {
            for(var i = 0; i < model.size(); i++){
                if(friend.getUsername().equals(model.get(i).getUsername())){
                    model.remove(i);
                    break;
                }
            }
        });
        myfriend.setModel(model);
        myfriend.setCellRenderer(new MemberGroupRenderer());
    }

    private void cancelButtonActionPerformed(ActionEvent e) {
        this.dispose();
    }

    private void addMemberMouseClicked(MouseEvent e) {
        var dao = new AccountDao();
        if(e.getClickCount() == 2){
            if(action.equals("add")) {
                var username = myfriend.getModel().getElementAt(myfriend.getSelectedIndex()).toString().split("username='")[1].split("'")[0];
                var fullname = myfriend.getModel().getElementAt(myfriend.getSelectedIndex()).toString().split("fullname='")[1].split("'")[0];
                dao.insertMemberGroup(roomId, username);
                loadFriendsList(roomId);
                JOptionPane.showMessageDialog(null, "Add " + fullname + " to the successful group.");
            }
        }
    }

    private void thisWindowClosing(WindowEvent e) {

    }

    private void removerMouseClicked(MouseEvent e) {
        var dao = new AccountDao();
        if(e.getClickCount() == 2){
            if(action.equals("remove")) {
                var username = myfriend.getModel().getElementAt(myfriend.getSelectedIndex()).toString().split("username='")[1].split("'")[0];
                var fullname = myfriend.getModel().getElementAt(myfriend.getSelectedIndex()).toString().split("fullname='")[1].split("'")[0];
                var check = JOptionPane.showConfirmDialog(null, "You definitely want to remove "+fullname+ " from the group");
                if(check == JOptionPane.YES_OPTION) {
                    dao.deleteMemberGroup(roomId, username);
                }
                loadMemberGroup();
            }
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        scrollPane2 = new JScrollPane();
        myfriend = new JList();
        buttonBar = new JPanel();
        cancelButton = new JButton();

        //======== this ========
        setTitle("Add new member");
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                thisWindowClosing(e);
            }
        });
        var contentPane = getContentPane();

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setLayout(new GridBagLayout());
                ((GridBagLayout)contentPanel.getLayout()).columnWidths = new int[] {0, 0};
                ((GridBagLayout)contentPanel.getLayout()).rowHeights = new int[] {462, 0};
                ((GridBagLayout)contentPanel.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
                ((GridBagLayout)contentPanel.getLayout()).rowWeights = new double[] {0.0, 1.0E-4};

                //======== scrollPane2 ========
                {

                    //---- myfriend ----
                    myfriend.setBorder(new TitledBorder("My friend"));
                    myfriend.setFixedCellHeight(60);
                    myfriend.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            addMemberMouseClicked(e);
                            removerMouseClicked(e);
                        }
                    });
                    scrollPane2.setViewportView(myfriend);
                }
                contentPanel.add(scrollPane2, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 85, 80};
                ((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0};

                //---- cancelButton ----
                cancelButton.setText("CLOSE");
                cancelButton.addActionListener(e -> cancelButtonActionPerformed(e));
                buttonBar.add(cancelButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addComponent(dialogPane, GroupLayout.PREFERRED_SIZE, 398, GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE))
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addComponent(dialogPane, GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE)
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JScrollPane scrollPane2;
    private JList myfriend;
    private JPanel buttonBar;
    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
    private static int roomId;
    private static String action;
}
