import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.Color;

public class ChatList extends JPanel {
	
	public ChatList() {
		ImageIcon ChatIcon = new ImageIcon("/Users/zohnan/Documents/btn_image/chat.png");
		JLabel image = new JLabel();
		image.setIcon(ChatIcon);
		add(image);
		
		JButton ChatTitle = new JButton("userlist");
		ChatTitle.setBorderPainted(false);
		add(ChatTitle);		
	}

}
