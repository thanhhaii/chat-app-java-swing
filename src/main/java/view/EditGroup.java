/*
 * Created by JFormDesigner on Fri Oct 30 16:00:18 ICT 2020
 */

package view;

import javax.swing.border.*;
import custom.GroupChatRenderer;
import custom.MemberGroupRenderer;
import dao.AccountDao;
import dao.MessageDao;
import entity.Account;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Base64;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * @author Thanh Hai
 */
public class EditGroup extends JDialog {
    public EditGroup(JFrame owner, int roomId) {
        super(owner, true);
        initComponents();
        var model = new DefaultListModel<Account>();
        new MessageDao().defaultGroupChat(roomId).forEach(group -> {
            nameGroup.setText(group.getNameGroup());
            avatarGroup.setIcon(new ImageIcon(new ImageIcon(group.getIconGroup()).getImage().getScaledInstance(70,70,Image.SCALE_SMOOTH)));
            new AccountDao().getMemberGroupChat(roomId).forEach(member -> {
                model.addElement(new Account(member.getAvatar(), member.getFullname()));
            });
        });
        member.setCellRenderer(new MemberGroupRenderer());
        member.setModel(model);
    }

    private void closeFormActionPerformed(ActionEvent e) {
        this.dispose();
    }

    private void changeColorMouseEntered(MouseEvent e) {
        changeAvatarGroup.setForeground(Color.BLACK);
    }

    private void changeAvatarMouseExited(MouseEvent e) {
        changeAvatarGroup.setForeground(Color.GRAY);
    }

    private void saveChangeActionPerformed(ActionEvent e) {
        new MessageDao().updateGroup("data/iconGroup/"+Main.roomId+".jpg", nameGroup.getText());
        dispose();
    }

    private void changeAvatarGroupMouseClicked(MouseEvent e) {
        var chooser = new JFileChooser();
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("*.Images", "jpg","png"));
        chooser.setAcceptAllFileFilterUsed(false);
        var retVal = chooser.showSaveDialog(null);
        if(retVal == JFileChooser.APPROVE_OPTION){
            var path = chooser.getSelectedFile().getAbsolutePath();
            avatarGroup.setIcon(new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH)));
            decoder(encoder(path), "data/iconGroup/"+Main.roomId+".jpg");
            new MessageDao().updateGroup("data/iconGroup/"+Main.roomId+".jpg", nameGroup.getText());
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

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        avatar = new JPanel();
        avatarGroup = new JLabel();
        changeAvatarGroup = new JLabel();
        panel1 = new JPanel();
        label2 = new JLabel();
        nameGroup = new JTextField();
        scrollPane1 = new JScrollPane();
        member = new JList();
        panel2 = new JPanel();
        button1 = new JButton();
        button2 = new JButton();

        //======== this ========
        setTitle("Edit Group Chat");
        var contentPane = getContentPane();
        contentPane.setLayout(new GridBagLayout());
        ((GridBagLayout)contentPane.getLayout()).columnWidths = new int[] {398, 0};
        ((GridBagLayout)contentPane.getLayout()).rowHeights = new int[] {106, 0, 48, 318, 37, 0};
        ((GridBagLayout)contentPane.getLayout()).columnWeights = new double[] {0.0, 1.0E-4};
        ((GridBagLayout)contentPane.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};

        //======== avatar ========
        {
            avatar.setLayout(new FlowLayout());
            avatar.add(avatarGroup);
        }
        contentPane.add(avatar, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 5, 0), 0, 0));

        //---- changeAvatarGroup ----
        changeAvatarGroup.setText("Change Avatar Group");
        changeAvatarGroup.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                changeAvatarGroupMouseClicked(e);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                changeAvatarMouseExited(e);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                changeColorMouseEntered(e);
            }
        });
        contentPane.add(changeAvatarGroup, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
            new Insets(0, 0, 5, 0), 0, 0));

        //======== panel1 ========
        {
            panel1.setLayout(new GridBagLayout());
            ((GridBagLayout)panel1.getLayout()).columnWidths = new int[] {110, 283, 0};
            ((GridBagLayout)panel1.getLayout()).rowHeights = new int[] {32, 0};
            ((GridBagLayout)panel1.getLayout()).columnWeights = new double[] {0.0, 0.0, 1.0E-4};
            ((GridBagLayout)panel1.getLayout()).rowWeights = new double[] {0.0, 1.0E-4};

            //---- label2 ----
            label2.setText("Name Group:");
            label2.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            panel1.add(label2, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                new Insets(0, 0, 0, 5), 0, 0));

            //---- nameGroup ----
            nameGroup.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            panel1.add(nameGroup, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
        }
        contentPane.add(panel1, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets(0, 0, 5, 0), 0, 0));

        //======== scrollPane1 ========
        {

            //---- member ----
            member.setFixedCellHeight(55);
            member.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            scrollPane1.setViewportView(member);
        }
        contentPane.add(scrollPane1, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 5, 0), 0, 0));

        //======== panel2 ========
        {
            panel2.setLayout(new FlowLayout(FlowLayout.RIGHT));

            //---- button1 ----
            button1.setText("Save");
            button1.addActionListener(e -> saveChangeActionPerformed(e));
            panel2.add(button1);

            //---- button2 ----
            button2.setText("Close");
            button2.addActionListener(e -> closeFormActionPerformed(e));
            panel2.add(button2);
        }
        contentPane.add(panel2, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 0), 0, 0));
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel avatar;
    private JLabel avatarGroup;
    private JLabel changeAvatarGroup;
    private JPanel panel1;
    private JLabel label2;
    private JTextField nameGroup;
    private JScrollPane scrollPane1;
    private JList member;
    private JPanel panel2;
    private JButton button1;
    private JButton button2;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
