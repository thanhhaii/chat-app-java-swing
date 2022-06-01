/*
 * Created by JFormDesigner on Thu Oct 15 14:32:40 ICT 2020
 */

package view;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import dao.AccountDao;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import javax.swing.*;

/**
 * @author Thanh Hai
 */
public class ViewProfileRequest extends JDialog {

    public ViewProfileRequest(JFrame owner, String username) {
        super(owner, true);
        initComponents();
        var dao = new AccountDao();
        var checkStatus = dao.checkStatusFriend(username);
        dao.getInfoUser(username).forEach(Account -> {
            lbUsername.setText(Account.getUsername());
            setTitle(Account.getFullname());
            Avatar.setIcon(new ImageIcon(new ImageIcon(Account.getAvatar()).getImage().getScaledInstance(90,90, Image.SCALE_SMOOTH)));
            lbFullname.setText(Account.getFullname());
            lbGender.setText(Account.getGender());
            lbBirthday.setText(new SimpleDateFormat("dd/MM/yyyy").format(Account.getBirthday()));
            lbEmail.setText(Account.getEmail());
        });
        setVisibleAcceptDecline(false);
        switch (checkStatus) {
            case 0 -> {
                setVisibleAcceptDecline(false);
                add.setText("ADD FRIEND");
            }
            case 1 -> {
                setVisibleAcceptDecline(false);
                add.setText("UNFRIEND");
            }
            case 2 -> {
                setVisibleAcceptDecline(false);
                add.setText("CANCEL ADD FRIEND");
            }
            case 3 -> {
                setVisibleAcceptDecline(true);
            }
        }
    }

    private void setVisibleAcceptDecline(Boolean trueFalse){
        accept.setVisible(trueFalse);
        decline.setVisible(trueFalse);
        add.setVisible(!trueFalse);
    }

    private void closeActionPerformed(ActionEvent e) {
        this.dispose();
    }

    private void addActionPerformed(ActionEvent e) {
        var dao = new AccountDao();
        if(add.getText().equals("ADD FRIEND")){
            add.setText("CANCEL ADD FRIEND");
            dao.actionWithFriend(lbUsername.getText(), "addfriend");
        }else if(add.getText().equals("UNFRIEND") || add.getText().equals("CANCEL ADD FRIEND")){
            add.setText("ADD FRIEND");
            dao.actionWithFriend(lbUsername.getText(), "unfriend");
            setVisibleAcceptDecline(false);
        }
    }

    private void acceptActionPerformed(ActionEvent e) {
        var dao = new AccountDao();
        dao.actionWithFriend(lbUsername.getText(),"accept");
        setVisibleAcceptDecline(false);
        add.setText("UNFRIEND");
    }

    private void declineActionPerformed(ActionEvent e) {
        var dao = new AccountDao();
        dao.actionWithFriend(lbUsername.getText(),"decline");
        setVisibleAcceptDecline(false);
        add.setText("ADD");
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        panel1 = new JPanel();
        lbUsername = new JLabel();
        label2 = new JLabel();
        lbFullname = new JLabel();
        label3 = new JLabel();
        lbGender = new JLabel();
        label4 = new JLabel();
        lbBirthday = new JLabel();
        label5 = new JLabel();
        lbEmail = new JLabel();
        label1 = new JLabel();
        panel2 = new JPanel();
        Avatar = new JLabel();
        panel3 = new JPanel();
        accept = new JButton();
        add = new JButton();
        decline = new JButton();
        close = new JButton();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setResizable(false);
        var contentPane = getContentPane();
        contentPane.setLayout(new FormLayout(
            "214dlu",
            "fill:74dlu, $lgap, fill:104dlu, $lgap, fill:29dlu"));

        //======== panel1 ========
        {
            panel1.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            panel1.setLayout(new GridBagLayout());
            ((GridBagLayout)panel1.getLayout()).columnWidths = new int[] {87, 91, 0};
            ((GridBagLayout)panel1.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0, 0};
            ((GridBagLayout)panel1.getLayout()).columnWeights = new double[] {0.0, 0.6, 1.0E-4};
            ((GridBagLayout)panel1.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};

            //---- lbUsername ----
            lbUsername.setText("text");
            lbUsername.setFont(new Font("Segoe UI", Font.PLAIN, 17));
            panel1.add(lbUsername, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 10, 0), 0, 0));

            //---- label2 ----
            label2.setText("Fullname:");
            label2.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            panel1.add(label2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                new Insets(0, 0, 10, 10), 0, 0));

            //---- lbFullname ----
            lbFullname.setText("text");
            lbFullname.setFont(new Font("Segoe UI", Font.PLAIN, 17));
            panel1.add(lbFullname, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 10, 0), 0, 0));

            //---- label3 ----
            label3.setText("Gender:");
            label3.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            panel1.add(label3, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                new Insets(0, 0, 10, 10), 0, 0));

            //---- lbGender ----
            lbGender.setText("text");
            lbGender.setFont(new Font("Segoe UI", Font.PLAIN, 17));
            panel1.add(lbGender, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 10, 0), 0, 0));

            //---- label4 ----
            label4.setText("Birthday:");
            label4.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            panel1.add(label4, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                new Insets(0, 0, 10, 10), 0, 0));

            //---- lbBirthday ----
            lbBirthday.setText("text");
            lbBirthday.setFont(new Font("Segoe UI", Font.PLAIN, 17));
            panel1.add(lbBirthday, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 10, 0), 0, 0));

            //---- label5 ----
            label5.setText("Email:");
            label5.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            panel1.add(label5, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                new Insets(0, 0, 0, 10), 0, 0));

            //---- lbEmail ----
            lbEmail.setText("text");
            lbEmail.setFont(new Font("Segoe UI", Font.PLAIN, 17));
            panel1.add(lbEmail, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));

            //---- label1 ----
            label1.setText("Username:");
            label1.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            panel1.add(label1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                new Insets(0, 0, 10, 10), 0, 0));
        }
        contentPane.add(panel1, cc.xy(1, 3));

        //======== panel2 ========
        {
            panel2.setLayout(new FlowLayout());
            panel2.add(Avatar);
        }
        contentPane.add(panel2, cc.xy(1, 1));

        //======== panel3 ========
        {
            panel3.setLayout(new FlowLayout());

            //---- accept ----
            accept.setText("ACCEPT");
            accept.addActionListener(e -> acceptActionPerformed(e));
            panel3.add(accept);

            //---- add ----
            add.setText("ADD FRIEND");
            add.setVisible(false);
            add.addActionListener(e -> addActionPerformed(e));
            panel3.add(add);

            //---- decline ----
            decline.setText("DECLINE");
            decline.addActionListener(e -> declineActionPerformed(e));
            panel3.add(decline);

            //---- close ----
            close.setText("CLOSE");
            close.addActionListener(e -> closeActionPerformed(e));
            panel3.add(close);
        }
        contentPane.add(panel3, cc.xy(1, 5));
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel panel1;
    private JLabel lbUsername;
    private JLabel label2;
    private JLabel lbFullname;
    private JLabel label3;
    private JLabel lbGender;
    private JLabel label4;
    private JLabel lbBirthday;
    private JLabel label5;
    private JLabel lbEmail;
    private JLabel label1;
    private JPanel panel2;
    private JLabel Avatar;
    private JPanel panel3;
    private JButton accept;
    private JButton add;
    private JButton decline;
    private JButton close;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

}
