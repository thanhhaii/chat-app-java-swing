/*
 * Created by JFormDesigner on Wed Sep 30 16:54:10 ICT 2020
 */

package view;

import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;
import javax.swing.*;
import com.toedter.calendar.*;
import dao.AccountDao;
import entity.Account;

/**
 * @author Thanh Hai
 */
public class Login extends JDialog {
    public Login(Window owner) {
        super(owner);
        initComponents();
        setVisible(login);
    }

    private void setVisible(JPanel panel){
        login.setVisible(false);
        register.setVisible(false);
        panel.setVisible(true);
    }

    private void Connect(){
        try {
            if (socket != null) {
                socket.close();
            }
            socket = new Socket(host, port);
            this.dis = new DataInputStream(socket.getInputStream());
            this.dos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void Login(String username){
        try{
            Connect();
            dos.writeUTF(username);
            dos.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void button2ActionPerformed(ActionEvent e) {
        setVisible(register);
    }

    private void gotoLoginActionPerformed(ActionEvent e) {
        setVisible(login);
    }

    private void registerAccount(ActionEvent e) {
        var dao = new AccountDao();
        var checkadd = dao.registerAccount(jtfUserReg.getText(), jtfPassReg.getText(), jtfFullnameReg.getText(), Objects.requireNonNull(cBGender.getSelectedItem()).toString(),new java.sql.Date(dCBirthDay.getDate().getTime()), jtfMailReg.getText());
        if(checkadd == 1){
            JOptionPane.showMessageDialog(this, "Register Success! Go to login.");
            setVisible(login);
            for (Component component : register.getComponents()){
                if(component instanceof JTextField){
                    ((JTextField) component).setText("");
                }
            }
            dCBirthDay.setDate(null);
        }else {
            JOptionPane.showMessageDialog(this, "Username already exists!");
        }
    }

    private void login(){
        var dao = new AccountDao();
        var checkLogin = dao.login(usernameLogin.getText(), new String(passLogin.getPassword()));
        if(checkLogin){
            Login(usernameLogin.getText());
            usernameUser = usernameLogin.getText();
            this.setVisible(false);
            new Main(this.dis, this.dos).setVisible(true);
            dao.online(true);
        }else {
            JOptionPane.showMessageDialog(this, "Username or password incorrect");
        }
    }

    private void loginAccount(ActionEvent e) {
        login();
    }

    private void forgotPassMouseClicked(MouseEvent e) {
        var username = JOptionPane.showInputDialog(this, "Enter your username", "Forgot password", JOptionPane.INFORMATION_MESSAGE);
        new AccountDao().forgotPassword(username);
    }

    private void usernameLoginKeyPressed(KeyEvent e) {
        var checkText = e.getKeyChar();
        var checkLengt = usernameLogin.getText().length();
        if ((Character.isLetter(checkText) || !Character.isLetter(checkText)) && !Character.isWhitespace(checkText) && (checkLengt <= 50)) {
            usernameLogin.setEditable(true);
        } else {
            usernameLogin.setEditable(false);
        }
    }

    private void passLoginKeyPressed(KeyEvent e) {
        var checkPass = e.getKeyChar();
        if(!Character.isWhitespace(checkPass)){
            passLogin.setEditable(true);
        }else {
            passLogin.setEditable(false);
        }
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            login();
        }
    }

    private void jtfFullnameRegKeyPressed(KeyEvent e) {
        if (!Character.isLetter(e.getKeyChar()) && !(jtfFullnameReg.getText().length() <= 50)){
            jtfFullnameReg.setEditable(false);
        }else {
            jtfFullnameReg.setEditable(true);
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        login = new JPanel();
        usernameLogin = new JTextField();
        usernameLabel = new JLabel();
        passwrodLabel = new JLabel();
        button1 = new JButton();
        label3 = new JLabel();
        button2 = new JButton();
        passLogin = new JPasswordField();
        forgotPass = new JLabel();
        register = new JPanel();
        gotoLogin = new JButton();
        jtfUserReg = new JTextField();
        jtfPassReg = new JTextField();
        jtfFullnameReg = new JTextField();
        cBGender = new JComboBox<>();
        dCBirthDay = new JDateChooser();
        jtfMailReg = new JTextField();
        label1 = new JLabel();
        label2 = new JLabel();
        label4 = new JLabel();
        label5 = new JLabel();
        label6 = new JLabel();
        label7 = new JLabel();
        btnRegister = new JButton();

        //======== this ========
        setTitle("ChatApp");
        var contentPane = getContentPane();
        contentPane.setLayout(null);

        //======== login ========
        {
            login.setBackground(new Color(51, 51, 51));
            login.setLayout(null);

            //---- usernameLogin ----
            usernameLogin.setFont(new Font("JetBrains Mono", Font.PLAIN, 16));
            usernameLogin.setForeground(Color.white);
            usernameLogin.setBackground(new Color(78, 78, 78));
            usernameLogin.setBorder(null);
            usernameLogin.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    usernameLoginKeyPressed(e);
                }
            });
            login.add(usernameLogin);
            usernameLogin.setBounds(45, 275, 375, 38);

            //---- usernameLabel ----
            usernameLabel.setText("Username:");
            usernameLabel.setFont(new Font("JetBrains Mono", Font.PLAIN, 16));
            usernameLabel.setForeground(new Color(153, 153, 153));
            login.add(usernameLabel);
            usernameLabel.setBounds(new Rectangle(new Point(45, 250), usernameLabel.getPreferredSize()));

            //---- passwrodLabel ----
            passwrodLabel.setText("Password:");
            passwrodLabel.setFont(new Font("JetBrains Mono", Font.PLAIN, 16));
            passwrodLabel.setForeground(new Color(153, 153, 153));
            login.add(passwrodLabel);
            passwrodLabel.setBounds(new Rectangle(new Point(45, 320), passwrodLabel.getPreferredSize()));

            //---- button1 ----
            button1.setText("Login");
            button1.setBorder(null);
            button1.setBackground(new Color(38, 107, 153));
            button1.setForeground(Color.white);
            button1.addActionListener(e -> loginAccount(e));
            login.add(button1);
            button1.setBounds(45, 410, 375, 38);

            //---- label3 ----
            label3.setText("Welcome to ChatApp");
            label3.setFont(new Font("Leelawadee UI", Font.BOLD, 28));
            label3.setForeground(Color.white);
            login.add(label3);
            label3.setBounds(new Rectangle(new Point(100, 140), label3.getPreferredSize()));

            //---- button2 ----
            button2.setText("Create New Account");
            button2.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
            button2.setBackground(new Color(89, 89, 89));
            button2.setForeground(Color.white);
            button2.setBorder(null);
            button2.addActionListener(e -> button2ActionPerformed(e));
            login.add(button2);
            button2.setBounds(145, 535, 195, 40);

            //---- passLogin ----
            passLogin.setBorder(null);
            passLogin.setForeground(Color.white);
            passLogin.setBackground(new Color(78, 78, 78));
            passLogin.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
            passLogin.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    passLoginKeyPressed(e);
                }
            });
            login.add(passLogin);
            passLogin.setBounds(45, 345, 375, 40);

            //---- forgotPass ----
            forgotPass.setText("Forgot Password");
            forgotPass.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            forgotPass.setForeground(Color.white);
            forgotPass.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    forgotPassMouseClicked(e);
                }
            });
            login.add(forgotPass);
            forgotPass.setBounds(new Rectangle(new Point(195, 590), forgotPass.getPreferredSize()));

            {
                // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < login.getComponentCount(); i++) {
                    Rectangle bounds = login.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = login.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                login.setMinimumSize(preferredSize);
                login.setPreferredSize(preferredSize);
            }
        }
        contentPane.add(login);
        login.setBounds(0, 0, 465, 620);

        //======== register ========
        {
            register.setBackground(new Color(51, 51, 51));
            register.setLayout(null);

            //---- gotoLogin ----
            gotoLogin.setText("Login");
            gotoLogin.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
            gotoLogin.setBackground(new Color(89, 89, 89));
            gotoLogin.setForeground(Color.white);
            gotoLogin.setBorder(null);
            gotoLogin.addActionListener(e -> gotoLoginActionPerformed(e));
            register.add(gotoLogin);
            gotoLogin.setBounds(140, 555, 195, 40);

            //---- jtfUserReg ----
            jtfUserReg.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
            jtfUserReg.setBackground(new Color(78, 78, 78));
            jtfUserReg.setForeground(Color.white);
            jtfUserReg.setBorder(null);
            jtfUserReg.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    usernameLoginKeyPressed(e);
                }
            });
            register.add(jtfUserReg);
            jtfUserReg.setBounds(50, 140, 365, 40);

            //---- jtfPassReg ----
            jtfPassReg.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
            jtfPassReg.setBackground(new Color(78, 78, 78));
            jtfPassReg.setForeground(Color.white);
            jtfPassReg.setBorder(null);
            jtfPassReg.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    passLoginKeyPressed(e);
                }
            });
            register.add(jtfPassReg);
            jtfPassReg.setBounds(50, 205, 365, 40);

            //---- jtfFullnameReg ----
            jtfFullnameReg.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
            jtfFullnameReg.setBackground(new Color(78, 78, 78));
            jtfFullnameReg.setForeground(Color.white);
            jtfFullnameReg.setBorder(null);
            jtfFullnameReg.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    jtfFullnameRegKeyPressed(e);
                }
            });
            register.add(jtfFullnameReg);
            jtfFullnameReg.setBounds(50, 270, 365, 40);

            //---- cBGender ----
            cBGender.setModel(new DefaultComboBoxModel<>(new String[] {
                "Male",
                "Female",
                "Other"
            }));
            cBGender.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
            cBGender.setBackground(new Color(78, 78, 78));
            cBGender.setForeground(Color.white);
            cBGender.setBorder(null);
            register.add(cBGender);
            cBGender.setBounds(50, 335, 185, 40);

            //---- dCBirthDay ----
            dCBirthDay.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
            dCBirthDay.setBackground(new Color(78, 78, 78));
            dCBirthDay.setForeground(Color.white);
            dCBirthDay.setBorder(null);
            register.add(dCBirthDay);
            dCBirthDay.setBounds(245, 335, 170, 40);

            //---- jtfMailReg ----
            jtfMailReg.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
            jtfMailReg.setBackground(new Color(78, 78, 78));
            jtfMailReg.setForeground(Color.white);
            jtfMailReg.setBorder(null);
            register.add(jtfMailReg);
            jtfMailReg.setBounds(50, 400, 365, 40);

            //---- label1 ----
            label1.setText("Username:");
            label1.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
            label1.setForeground(new Color(153, 153, 153));
            register.add(label1);
            label1.setBounds(new Rectangle(new Point(50, 120), label1.getPreferredSize()));

            //---- label2 ----
            label2.setText("Password:");
            label2.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
            label2.setForeground(new Color(153, 153, 153));
            register.add(label2);
            label2.setBounds(50, 185, 72, 18);

            //---- label4 ----
            label4.setText("Fullname:");
            label4.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
            label4.setForeground(new Color(153, 153, 153));
            register.add(label4);
            label4.setBounds(50, 250, 72, 18);

            //---- label5 ----
            label5.setText("Gender:");
            label5.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
            label5.setForeground(new Color(153, 153, 153));
            register.add(label5);
            label5.setBounds(50, 315, 72, 18);

            //---- label6 ----
            label6.setText("Birthday:");
            label6.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
            label6.setForeground(new Color(153, 153, 153));
            register.add(label6);
            label6.setBounds(245, 315, 72, 18);

            //---- label7 ----
            label7.setText("Email:");
            label7.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
            label7.setForeground(new Color(153, 153, 153));
            register.add(label7);
            label7.setBounds(50, 380, 72, 18);

            //---- btnRegister ----
            btnRegister.setText("Register");
            btnRegister.addActionListener(e -> registerAccount(e));
            register.add(btnRegister);
            btnRegister.setBounds(50, 460, 365, 40);

            {
                // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < register.getComponentCount(); i++) {
                    Rectangle bounds = register.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = register.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                register.setMinimumSize(preferredSize);
                register.setPreferredSize(preferredSize);
            }
        }
        contentPane.add(register);
        register.setBounds(0, 0, 465, 620);

        {
            // compute preferred size
            Dimension preferredSize = new Dimension();
            for(int i = 0; i < contentPane.getComponentCount(); i++) {
                Rectangle bounds = contentPane.getComponent(i).getBounds();
                preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
            }
            Insets insets = contentPane.getInsets();
            preferredSize.width += insets.right;
            preferredSize.height += insets.bottom;
            contentPane.setMinimumSize(preferredSize);
            contentPane.setPreferredSize(preferredSize);
        }
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel login;
    private JTextField usernameLogin;
    private JLabel usernameLabel;
    private JLabel passwrodLabel;
    private JButton button1;
    private JLabel label3;
    private JButton button2;
    private JPasswordField passLogin;
    private JLabel forgotPass;
    private JPanel register;
    private JButton gotoLogin;
    private JTextField jtfUserReg;
    private JTextField jtfPassReg;
    private JTextField jtfFullnameReg;
    private JComboBox<String> cBGender;
    private JDateChooser dCBirthDay;
    private JTextField jtfMailReg;
    private JLabel label1;
    private JLabel label2;
    private JLabel label4;
    private JLabel label5;
    private JLabel label6;
    private JLabel label7;
    private JButton btnRegister;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
    public static String usernameUser;
    private String host = "localhost";
    private final int port = 1411;
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

    public static void main(String[] args) {
        try {
            //here you can put the selected theme class name in JTattoo
            UIManager.setLookAndFeel("com.jtattoo.plaf.fast.FastLookAndFeel");

        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Login(null).setVisible(true);
            }
        });
    }
}
