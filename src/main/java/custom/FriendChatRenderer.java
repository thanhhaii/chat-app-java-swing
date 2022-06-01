package custom;

import entity.Account;
import entity.Message;

import javax.swing.*;
import java.awt.*;

public class FriendChatRenderer extends JPanel implements ListCellRenderer<Account> {

    private JLabel lbIcon = new JLabel();
    private JLabel lbName = new JLabel();

    public FriendChatRenderer(){
        setLayout(new BorderLayout(5,5));
        JPanel panelText = new JPanel(new GridLayout(0, 1));
        panelText.add(lbName);
        add(lbIcon,BorderLayout.WEST);
        add(panelText,BorderLayout.CENTER);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Account> list, Account value, int index, boolean isSelected, boolean cellHasFocus) {
        lbIcon.setIcon(new ImageIcon(new ImageIcon(value.getAvatar()).getImage().getScaledInstance(50,50, Image.SCALE_SMOOTH)));
        lbName.setText(value.getFullname());
        lbName.setFont(getFont().deriveFont(18.0f));
        lbName.setForeground(Color.white);

        lbIcon.setOpaque(true);
        lbName.setOpaque(true);

        if(isSelected){
            lbName.setBackground(list.getSelectionBackground());
            lbIcon.setBackground(list.getSelectionBackground());
            setBackground(list.getSelectionBackground());
        }else {
            lbName.setBackground(list.getBackground());
            lbIcon.setBackground(list.getBackground());
            setBackground(list.getBackground());
        }

        return this;
    }
}
