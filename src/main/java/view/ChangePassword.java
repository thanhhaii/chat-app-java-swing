/*
 * Created by JFormDesigner on Sat Oct 31 21:10:05 ICT 2020
 */

package view;

import dao.AccountDao;
import org.w3c.dom.css.RGBColor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * @author Thanh Hai
 */
public class ChangePassword extends JDialog {
    public ChangePassword(JFrame owner) {
        super(owner,true);
        initComponents();
    }

    private void cancelButtonActionPerformed(ActionEvent e) {
        this.dispose();
    }

    private void okButtonActionPerformed(ActionEvent e) {
        if(new String(newPassword.getPassword()).equals(new String(ConfirmNewPass.getPassword()))){
            new AccountDao().changePassword(new String(oldPass.getPassword()), new String(newPassword.getPassword()));
        }
    }

    private void oldPassFocusLost(FocusEvent e) {
        var oldPassword = new String(oldPass.getPassword());
        if(new AccountDao().passwordComparison(oldPassword) && !oldPassword.isEmpty()){
            oldPass.setBorder(new JPasswordField().getBorder());
            passIncorrect.setVisible(false);
        }else {
            oldPass.setBorder(new LineBorder(Color.RED,1));
            passIncorrect.setVisible(true);
        }
    }

    private void newPassFocusLost(FocusEvent e) {

    }

    private void ConfirmNewPassFocusLost(FocusEvent e) {
        var newPass = new String(newPassword.getPassword());
        var confirmPass = new String(ConfirmNewPass.getPassword());
        if(newPass.equals(confirmPass)){
            ConfirmNewPass.setBorder(new JPasswordField().getBorder());
            confirmIncorrect.setVisible(false);
        }else {
            ConfirmNewPass.setBorder(new LineBorder(Color.RED,1));
            confirmIncorrect.setVisible(true);
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        panel1 = new JPanel();
        label1 = new JLabel();
        oldPass = new JPasswordField();
        passIncorrect = new JLabel();
        label2 = new JLabel();
        newPassword = new JPasswordField();
        label3 = new JLabel();
        ConfirmNewPass = new JPasswordField();
        confirmIncorrect = new JLabel();
        buttonBar = new JPanel();
        okButton = new JButton();
        cancelButton = new JButton();

        //======== this ========
        setTitle("Change Password");
        var contentPane = getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setLayout(new GridLayout());

                //======== panel1 ========
                {
                    panel1.setLayout(new GridBagLayout());
                    ((GridBagLayout)panel1.getLayout()).columnWidths = new int[] {101, 197, 0};
                    ((GridBagLayout)panel1.getLayout()).rowHeights = new int[] {20, 12, 0, 0, 13, 29, 0};
                    ((GridBagLayout)panel1.getLayout()).columnWeights = new double[] {0.0, 0.0, 1.0E-4};
                    ((GridBagLayout)panel1.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};

                    //---- label1 ----
                    label1.setText("Old Password:");
                    label1.setFont(new Font("Segoe UI", Font.BOLD, 12));
                    panel1.add(label1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                        new Insets(0, 0, 5, 5), 0, 0));

                    //---- oldPass ----
                    oldPass.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 18));
                    oldPass.addFocusListener(new FocusAdapter() {
                        @Override
                        public void focusLost(FocusEvent e) {
                            oldPassFocusLost(e);
                        }
                    });
                    panel1.add(oldPass, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 0), 0, 0));

                    //---- passIncorrect ----
                    passIncorrect.setText(" Password incorrect");
                    passIncorrect.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                    passIncorrect.setForeground(Color.red);
                    passIncorrect.setVisible(false);
                    panel1.add(passIncorrect, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 0), 0, 0));

                    //---- label2 ----
                    label2.setText("New Password:");
                    label2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                    panel1.add(label2, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                        GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                        new Insets(0, 0, 5, 5), 0, 0));

                    //---- newPassword ----
                    newPassword.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 18));
                    newPassword.addFocusListener(new FocusAdapter() {
                        @Override
                        public void focusLost(FocusEvent e) {
                            newPassFocusLost(e);
                        }
                    });
                    panel1.add(newPassword, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 0), 0, 0));

                    //---- label3 ----
                    label3.setText("Confirm New Password:");
                    label3.setFont(new Font("Segoe UI", Font.BOLD, 12));
                    panel1.add(label3, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                        GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                        new Insets(0, 0, 5, 5), 0, 0));

                    //---- ConfirmNewPass ----
                    ConfirmNewPass.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 18));
                    ConfirmNewPass.addFocusListener(new FocusAdapter() {
                        @Override
                        public void focusLost(FocusEvent e) {
                            ConfirmNewPassFocusLost(e);
                        }
                    });
                    panel1.add(ConfirmNewPass, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 0), 0, 0));

                    //---- confirmIncorrect ----
                    confirmIncorrect.setText("Confirm password does match");
                    confirmIncorrect.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                    confirmIncorrect.setForeground(Color.red);
                    confirmIncorrect.setVisible(false);
                    panel1.add(confirmIncorrect, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 0), 0, 0));
                }
                contentPanel.add(panel1);
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 85, 80};
                ((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0};

                //---- okButton ----
                okButton.setText("Change");
                okButton.addActionListener(e -> okButtonActionPerformed(e));
                buttonBar.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

                //---- cancelButton ----
                cancelButton.setText("Cancel");
                cancelButton.addActionListener(e -> cancelButtonActionPerformed(e));
                buttonBar.add(cancelButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }
        contentPane.add(dialogPane);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JPanel panel1;
    private JLabel label1;
    private JPasswordField oldPass;
    private JLabel passIncorrect;
    private JLabel label2;
    private JPasswordField newPassword;
    private JLabel label3;
    private JPasswordField ConfirmNewPass;
    private JLabel confirmIncorrect;
    private JPanel buttonBar;
    private JButton okButton;
    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
