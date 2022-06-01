/*
 * Created by JFormDesigner on Sun Oct 04 22:21:28 ICT 2020
 */

package view;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import custom.FriendChatRenderer;
import custom.GroupChatRenderer;
import custom.MemberGroupRenderer;
import dao.MessageDao;
import dao.AccountDao;
import entity.Account;
import entity.Message;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.text.*;

/**
 * @author hai
 */
public class Main extends JFrame {
    public Main(DataInputStream dis, DataOutputStream dos) {
        initComponents();
        this.dis = dis;
        this.dos = dos;
        chat.doClick();
        receiver = new Thread(new Receiver(dis));
        receiver.start();
        var countLength = listChat.getModel().getSize();
        if(countLength != 0) {
            setSelectedFriend(0);
        }
        setIconEmoji();
    }

    private void setIconEmoji() {
        angryEmoji.setIcon(new ImageIcon("data\\emoji\\angry.png"));
        appleEmoji.setIcon(new ImageIcon("data\\emoji\\apple.png"));
        australiaEmoji.setIcon(new ImageIcon("data\\emoji\\australia.png"));
        bigSimleEmoji.setIcon(new ImageIcon("data\\emoji\\big-smile.png"));
        cakeEmoji.setIcon(new ImageIcon("data\\emoji\\cake.png"));
        canadaEmoji.setIcon(new ImageIcon("data\\emoji\\canada.png"));
        cherryEmoji.setIcon(new ImageIcon("data\\emoji\\cherry.png"));
        confusedEmoji.setIcon(new ImageIcon("data\\emoji\\confused.png"));
        egyptEmoji.setIcon(new ImageIcon("data\\emoji\\egypt.png"));
        happyEmoji.setIcon(new ImageIcon("data\\emoji\\happy.png"));
        italyEmoji.setIcon(new ImageIcon("data\\emoji\\italy.png"));
        loveEmoji.setIcon(new ImageIcon("data\\emoji\\love.png"));
        madEmoji.setIcon(new ImageIcon("data\\emoji\\mad.png"));
        ninjaEmoji.setIcon(new ImageIcon("data\\emoji\\ninja.png"));
        orangeEmoji.setIcon(new ImageIcon("data\\emoji\\orange.png"));
        sadEmoji.setIcon(new ImageIcon("data\\emoji\\sad.png"));
        smileEmoji.setIcon(new ImageIcon("data\\emoji\\smile.png"));
        spainEmoji.setIcon(new ImageIcon("data\\emoji\\spain.png"));
        suspiciousEmoji.setIcon(new ImageIcon("data\\emoji\\suspicious.png"));
        swedenEmoji.setIcon(new ImageIcon("data\\emoji\\sweden.png"));
        ukEmoji.setIcon(new ImageIcon("data\\emoji\\uk.png"));
        unhappyEmoji.setIcon(new ImageIcon("data\\emoji\\unhappy.png"));
        usEmoji.setIcon(new ImageIcon("data\\emoji\\us.png"));
        vietnamEmoji.setIcon(new ImageIcon("data\\emoji\\vietnam.png"));
    }

    private void autoScroll() {
        scrollPane2.getVerticalScrollBar().setValue(scrollPane2.getVerticalScrollBar().getMaximum());
    }

    private void loadMessageFriend(String usernameFriend) {
        new MessageDao().loadMessage(usernameFriend).forEach(message -> {
            var yourMessage = message.getUsernameSend().equals(Login.usernameUser);
            switch (message.getType()) {
                case "text" -> {
                    newMessage(message.getUsernameSend(), message.getContent(), yourMessage);
                }
                case "emoji" -> {
                    newEmoji(message.getUsernameSend(), message.getContent(), yourMessage);
                }
                case "file" -> {
                    var filename = message.getContent().split("/")[1];
                    byte[] selectedFile = new byte[(int) message.getContent().length()];
                    newFile(message.getUsernameSend(), filename, selectedFile, message.getUsernameSend().equals(Login.usernameUser));
                }
            }
        });
    }

    private void loadMessageGroup(String roomId){
        showChat.setText("");
        new MessageDao().loadMessageGroup(Integer.parseInt(roomId)).forEach(message -> {
            var yourMessage = message.getUsernameSend().equals(Login.usernameUser);
            switch (message.getType()){
                case "text" -> {
                    newMessage(message.getUsernameSend(), message.getContent(), yourMessage);
                }
                case "emoji" -> {
                    newEmoji(message.getUsernameSend(), message.getContent(), yourMessage);
                }
                case "file" -> {
                    var filename = message.getContent().split("/")[1];
                    byte[] selectedFile = new byte[(int) message.getContent().length()];
                    newFile(message.getUsernameSend(), filename, selectedFile, message.getUsernameSend().equals(Login.usernameUser));
                }
            }
        });
    }



    private void newMessage(String username, String message, Boolean yourMessage) {
        StyledDocument doc = showChat.getStyledDocument();
        new MessageDao().printNameSend(yourMessage, doc, username);
        Style messageStyle = doc.getStyle("Message style");
        if (messageStyle == null) {
            messageStyle = doc.addStyle("Message style", null);
            StyleConstants.setForeground(messageStyle, Color.BLACK);
            StyleConstants.setBold(messageStyle, false);
        }
        try {
            doc.insertString(doc.getLength(), message + "\n", messageStyle);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        autoScroll();
    }

    private void newEmoji(String username, String emoji, Boolean yourMessage) {
        StyledDocument doc = showChat.getStyledDocument();
        var userStyle = new MessageDao().printNameSend(yourMessage, doc, username);
        Style iconStyle = doc.getStyle("Icon style");
        if (iconStyle == null) {
            iconStyle = doc.addStyle("Icon style", null);
        }
        StyleConstants.setIcon(iconStyle, new ImageIcon(emoji));
        try {
            doc.insertString(doc.getLength(), "invisible text", iconStyle);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        try {
            doc.insertString(doc.getLength(), "\n", userStyle);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        autoScroll();
    }

    private void newFile(String username, String filename, byte[] file, boolean yourMessage) {
        StyledDocument doc = showChat.getStyledDocument();
        var userStyle = new MessageDao().printNameSend(yourMessage, doc, username);
        Style linkStyle = doc.getStyle("Link style");
        if (linkStyle == null) {
            linkStyle = doc.addStyle("Link style", null);
            StyleConstants.setForeground(linkStyle, Color.BLUE);
            StyleConstants.setUnderline(linkStyle, true);
            StyleConstants.setBold(linkStyle, true);
            linkStyle.addAttribute("link", new HyperLinkListener(filename, file));
        }
        if (showChat.getMouseListeners() != null) {
            showChat.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    Element ele = doc.getCharacterElement(showChat.viewToModel2D(e.getPoint()));
                    AttributeSet as = ele.getAttributes();
                    HyperLinkListener listener = (HyperLinkListener) as.getAttribute("link");
                    if (listener != null) {
                        listener.execute();
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
        }
        try {
            doc.insertString(doc.getLength(), "<" + filename + ">", linkStyle);
        } catch (BadLocationException e1) {
            e1.printStackTrace();
        }
        try {
            doc.insertString(doc.getLength(), "\n", userStyle);
        } catch (BadLocationException e1) {
            e1.printStackTrace();
        }
        autoScroll();
    }

    private void profileActionPerformed(ActionEvent e) {
        new Profile(this).setVisible(true);
    }

    private void sendActionPerformed(ActionEvent e) {
        if(checkButton.equals("chat")) {
            new MessageDao().sendMessage(dos, usernameFriend.getText(), tfMessage.getText(), "friend");
            newMessage(Login.usernameUser, tfMessage.getText(), true);
            tfMessage.setText("");
            var selected = listChat.getSelectedIndex();
            listChat.setSelectedIndex(selected);
        }else {
            new MessageDao().sendMessage(dos, usernameFriend.getText(), tfMessage.getText(), "group");
            newMessage(Login.usernameUser, tfMessage.getText(), true);
            tfMessage.setText("");
        }
    }

    private void logout(ActionEvent e) {
        logOutCloseSocket();
    }

    private void addFriendActionPerformed(ActionEvent e) {
        new AddFriend(this).setVisible(true);
    }

    private void showChatActionPerformed(ActionEvent e) {
        loadFriendChat();
        var countLength = listChat.getModel().getSize();
        System.out.println(countLength);
        if(countLength != 0) {
            if (lastSelected >= 0) {
                setSelectedFriend(lastSelected);
            }
        }
    }

    private void setSelectedFriend(int selected){
        menuBar1.setVisible(false);
        var accountDao = new AccountDao();
        listChat.setSelectedIndex(selected);
        var usernameSelected = listChat.getModel().getElementAt(selected).toString().split("username='")[1].split("'")[0];
        fullnamefriend.setText(accountDao.getInfoUser(usernameSelected).get(0).getFullname());
        usernameFriend.setText(accountDao.getInfoUser(usernameSelected).get(0).getUsername());
        loadMessageFriend(usernameSelected);
        accountDao.userOnline(usernameSelected, statusOnOff);
    }

    private void loadFriendChat(){
        checkButton = "chat";
        var listModel = new DefaultListModel<Account>();
        new AccountDao().loadFriend().forEach(user -> {
            listModel.addElement(new Account(user.getAvatar(), user.getFullname(), user.getUsername()));
        });
        listChat.setCellRenderer(new FriendChatRenderer());
        listChat.setModel(listModel);
    }

    private void listFriendMouseClicked(MouseEvent e) {
        var accountDao = new AccountDao();
        if (e.getClickCount() == 1 && checkButton.equals("chat")) {
            showChat.setText("");
            menuBar1.setVisible(false);
            setSelectedFriend(listChat.getSelectedIndex());
        }
    }

    private void listGroupMouseClicked(MouseEvent e) {
        var messageDao = new MessageDao();
        if(e.getClickCount() == 1 && checkButton.equals("group")){
            menuBar1.setVisible(true);
            statusOnOff.setVisible(false);
            for(int i = 0; i < messageDao.getGroupChat().size(); i++){
                if(listChat.getSelectedIndex() == i){
                    roomId = Integer.parseInt(messageDao.getGroupChat().get(i).getGroupId());
                    fullnamefriend.setText(messageDao.getGroupChat().get(i).getNameGroup());
                    usernameFriend.setText(messageDao.getGroupChat().get(i).getGroupId());
                    loadMessageGroup(usernameFriend.getText());
                    if(messageDao.checkAdminstrator(Integer.parseInt(messageDao.getGroupChat().get(i).getGroupId()))){
                        removeMember.setVisible(true);
                    }else {
                        removeMember.setVisible(false);
                    }
                }
            }
        }
    }

    private void thisWindowClosing(WindowEvent e) {
        logOutCloseSocket();
    }

    private void logOutCloseSocket() {
        var dao = new AccountDao();
        dao.online(false);
        try {
            dos.writeUTF("Log out");
            dos.flush();
            receiver.join();
            dis.close();
            dos.close();
        } catch (IOException | InterruptedException ioException) {
            ioException.printStackTrace();
        }
        this.setVisible(false);
        new Login(this).setVisible(true);
    }

    private void btnSearchActionPerformed(ActionEvent e) {
        if (checkButton.equals("chat")) {
            var listModel = new DefaultListModel<Account>();
            new AccountDao().searchFriend(tfSearch.getText()).forEach(Account -> {
                listModel.addElement(new Account(Account.getAvatar(), Account.getFullname(), Account.getUsername(), Account.getId()));
            });
            listChat.setCellRenderer(new FriendChatRenderer());
            listChat.setModel(listModel);
        }
    }

    private void thisKeyPressed(KeyEvent e) {
        if (((KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, java.awt.event.InputEvent.ALT_DOWN_MASK)) != null) && e.getKeyCode() == KeyEvent.VK_F4) {
            logOutCloseSocket();
        }

    }

    private void tfMessageKeyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER && checkButton.equals("chat")) {
            newMessage(Login.usernameUser, tfMessage.getText(), true);
            new MessageDao().sendMessage(dos, usernameFriend.getText(), tfMessage.getText(), "friend");
            tfMessage.setText("");
            var selected = listChat.getSelectedIndex();
            listChat.setSelectedIndex(selected);
        }else if(e.getKeyCode() == KeyEvent.VK_ENTER && checkButton.equals("group")){
            newMessage(Login.usernameUser, tfMessage.getText(), true);
            new MessageDao().sendMessage(dos, usernameFriend.getText(), tfMessage.getText(), "group");
            tfMessage.setText("");
        }
    }

    private void EmojiMouseClicked(MouseEvent e) {
        var emoji = (e.getSource().toString().split("defaultIcon=")[1].split(","))[0];
        if (tfMessage.isEnabled()) {
            try {
                if(checkButton.equals("chat")) {
                    dos.writeUTF("Emoji");
                    new MessageDao().insertMessage(usernameFriend.getText(), "emoji", emoji);
                }else {
                    dos.writeUTF("EmojiGroup");
                    new MessageDao().insertMessageGroup(usernameFriend.getText(), "emoji", emoji);
                }
                dos.writeUTF(usernameFriend.getText());
                dos.writeUTF(emoji);
                dos.flush();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            if(checkButton.equals("chat")) {
                var i = listChat.getSelectedIndex();
                listChat.setSelectedIndex(i);
            }
            newEmoji(Login.usernameUser, emoji, true);
        }
    }

    private void sendFileActionPerformed(ActionEvent e) {
        var jFileChooser = new JFileChooser();
        int rVal = jFileChooser.showOpenDialog(panel3.getParent());
        if (rVal == JFileChooser.APPROVE_OPTION) {
            byte[] selectedFile = new byte[(int) jFileChooser.getSelectedFile().length()];
            BufferedInputStream bis;
            try (var fos = new FileOutputStream("file/" + jFileChooser.getSelectedFile().getName())) {
                bis = new BufferedInputStream(new FileInputStream(jFileChooser.getSelectedFile()));
                bis.read(selectedFile, 0, selectedFile.length);
                fos.write(selectedFile);
                if(checkButton.equals("chat")) {
                    dos.writeUTF("File");
                    new MessageDao().insertMessage(usernameFriend.getText(), "file", "file/" + jFileChooser.getSelectedFile().getName());
                }else if(checkButton.equals("group")){
                    dos.writeUTF("FileGroup");
                    new MessageDao().insertMessageGroup(usernameFriend.getText(),"file", "file/" + jFileChooser.getSelectedFile().getName());
                }
                dos.writeUTF(usernameFriend.getText()); //gui username thi gui ten room
                dos.writeUTF(jFileChooser.getSelectedFile().getName()); //gui name file
                dos.writeUTF(String.valueOf(selectedFile.length)); //gui do dai cua file

                int size = selectedFile.length;
                int bufferSize = 2048;
                int offset = 0;

                while (size > 0) {
                    dos.write(selectedFile, offset, Math.min(size, bufferSize));
                    offset += Math.min(size, bufferSize);
                    size -= bufferSize;
                }
                dos.flush();
                bis.close();
                newFile(Login.usernameUser, jFileChooser.getSelectedFile().getName(), selectedFile, true);
            } catch (IOException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
        }
    }

    private void newGroupChatActionPerformed(ActionEvent e) {
        new NewGroupChat(this).setVisible(true);
    }

    private void chatGroupActionPerformed(ActionEvent e) {
        lastSelected = listChat.getSelectedIndex();
        var messageDao = new MessageDao();
        checkButton = "group";
        listChat.setCellRenderer(new GroupChatRenderer());
        var model = new DefaultListModel<Message>();
        new MessageDao().getGroupChat().forEach(value -> {
            model.addElement(new Message(value.getIconGroup(), value.getNameGroup()));
        });
        listChat.setModel(model);
        menuBar1.setVisible(true);
        statusOnOff.setVisible(false);
        listChat.setSelectedIndex(0);
        for(int i = 0; i < messageDao.getGroupChat().size(); i++){
            if(listChat.getSelectedIndex() == i){
                roomId = Integer.parseInt(messageDao.getGroupChat().get(i).getGroupId());
                fullnamefriend.setText(messageDao.getGroupChat().get(i).getNameGroup());
                usernameFriend.setText(messageDao.getGroupChat().get(i).getGroupId());
                loadMessageGroup(usernameFriend.getText());
                break;
            }
        }
    }

    private void changePasswordActionPerformed(ActionEvent e) {
        new ChangePassword(this).setVisible(true);
    }

    private void EditGroupActionPerformed(ActionEvent e) {
        new EditGroup(this, Integer.parseInt(usernameFriend.getText())).setVisible(true);
        chatGroup.doClick();
    }

    private void addMemberActionPerformed(ActionEvent e) {
        action = "add";
        new AddRemoveMemberGroup(this).setVisible(true);
    }

    private void removeMemberActionPerformed(ActionEvent e) {
        action = "remove";
        new AddRemoveMemberGroup(this).setVisible(true);
    }

    private void OutGroupActionPerformed(ActionEvent e) {
        new AccountDao().deleteMemberGroup(Integer.parseInt(usernameFriend.getText()), Login.usernameUser);
        chatGroup.doClick();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        panel1 = new JPanel();
        label1 = new JButton();
        logOut = new JButton();
        addFriend = new JButton();
        chat = new JButton();
        chatGroup = new JButton();
        newGroupChat = new JButton();
        changePassword = new JButton();
        panel2 = new JPanel();
        chatClient = new JScrollPane();
        listChat = new JList();
        tfSearch = new JTextField();
        btnSearch = new JButton();
        panel3 = new JPanel();
        panel4 = new JPanel();
        panel8 = new JPanel();
        fullnamefriend = new JLabel();
        usernameFriend = new JLabel();
        menuBar1 = new JMenuBar();
        menu2 = new JMenu();
        menuItem2 = new JMenuItem();
        menuItem3 = new JMenuItem();
        removeMember = new JMenuItem();
        menuItem5 = new JMenuItem();
        statusOnOff = new JLabel();
        panel5 = new JPanel();
        scrollPane2 = new JScrollPane();
        showChat = new JTextPane();
        panel9 = new JPanel();
        angryEmoji = new JLabel();
        appleEmoji = new JLabel();
        australiaEmoji = new JLabel();
        bigSimleEmoji = new JLabel();
        cakeEmoji = new JLabel();
        canadaEmoji = new JLabel();
        cherryEmoji = new JLabel();
        confusedEmoji = new JLabel();
        egyptEmoji = new JLabel();
        happyEmoji = new JLabel();
        italyEmoji = new JLabel();
        loveEmoji = new JLabel();
        madEmoji = new JLabel();
        ninjaEmoji = new JLabel();
        orangeEmoji = new JLabel();
        sadEmoji = new JLabel();
        smileEmoji = new JLabel();
        spainEmoji = new JLabel();
        suspiciousEmoji = new JLabel();
        swedenEmoji = new JLabel();
        ukEmoji = new JLabel();
        unhappyEmoji = new JLabel();
        usEmoji = new JLabel();
        vietnamEmoji = new JLabel();
        panel6 = new JPanel();
        tfMessage = new JTextField();
        button2 = new JButton();
        button1 = new JButton();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setTitle("Chat App");
        setResizable(false);
        setUndecorated(true);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                thisWindowClosing(e);
            }
        });
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                thisKeyPressed(e);
            }
        });
        var contentPane = getContentPane();

        //======== panel1 ========
        {
            panel1.setBackground(new Color(48, 56, 65));
            panel1.setLayout(null);

            //---- label1 ----
            label1.setIcon(new ImageIcon(getClass().getResource("/image/user.png")));
            label1.setBackground(new Color(48, 56, 65));
            label1.addActionListener(e -> profileActionPerformed(e));
            panel1.add(label1);
            label1.setBounds(10, 50, 70, 70);

            //---- logOut ----
            logOut.setBackground(new Color(48, 56, 65));
            logOut.setIcon(new ImageIcon(getClass().getResource("/image/logout.png")));
            logOut.addActionListener(e -> logout(e));
            panel1.add(logOut);
            logOut.setBounds(20, 755, 45, 48);

            //---- addFriend ----
            addFriend.setBackground(new Color(48, 56, 65));
            addFriend.setIcon(new ImageIcon(getClass().getResource("/image/addFriend.png")));
            addFriend.setToolTipText("Show request or find new friend");
            addFriend.addActionListener(e -> addFriendActionPerformed(e));
            panel1.add(addFriend);
            addFriend.setBounds(20, 380, 45, 48);

            //---- chat ----
            chat.setBackground(new Color(48, 56, 65));
            chat.setIcon(new ImageIcon(getClass().getResource("/image/chat.png")));
            chat.setToolTipText("Show chat with friend or group");
            chat.addActionListener(e -> showChatActionPerformed(e));
            panel1.add(chat);
            chat.setBounds(20, 310, 45, 48);

            //---- chatGroup ----
            chatGroup.setBackground(new Color(48, 56, 65));
            chatGroup.setIcon(new ImageIcon(getClass().getResource("/image/friend.png")));
            chatGroup.setToolTipText("Chat Group");
            chatGroup.addActionListener(e -> chatGroupActionPerformed(e));
            panel1.add(chatGroup);
            chatGroup.setBounds(20, 240, 45, 48);

            //---- newGroupChat ----
            newGroupChat.setBackground(new Color(48, 56, 65));
            newGroupChat.setIcon(new ImageIcon(getClass().getResource("/image/newGroupchat.png")));
            newGroupChat.setToolTipText("New Group Chat");
            newGroupChat.addActionListener(e -> newGroupChatActionPerformed(e));
            panel1.add(newGroupChat);
            newGroupChat.setBounds(20, 450, 45, 48);

            //---- changePassword ----
            changePassword.setBackground(new Color(48, 56, 65));
            changePassword.setIcon(new ImageIcon(getClass().getResource("/image/changepassword.png")));
            changePassword.addActionListener(e -> changePasswordActionPerformed(e));
            panel1.add(changePassword);
            changePassword.setBounds(20, 690, 45, 48);

            {
                // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < panel1.getComponentCount(); i++) {
                    Rectangle bounds = panel1.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = panel1.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                panel1.setMinimumSize(preferredSize);
                panel1.setPreferredSize(preferredSize);
            }
        }

        //======== panel2 ========
        {
            panel2.setBackground(new Color(54, 62, 71));

            //======== chatClient ========
            {
                chatClient.setBackground(new Color(54, 62, 71));
                chatClient.setForeground(Color.white);
                chatClient.setBorder(null);

                //---- listChat ----
                listChat.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                listChat.setFont(new Font("Segoe UI", Font.PLAIN, 20));
                listChat.setFixedCellHeight(60);
                listChat.setBackground(new Color(54, 62, 71));
                listChat.setForeground(Color.white);
                listChat.setBorder(null);
                listChat.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                listChat.setSelectionBackground(new Color(45, 50, 45));
                listChat.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        listFriendMouseClicked(e);
                        listGroupMouseClicked(e);
                    }
                });
                chatClient.setViewportView(listChat);
            }

            //---- btnSearch ----
            btnSearch.setIcon(new ImageIcon(getClass().getResource("/image/search.png")));
            btnSearch.setBackground(new Color(54, 62, 71));
            btnSearch.addActionListener(e -> btnSearchActionPerformed(e));

            GroupLayout panel2Layout = new GroupLayout(panel2);
            panel2.setLayout(panel2Layout);
            panel2Layout.setHorizontalGroup(
                panel2Layout.createParallelGroup()
                    .addGroup(panel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panel2Layout.createParallelGroup()
                            .addGroup(panel2Layout.createSequentialGroup()
                                .addComponent(chatClient, GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
                                .addContainerGap())
                            .addGroup(panel2Layout.createSequentialGroup()
                                .addComponent(tfSearch, GroupLayout.PREFERRED_SIZE, 233, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSearch, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            );
            panel2Layout.setVerticalGroup(
                panel2Layout.createParallelGroup()
                    .addGroup(GroupLayout.Alignment.TRAILING, panel2Layout.createSequentialGroup()
                        .addGap(0, 11, Short.MAX_VALUE)
                        .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addComponent(tfSearch)
                            .addComponent(btnSearch, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addComponent(chatClient, GroupLayout.PREFERRED_SIZE, 746, GroupLayout.PREFERRED_SIZE))
            );
        }

        //======== panel3 ========
        {

            //======== panel4 ========
            {

                //======== panel8 ========
                {
                    panel8.setLayout(new FormLayout(
                        "203dlu, $lcgap, 101dlu, $lcgap, 96dlu",
                        "18dlu, $lgap, 17dlu"));

                    //---- fullnamefriend ----
                    fullnamefriend.setFont(new Font("Segoe UI", Font.BOLD, 20));
                    panel8.add(fullnamefriend, cc.xy(1, 1));

                    //---- usernameFriend ----
                    usernameFriend.setVisible(false);
                    panel8.add(usernameFriend, cc.xy(3, 1));

                    //======== menuBar1 ========
                    {

                        //======== menu2 ========
                        {
                            menu2.setText("Edit group chat");

                            //---- menuItem2 ----
                            menuItem2.setText("Customize the chat");
                            menuItem2.addActionListener(e -> EditGroupActionPerformed(e));
                            menu2.add(menuItem2);

                            //---- menuItem3 ----
                            menuItem3.setText("Add member");
                            menuItem3.addActionListener(e -> addMemberActionPerformed(e));
                            menu2.add(menuItem3);

                            //---- removeMember ----
                            removeMember.setText("Remove member");
                            removeMember.addActionListener(e -> removeMemberActionPerformed(e));
                            menu2.add(removeMember);

                            //---- menuItem5 ----
                            menuItem5.setText("Out group");
                            menuItem5.addActionListener(e -> OutGroupActionPerformed(e));
                            menu2.add(menuItem5);
                        }
                        menuBar1.add(menu2);
                    }
                    panel8.add(menuBar1, cc.xy(5, 1));

                    //---- statusOnOff ----
                    statusOnOff.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                    panel8.add(statusOnOff, cc.xy(1, 3));
                }

                //======== panel5 ========
                {
                    panel5.setLayout(new GridLayout());

                    //======== scrollPane2 ========
                    {

                        //---- showChat ----
                        showChat.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                        showChat.setEditable(false);
                        scrollPane2.setViewportView(showChat);
                    }
                    panel5.add(scrollPane2);
                }

                //======== panel9 ========
                {
                    panel9.setLayout(new FormLayout(
                        "23*(default, $lcgap), default",
                        "16dlu"));

                    //---- angryEmoji ----
                    angryEmoji.setIcon(new ImageIcon(getClass().getResource("/emoji/angry.png")));
                    angryEmoji.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    angryEmoji.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            EmojiMouseClicked(e);
                        }
                    });
                    panel9.add(angryEmoji, cc.xy(1, 1));

                    //---- appleEmoji ----
                    appleEmoji.setIcon(new ImageIcon(getClass().getResource("/emoji/apple.png")));
                    appleEmoji.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    appleEmoji.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            EmojiMouseClicked(e);
                        }
                    });
                    panel9.add(appleEmoji, cc.xy(3, 1));

                    //---- australiaEmoji ----
                    australiaEmoji.setIcon(new ImageIcon(getClass().getResource("/emoji/australia.png")));
                    australiaEmoji.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    australiaEmoji.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            EmojiMouseClicked(e);
                        }
                    });
                    panel9.add(australiaEmoji, cc.xy(5, 1));

                    //---- bigSimleEmoji ----
                    bigSimleEmoji.setIcon(new ImageIcon(getClass().getResource("/emoji/big-smile.png")));
                    bigSimleEmoji.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    bigSimleEmoji.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            EmojiMouseClicked(e);
                        }
                    });
                    panel9.add(bigSimleEmoji, cc.xy(7, 1, CellConstraints.FILL, CellConstraints.DEFAULT));

                    //---- cakeEmoji ----
                    cakeEmoji.setIcon(new ImageIcon(getClass().getResource("/emoji/cake.png")));
                    cakeEmoji.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    cakeEmoji.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            EmojiMouseClicked(e);
                        }
                    });
                    panel9.add(cakeEmoji, cc.xy(9, 1));

                    //---- canadaEmoji ----
                    canadaEmoji.setIcon(new ImageIcon(getClass().getResource("/emoji/canada.png")));
                    canadaEmoji.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    canadaEmoji.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            EmojiMouseClicked(e);
                        }
                    });
                    panel9.add(canadaEmoji, cc.xy(11, 1));

                    //---- cherryEmoji ----
                    cherryEmoji.setIcon(new ImageIcon(getClass().getResource("/emoji/cherry.png")));
                    cherryEmoji.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    cherryEmoji.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            EmojiMouseClicked(e);
                        }
                    });
                    panel9.add(cherryEmoji, cc.xy(13, 1));

                    //---- confusedEmoji ----
                    confusedEmoji.setIcon(new ImageIcon(getClass().getResource("/emoji/confused.png")));
                    confusedEmoji.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    confusedEmoji.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            EmojiMouseClicked(e);
                        }
                    });
                    panel9.add(confusedEmoji, cc.xy(15, 1));

                    //---- egyptEmoji ----
                    egyptEmoji.setIcon(new ImageIcon(getClass().getResource("/emoji/egypt.png")));
                    egyptEmoji.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    egyptEmoji.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            EmojiMouseClicked(e);
                        }
                    });
                    panel9.add(egyptEmoji, cc.xy(17, 1));

                    //---- happyEmoji ----
                    happyEmoji.setIcon(new ImageIcon(getClass().getResource("/emoji/happy.png")));
                    happyEmoji.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    happyEmoji.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            EmojiMouseClicked(e);
                        }
                    });
                    panel9.add(happyEmoji, cc.xy(19, 1));

                    //---- italyEmoji ----
                    italyEmoji.setIcon(new ImageIcon(getClass().getResource("/emoji/italy.png")));
                    italyEmoji.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    italyEmoji.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            EmojiMouseClicked(e);
                        }
                    });
                    panel9.add(italyEmoji, cc.xy(21, 1));

                    //---- loveEmoji ----
                    loveEmoji.setIcon(new ImageIcon(getClass().getResource("/emoji/love.png")));
                    loveEmoji.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    loveEmoji.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            EmojiMouseClicked(e);
                        }
                    });
                    panel9.add(loveEmoji, cc.xy(23, 1));

                    //---- madEmoji ----
                    madEmoji.setIcon(new ImageIcon(getClass().getResource("/emoji/mad.png")));
                    madEmoji.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    madEmoji.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            EmojiMouseClicked(e);
                        }
                    });
                    panel9.add(madEmoji, cc.xy(25, 1));

                    //---- ninjaEmoji ----
                    ninjaEmoji.setIcon(new ImageIcon(getClass().getResource("/emoji/ninja.png")));
                    ninjaEmoji.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    ninjaEmoji.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            EmojiMouseClicked(e);
                        }
                    });
                    panel9.add(ninjaEmoji, cc.xy(27, 1));

                    //---- orangeEmoji ----
                    orangeEmoji.setIcon(new ImageIcon(getClass().getResource("/emoji/orange.png")));
                    orangeEmoji.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    orangeEmoji.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            EmojiMouseClicked(e);
                        }
                    });
                    panel9.add(orangeEmoji, cc.xy(29, 1));

                    //---- sadEmoji ----
                    sadEmoji.setIcon(new ImageIcon(getClass().getResource("/emoji/sad.png")));
                    sadEmoji.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    sadEmoji.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            EmojiMouseClicked(e);
                        }
                    });
                    panel9.add(sadEmoji, cc.xy(31, 1));

                    //---- smileEmoji ----
                    smileEmoji.setIcon(new ImageIcon(getClass().getResource("/emoji/smile.png")));
                    smileEmoji.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    smileEmoji.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            EmojiMouseClicked(e);
                        }
                    });
                    panel9.add(smileEmoji, cc.xy(33, 1));

                    //---- spainEmoji ----
                    spainEmoji.setIcon(new ImageIcon(getClass().getResource("/emoji/spain.png")));
                    spainEmoji.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    spainEmoji.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            EmojiMouseClicked(e);
                        }
                    });
                    panel9.add(spainEmoji, cc.xy(35, 1));

                    //---- suspiciousEmoji ----
                    suspiciousEmoji.setIcon(new ImageIcon(getClass().getResource("/emoji/suspicious.png")));
                    suspiciousEmoji.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    suspiciousEmoji.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            EmojiMouseClicked(e);
                        }
                    });
                    panel9.add(suspiciousEmoji, cc.xy(37, 1));

                    //---- swedenEmoji ----
                    swedenEmoji.setIcon(new ImageIcon(getClass().getResource("/emoji/sweden.png")));
                    swedenEmoji.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    swedenEmoji.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            EmojiMouseClicked(e);
                        }
                    });
                    panel9.add(swedenEmoji, cc.xy(39, 1));

                    //---- ukEmoji ----
                    ukEmoji.setIcon(new ImageIcon(getClass().getResource("/emoji/uk.png")));
                    ukEmoji.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    ukEmoji.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            EmojiMouseClicked(e);
                        }
                    });
                    panel9.add(ukEmoji, cc.xy(41, 1));

                    //---- unhappyEmoji ----
                    unhappyEmoji.setIcon(new ImageIcon(getClass().getResource("/emoji/unhappy.png")));
                    unhappyEmoji.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    unhappyEmoji.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            EmojiMouseClicked(e);
                        }
                    });
                    panel9.add(unhappyEmoji, cc.xy(43, 1));

                    //---- usEmoji ----
                    usEmoji.setIcon(new ImageIcon(getClass().getResource("/emoji/us.png")));
                    usEmoji.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    usEmoji.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            EmojiMouseClicked(e);
                        }
                    });
                    panel9.add(usEmoji, cc.xy(45, 1));

                    //---- vietnamEmoji ----
                    vietnamEmoji.setIcon(new ImageIcon(getClass().getResource("/emoji/vietnam.png")));
                    vietnamEmoji.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    vietnamEmoji.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            EmojiMouseClicked(e);
                        }
                    });
                    panel9.add(vietnamEmoji, cc.xy(47, 1));
                }

                //======== panel6 ========
                {
                    panel6.setLayout(new FormLayout(
                        "352dlu, 2*($lcgap, 28dlu)",
                        "28dlu"));

                    //---- tfMessage ----
                    tfMessage.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                    tfMessage.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyPressed(KeyEvent e) {
                            tfMessageKeyPressed(e);
                        }
                    });
                    panel6.add(tfMessage, cc.xy(1, 1, CellConstraints.DEFAULT, CellConstraints.FILL));

                    //---- button2 ----
                    button2.setIcon(new ImageIcon(getClass().getResource("/image/attach.png")));
                    button2.addActionListener(e -> sendFileActionPerformed(e));
                    panel6.add(button2, cc.xy(3, 1, CellConstraints.FILL, CellConstraints.FILL));

                    //---- button1 ----
                    button1.setIcon(new ImageIcon(getClass().getResource("/image/send.png")));
                    button1.addActionListener(e -> sendActionPerformed(e));
                    panel6.add(button1, cc.xy(5, 1, CellConstraints.FILL, CellConstraints.FILL));
                }

                GroupLayout panel4Layout = new GroupLayout(panel4);
                panel4.setLayout(panel4Layout);
                panel4Layout.setHorizontalGroup(
                    panel4Layout.createParallelGroup()
                        .addComponent(panel8, GroupLayout.PREFERRED_SIZE, 696, GroupLayout.PREFERRED_SIZE)
                        .addComponent(panel5, GroupLayout.PREFERRED_SIZE, 696, GroupLayout.PREFERRED_SIZE)
                        .addGroup(panel4Layout.createSequentialGroup()
                            .addGap(98, 98, 98)
                            .addComponent(panel9, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addComponent(panel6, GroupLayout.PREFERRED_SIZE, 696, GroupLayout.PREFERRED_SIZE)
                );
                panel4Layout.setVerticalGroup(
                    panel4Layout.createParallelGroup()
                        .addGroup(panel4Layout.createSequentialGroup()
                            .addGap(2, 2, 2)
                            .addComponent(panel8, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addGap(8, 8, 8)
                            .addComponent(panel5, GroupLayout.PREFERRED_SIZE, 648, GroupLayout.PREFERRED_SIZE)
                            .addGap(6, 6, 6)
                            .addComponent(panel9, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addGap(8, 8, 8)
                            .addComponent(panel6, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addContainerGap(9, Short.MAX_VALUE))
                );
            }

            GroupLayout panel3Layout = new GroupLayout(panel3);
            panel3.setLayout(panel3Layout);
            panel3Layout.setHorizontalGroup(
                panel3Layout.createParallelGroup()
                    .addGroup(panel3Layout.createSequentialGroup()
                        .addComponent(panel4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
            );
            panel3Layout.setVerticalGroup(
                panel3Layout.createParallelGroup()
                    .addGroup(panel3Layout.createSequentialGroup()
                        .addComponent(panel4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
            );
        }

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addComponent(panel1, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE)
                    .addGap(5, 5, 5)
                    .addComponent(panel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addGap(5, 5, 5)
                    .addComponent(panel3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addComponent(panel1, GroupLayout.PREFERRED_SIZE, 813, GroupLayout.PREFERRED_SIZE)
                .addComponent(panel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(panel3, GroupLayout.PREFERRED_SIZE, 806, GroupLayout.PREFERRED_SIZE)
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel panel1;
    private JButton label1;
    private JButton logOut;
    private JButton addFriend;
    private JButton chat;
    private JButton chatGroup;
    private JButton newGroupChat;
    private JButton changePassword;
    private JPanel panel2;
    private JScrollPane chatClient;
    public JList listChat;
    private JTextField tfSearch;
    private JButton btnSearch;
    private JPanel panel3;
    private JPanel panel4;
    private JPanel panel8;
    private JLabel fullnamefriend;
    private JLabel usernameFriend;
    private JMenuBar menuBar1;
    private JMenu menu2;
    private JMenuItem menuItem2;
    private JMenuItem menuItem3;
    private JMenuItem removeMember;
    private JMenuItem menuItem5;
    private JLabel statusOnOff;
    private JPanel panel5;
    private JScrollPane scrollPane2;
    private JTextPane showChat;
    private JPanel panel9;
    private JLabel angryEmoji;
    private JLabel appleEmoji;
    private JLabel australiaEmoji;
    private JLabel bigSimleEmoji;
    private JLabel cakeEmoji;
    private JLabel canadaEmoji;
    private JLabel cherryEmoji;
    private JLabel confusedEmoji;
    private JLabel egyptEmoji;
    private JLabel happyEmoji;
    private JLabel italyEmoji;
    private JLabel loveEmoji;
    private JLabel madEmoji;
    private JLabel ninjaEmoji;
    private JLabel orangeEmoji;
    private JLabel sadEmoji;
    private JLabel smileEmoji;
    private JLabel spainEmoji;
    private JLabel suspiciousEmoji;
    private JLabel swedenEmoji;
    private JLabel ukEmoji;
    private JLabel unhappyEmoji;
    private JLabel usEmoji;
    private JLabel vietnamEmoji;
    private JPanel panel6;
    private JTextField tfMessage;
    private JButton button2;
    private JButton button1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
    private DataInputStream dis;
    private DataOutputStream dos;
    private static String checkButton;
    public static int roomId;
    public static String action;
    private static int lastSelected = -1;

    Thread receiver;

    class Receiver implements Runnable {

        private DataInputStream dis;

        public Receiver(DataInputStream dis) {
            this.dis = dis;
        }

        @Override
        public void run() {
            try {
                label:
                while (true) {
                    var messageDao = new MessageDao();
                    var method = dis.readUTF();
                    switch (method) {
                        case "Text" -> {
                            var sender = dis.readUTF();
                            var message = dis.readUTF();
                            var i = listChat.getSelectedIndex();
                            listChat.setSelectedIndex(i);
                            if(sender.equals(usernameFriend.getText())) {
                                newMessage(sender, message, false);
                                messageDao.seenMessage(sender, true);
                            }else {
                                messageDao.seenMessage(sender, false);
                            }
                        }
                        case "TextGroup" -> {
                            String sender = dis.readUTF();
                            String roomId = dis.readUTF();
                            String content = dis.readUTF();
                            if(usernameFriend.getText().equals(roomId) && !sender.equals(Login.usernameUser)){
                                newMessage(sender, content, false);
                            }
                        }
                        case "Log out" -> {
                            break label;
                        }
                        case "Emoji" -> {
                            var sender = dis.readUTF();
                            var emoji = dis.readUTF();
                            if(sender.equals(usernameFriend.getText())) {
                                newEmoji(sender, emoji, false);
                                messageDao.seenMessage(sender, true);
                            }else {
                                messageDao.seenMessage(sender, false);
                            }
                        }
                        case "EmojiGroup" -> {
                            var sender = dis.readUTF();
                            var roomId = dis.readUTF();
                            var emoji = dis.readUTF();
                            if(roomId.equals(usernameFriend.getText()) && !sender.equals(Login.usernameUser)){
                                newEmoji(sender, emoji, false);
                            }
                        }
                        case "File" -> {
                            String sender = dis.readUTF();
                            String filename = dis.readUTF();
                            int size = Integer.parseInt(dis.readUTF());
                            int bufferSize = 2048;
                            byte[] buffer = new byte[bufferSize];
                            ByteArrayOutputStream file = new ByteArrayOutputStream();

                            while (size > 0) {
                                dis.read(buffer, 0, Math.min(bufferSize, size));
                                file.write(buffer, 0, Math.min(bufferSize, size));
                                size -= bufferSize;
                            }

                            // In ra màn hình file đó
                            if(sender.equals(usernameFriend.getText())) {
                                newFile(sender, filename, file.toByteArray(), false);
                                messageDao.seenMessage(sender, true);
                            }else {
                                messageDao.seenMessage(sender, false);
                            }
                        }
                        case "FileGroup" -> {
                            var sender = dis.readUTF();
                            var roomId = dis.readUTF();
                            var filename = dis.readUTF();
                            int size = Integer.parseInt(dis.readUTF());
                            int bufferSize = 2048;
                            byte[] buffer = new byte[bufferSize];
                            ByteArrayOutputStream file = new ByteArrayOutputStream();
                            while (size > 0) {
                                dis.read(buffer, 0, Math.min(bufferSize, size));
                                file.write(buffer, 0, Math.min(bufferSize, size));
                                size -= bufferSize;
                            }
                            if(roomId.equals(usernameFriend.getText()) && !sender.equals(Login.usernameUser)){
                                newFile(sender, filename, file.toByteArray(), false);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class HyperLinkListener extends AbstractAction {
        String fileName;
        byte[] file;

        public HyperLinkListener(String filename, byte[] file) {
            this.fileName = filename;
            this.file = Arrays.copyOf(file, file.length);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            execute();
        }

        public void execute() {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new File(fileName));
            int rVal = fileChooser.showSaveDialog(panel4.getParent());
            if (rVal == JFileChooser.APPROVE_OPTION) {
                File saveFile = fileChooser.getSelectedFile();
                BufferedOutputStream bos = null;
                try {
                    bos = new BufferedOutputStream(new FileOutputStream(saveFile));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                int nextAction = JOptionPane.showConfirmDialog(null, "Save file to " + saveFile.getAbsolutePath() + "\nDo you want to open this file?", "Successful", JOptionPane.YES_NO_OPTION);

                if (nextAction == JOptionPane.YES_OPTION) {
                    try {
                        Desktop.getDesktop().open(saveFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (bos != null) {
                    try {
                        bos.write(this.file);
                        bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }
}

