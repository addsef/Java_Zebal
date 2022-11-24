import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.FlowLayout;
import java.awt.SystemColor;

public class FriendList extends JPanel {

	/**
	 * Create the panel.
	 */
	public FriendList(String username) {
		setLayout(null);
		
		// 인자로 username, 상태메시지, 프로필사진 ( String username, String statusmsg, ImagIcon profileicon)
		
		
		JButton btnProfileIcon = new JButton();
		btnProfileIcon.setBounds(20, 20, 50, 50);
		btnProfileIcon.setIcon(new ImageIcon("/Users/zohnan/Documents/btn_image/user.png"));
		btnProfileIcon.setBorderPainted(false);
		add(btnProfileIcon);
		
		
		JLabel lblUserName = new JLabel(username);
		lblUserName.setBounds(82, 15, 100, 20);
		lblUserName.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		add(lblUserName);
		
		JButton btnStatusMessage = new JButton("Status Message");	
		btnStatusMessage.setBounds(79, 40, 140, 30);
		btnStatusMessage.setBorderPainted(false);
		add(btnStatusMessage);
		
	}
}
