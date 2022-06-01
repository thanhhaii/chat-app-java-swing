/*
 * Created by JFormDesigner on Mon Oct 05 16:05:46 ICT 2020
 */

package view;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageFilter;
import java.io.*;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.toedter.calendar.*;
import dao.AccountDao;

/**
 * @author hai
 */
public class Profile extends JDialog {
    public Profile(JFrame owner) {
        super(owner, true);
        initComponents();
        username.setText(Login.usernameUser);
        new AccountDao().getInfoUser(Login.usernameUser).forEach(Account->{
            fullname.setText(Account.getFullname());
            gender.setSelectedItem(Account.getGender());
            avatar.setIcon(new ImageIcon(new ImageIcon(Account.getAvatar()).getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH)));
            birthday.setDate(Account.getBirthday());
            email.setText(Account.getEmail());
        });
        birthday.setEnabled(false);
    }

    private void cancelButtonActionPerformed(ActionEvent e) {
        this.dispose();
    }

    private void changeColorMouseEntered(MouseEvent e) {
        changeAvatar.setForeground(Color.BLACK);
    }

    private void changeAvatarMouseExited(MouseEvent e) {
        changeAvatar.setForeground(Color.GRAY);
    }

    private void updateAvatarMouseClicked(MouseEvent e) {
        var chooser = new JFileChooser();
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("*.Images", "jpg","png"));
        chooser.setAcceptAllFileFilterUsed(false);
        var retVal = chooser.showSaveDialog(null);
        if(retVal == JFileChooser.APPROVE_OPTION){
            var path = chooser.getSelectedFile().getAbsolutePath();
            avatar.setIcon(new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH)));
            decoder(encoder(path), "image/"+Login.usernameUser+".jpg");
            new AccountDao().updateAvatar("image/"+Login.usernameUser+".jpg");
        }
    }

    public static String encoder(String imagePath) {
        String base64Image = "";
        File file = new File(imagePath);
        try (FileInputStream imageInFile = new FileInputStream(file)) {
            // Reading a Image file from file system
            byte[] imageData = new byte[(int) file.length()];
            imageInFile.read(imageData);
            base64Image = Base64.getEncoder().encodeToString(imageData);
        } catch (FileNotFoundException e) {
            System.out.println("Image not found" + e);
        } catch (IOException ioe) {
            System.out.println("Exception while reading the Image " + ioe);
        }
        return base64Image;
    }

    public static void decoder(String base64Image, String pathFile) {
        try (FileOutputStream imageOutFile = new FileOutputStream(pathFile)) {
            // Converting a Base64 String into Image byte array
            byte[] imageByteArray = Base64.getDecoder().decode(base64Image);
            imageOutFile.write(imageByteArray);
        } catch (FileNotFoundException e) {
            System.out.println("Image not found" + e);
        } catch (IOException ioe) {
            System.out.println("Exception while reading the Image " + ioe);
        }
    }

    private static BufferedImage resizeImage(BufferedImage originalImage, int type, int img_width, int img_height){
        BufferedImage resizedImage = new BufferedImage(img_width, img_height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, img_width, img_height, null);
        g.dispose();
        return resizedImage;
    }

    private void editActionPerformed(ActionEvent e) {
        if(edit.getText().equals("EDIT")){
            fullname.setEditable(true);
            gender.setEnabled(true);
            email.setEditable(true);
            birthday.setEnabled(true);
            save.setEnabled(true);
            edit.setText("CANCEL EDIT");
        }else {
            new AccountDao().getInfoUser(Login.usernameUser).forEach(Account->{
                fullname.setText(Account.getFullname());
                gender.setSelectedItem(Account.getGender());
                birthday.setDate(Account.getBirthday());
                email.setText(Account.getEmail());
            });
            fullname.setEditable(false);
            gender.setEnabled(false);
            email.setEditable(false);
            birthday.setEnabled(false);
            save.setEnabled(false);
            edit.setText("EDIT");
        }
    }

    private void saveActionPerformed(ActionEvent e) {
        var dao = new AccountDao();
        dao.getInfoUser(Login.usernameUser).forEach(Account->{
            if(Account.getFullname().equals(fullname.getText()) && Account.getGender().equals(gender.getSelectedItem()) && Account.getBirthday().equals(birthday.getDate()) && Account.getEmail().equals(email.getText())){
                JOptionPane.showMessageDialog(this, "No information has been change");
            }else if(Account.getFullname().equals(fullname.getText()) || Account.getGender().equals(gender.getSelectedItem()) || Account.getBirthday().equals(birthday.getDate()) || Account.getEmail().equals(email.getText())){
                if(JOptionPane.showConfirmDialog(this, "Are you sure you want to change the information") == 0){
                    dao.updateInfo(fullname.getText(), Objects.requireNonNull(gender.getSelectedItem()).toString(), new java.sql.Date(birthday.getDate().getTime()), email.getText());
                    JOptionPane.showMessageDialog(this,"Change information success!");
                    fullname.setEditable(false);
                    gender.setEnabled(false);
                    email.setEditable(false);
                    birthday.setEnabled(false);
                    save.setEnabled(false);
                    edit.setText("EDIT");
                }
            }
        });
    }

    private void fullnameKeyPressed(KeyEvent e) {
        if (!Character.isLetter(e.getKeyChar()) && !(fullname.getText().length() <= 50)){
            fullname.setEditable(false);
        }else {
            fullname.setEditable(true);
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        label2 = new JLabel();
        username = new JLabel();
        label1 = new JLabel();
        fullname = new JTextField();
        label4 = new JLabel();
        gender = new JComboBox<>();
        label5 = new JLabel();
        birthday = new JDateChooser();
        label6 = new JLabel();
        email = new JTextField();
        buttonBar = new JPanel();
        edit = new JButton();
        save = new JButton();
        cancelButton = new JButton();
        panel3 = new JPanel();
        avatar = new JLabel();
        changeAvatar = new JLabel();

        //======== this ========
        setTitle("My profile");
        var contentPane = getContentPane();
        contentPane.setLayout(null);

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setLayout(null);

            //======== contentPanel ========
            {
                contentPanel.setLayout(new GridBagLayout());
                ((GridBagLayout)contentPanel.getLayout()).columnWidths = new int[] {114, 0, 0};
                ((GridBagLayout)contentPanel.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0, 0};
                ((GridBagLayout)contentPanel.getLayout()).columnWeights = new double[] {0.0, 1.0, 1.0E-4};
                ((GridBagLayout)contentPanel.getLayout()).rowWeights = new double[] {1.0, 1.0, 1.0, 1.0, 1.0, 1.0E-4};

                //---- label2 ----
                label2.setText("Username:");
                label2.setFont(new Font("JetBrains Mono", Font.PLAIN, 16));
                contentPanel.add(label2, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 10, 0), 0, 0));

                //---- username ----
                username.setText("username");
                username.setFont(new Font("JetBrains Mono", Font.PLAIN, 16));
                contentPanel.add(username, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 10, 0), 0, 0));

                //---- label1 ----
                label1.setText("Fullname:");
                label1.setFont(new Font("JetBrains Mono", Font.PLAIN, 16));
                contentPanel.add(label1, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 10, 0), 0, 0));

                //---- fullname ----
                fullname.setFont(new Font("JetBrains Mono", Font.PLAIN, 16));
                fullname.setEditable(false);
                fullname.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        fullnameKeyPressed(e);
                    }
                });
                contentPanel.add(fullname, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 10, 0), 0, 0));

                //---- label4 ----
                label4.setText("Gender:");
                label4.setFont(new Font("JetBrains Mono", Font.PLAIN, 16));
                contentPanel.add(label4, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 10, 0), 0, 0));

                //---- gender ----
                gender.setModel(new DefaultComboBoxModel<>(new String[] {
                    "Male",
                    "Female",
                    "Orther"
                }));
                gender.setEnabled(false);
                contentPanel.add(gender, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 10, 0), 0, 0));

                //---- label5 ----
                label5.setText("Birthday:");
                label5.setFont(new Font("JetBrains Mono", Font.PLAIN, 16));
                contentPanel.add(label5, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 10, 0), 0, 0));
                contentPanel.add(birthday, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 10, 0), 0, 0));

                //---- label6 ----
                label6.setText("Email:");
                label6.setFont(new Font("JetBrains Mono", Font.PLAIN, 16));
                contentPanel.add(label6, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));

                //---- email ----
                email.setFont(new Font("JetBrains Mono", Font.PLAIN, 16));
                email.setEditable(false);
                contentPanel.add(email, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
            }
            dialogPane.add(contentPanel);
            contentPanel.setBounds(12, 123, 389, 246);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 0, 85, 80};
                ((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0, 0.0};

                //---- edit ----
                edit.setText("EDIT");
                edit.addActionListener(e -> editActionPerformed(e));
                buttonBar.add(edit, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

                //---- save ----
                save.setText("SAVE");
                save.setEnabled(false);
                save.addActionListener(e -> saveActionPerformed(e));
                buttonBar.add(save, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

                //---- cancelButton ----
                cancelButton.setText("CLOSE");
                cancelButton.addActionListener(e -> cancelButtonActionPerformed(e));
                buttonBar.add(cancelButton, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
            }
            dialogPane.add(buttonBar);
            buttonBar.setBounds(12, 375, 389, buttonBar.getPreferredSize().height);

            //======== panel3 ========
            {
                panel3.setLayout(new FlowLayout());

                //---- avatar ----
                avatar.setIcon(new ImageIcon(getClass().getResource("/image/user.png")));
                panel3.add(avatar);
            }
            dialogPane.add(panel3);
            panel3.setBounds(12, 12, 389, 88);

            //---- changeAvatar ----
            changeAvatar.setText("Change Avatar");
            changeAvatar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            changeAvatar.setForeground(Color.gray);
            changeAvatar.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    updateAvatarMouseClicked(e);
                }
                @Override
                public void mouseEntered(MouseEvent e) {
                    changeColorMouseEntered(e);
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    changeAvatarMouseExited(e);
                }
            });
            dialogPane.add(changeAvatar);
            changeAvatar.setBounds(new Rectangle(new Point(170, 105), changeAvatar.getPreferredSize()));

            {
                // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < dialogPane.getComponentCount(); i++) {
                    Rectangle bounds = dialogPane.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = dialogPane.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                dialogPane.setMinimumSize(preferredSize);
                dialogPane.setPreferredSize(preferredSize);
            }
        }
        contentPane.add(dialogPane);
        dialogPane.setBounds(0, 0, 413, 415);

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
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel label2;
    private JLabel username;
    private JLabel label1;
    private JTextField fullname;
    private JLabel label4;
    private JComboBox<String> gender;
    private JLabel label5;
    private JDateChooser birthday;
    private JLabel label6;
    private JTextField email;
    private JPanel buttonBar;
    private JButton edit;
    private JButton save;
    private JButton cancelButton;
    private JPanel panel3;
    private JLabel avatar;
    private JLabel changeAvatar;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

}
