
// JavaObjClientView.java ObjecStram 기반 Client
//실질적인 채팅 창
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Color;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

//import JavaObjClientMain.Myaction;

import javax.swing.JToggleButton;
import javax.swing.JList;

public class JavaObjClientFriendView extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private String UserName;
	private String IpAddr;
	private String PortNo;
	private Vector<String> myChatRoom;
	private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의
	private Socket socket; // 연결소켓

	private ObjectInputStream ois;
	private ObjectOutputStream oos;

	private JLabel lblUserName;
	// private JTextArea textArea;
	private JTextPane textArea;
	private JTextPane myTextArea;

	private Frame frame;
	private FileDialog fd;
	private JButton imgBtn;

	public JavaObjClientView Mainview;
	public JavaObjClientChatListView ChatListview;
	//public JavaObjClientNotice noticeview;
	
	/**
	 * Create the frame.
	 */
	public JavaObjClientFriendView(String username, JavaObjClientView mainview) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 472, 668);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(93, 76, 351, 518);
		contentPane.add(scrollPane);

		 
		
		textArea = new JTextPane();
		textArea.setEditable(true);
		textArea.setFont(new Font("굴림체", Font.PLAIN, 14));
		scrollPane.setViewportView(textArea);
		
		myTextArea = new JTextPane();
		myTextArea.setFont(new Font("굴림체", Font.PLAIN, 14));
		myTextArea.setEditable(true);
		myTextArea.setBounds(92, 42, 152, 352);
		scrollPane.setColumnHeaderView(myTextArea);

		lblUserName = new JLabel("Name");  // 상단 자기 이름, 프로필
		lblUserName.setBorder(new LineBorder(new Color(0, 0, 0)));
		lblUserName.setBackground(Color.WHITE);
		lblUserName.setFont(new Font("굴림", Font.BOLD, 14));
		lblUserName.setHorizontalAlignment(SwingConstants.CENTER);
		lblUserName.setBounds(12, 20, 62, 40);
		contentPane.add(lblUserName);
		setVisible(true);

		AppendText("User " + username + "friend lists");

		
		lblUserName.setText(username);
		
		JButton btnNewButton = new JButton("종 료"); // 종료 버튼
		btnNewButton.setFont(new Font("굴림", Font.PLAIN, 14));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChatMsg msg = new ChatMsg(UserName, "400", "Bye");
				SendObject(msg);
				System.exit(0);
			}
		});
		btnNewButton.setBounds(12, 554, 69, 40);
		contentPane.add(btnNewButton);
		
		JButton btnProfileButton = new JButton("친구"); // 메인화면 버튼
		btnProfileButton.setFont(new Font("굴림", Font.PLAIN, 14));
		/*btnProfileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChatMsg msg = new ChatMsg(UserName, "900", username);
				SendObject(msg);
			}
		});*/
		btnProfileButton.setBounds(12, 97, 69, 40);
		contentPane.add(btnProfileButton);
		
		JButton btnChatListButton = new JButton("채팅"); // 채팅방 목록 버튼
		btnChatListButton.setFont(new Font("굴림", Font.PLAIN, 14));
		btnChatListButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChatMsg msg = new ChatMsg(UserName, "500", username);
				SendObject(msg);
				//JavaObjClientChatListView view;
				
				ChatListview = new JavaObjClientChatListView(UserName, Mainview);
				setVisible(false);
			} // username에 맞는 채팅방 불러오기
		});
		//ChatListAction CLAction = new ChatListAction();
		//btnChatListButton.addActionListener(CLAction);
		//JavaObjClientChatListView view = new JavaObjClientChatListView(UserName, IpAddr, PortNo);
		//setVisible(false);
		//Mainview = this;
		//ChatListView
		btnChatListButton.setBounds(12, 170, 69, 40);
		contentPane.add(btnChatListButton);
		

		
	}
	
	
	
	
	

	// Server Message를 수신해서 화면에 표시
	class ListenNetwork extends Thread {
		public void run() {
			while (true) {
				try {
					Object obcm = null;
					String msg = null;
					ChatMsg cm;
					try {
						obcm = ois.readObject();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						break;
					}
					if (obcm == null)
						break;
					if (obcm instanceof ChatMsg) {
						cm = (ChatMsg) obcm;
						msg = String.format("[%s] %s", cm.getId(), cm.getData());
					} else
						continue;
					switch (cm.getCode()) {
					case "100": // 로그인 시
						break;
					case "500": // 채팅 버튼 >> 채팅 리스트. 받은 정보로 화면 전환
						//AppendText("test Client");
						//ChatListAction CLAction = new ChatListAction();
						//btnChatListButton.addActionListener(CLAction);
						//search >> chatroomid1.~~로 
						//myChatRoom = cm.chatroomId;
						//JavaObjClientChatListView view = new JavaObjClientChatListView(UserName, IpAddr, PortNo, myChatRoom);
						//setVisible(false);
						
						break;
					}
				} catch (IOException e) {
					AppendText("ois.readObject() error");
					try {
						ois.close();
						oos.close();
						socket.close();

						break;
					} catch (Exception ee) {
						break;
					} // catch문 끝
				} // 바깥 catch문끝

			}
		}
	}

	class ImageSendAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// 액션 이벤트가 sendBtn일때 또는 textField 에세 Enter key 치면
			if (e.getSource() == imgBtn) {
				frame = new Frame("이미지첨부");
				fd = new FileDialog(frame, "이미지 선택", FileDialog.LOAD);
				// frame.setVisible(true);
				// fd.setDirectory(".\\");
				fd.setVisible(true);
				//System.out.println(fd.getDirectory() + fd.getFile());
				ChatMsg obcm = new ChatMsg(UserName, "300", "IMG");
				ImageIcon img = new ImageIcon(fd.getDirectory() + fd.getFile());
				obcm.setImg(img);
				SendObject(obcm);
			}
		}
	}

	ImageIcon icon1 = new ImageIcon("src/icon1.jpg");

	public void AppendIcon(ImageIcon icon) {
		int len = textArea.getDocument().getLength();
		// 끝으로 이동
		textArea.setCaretPosition(len);
		textArea.insertIcon(icon);
	}

	// 화면에 출력
	public void AppendText(String msg) {
		// textArea.append(msg + "\n");
		//AppendIcon(icon1);
		msg = msg.trim(); // 앞뒤 blank와 \n을 제거한다.
		int len = textArea.getDocument().getLength();
		// 끝으로 이동
		textArea.setCaretPosition(len);
		textArea.replaceSelection(msg + "\n");
	}

	public void AppendImage(ImageIcon ori_icon) {
		int len = textArea.getDocument().getLength();
		textArea.setCaretPosition(len); // place caret at the end (with no selection)
		Image ori_img = ori_icon.getImage();
		int width, height;
		double ratio;
		width = ori_icon.getIconWidth();
		height = ori_icon.getIconHeight();
		// Image가 너무 크면 최대 가로 또는 세로 200 기준으로 축소시킨다.
		if (width > 200 || height > 200) {
			if (width > height) { // 가로 사진
				ratio = (double) height / width;
				width = 200;
				height = (int) (width * ratio);
			} else { // 세로 사진
				ratio = (double) width / height;
				height = 200;
				width = (int) (height * ratio);
			}
			Image new_img = ori_img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			ImageIcon new_icon = new ImageIcon(new_img);
			textArea.insertIcon(new_icon);
		} else
			textArea.insertIcon(ori_icon);
		len = textArea.getDocument().getLength();
		textArea.setCaretPosition(len);
		textArea.replaceSelection("\n");
		// ImageViewAction viewaction = new ImageViewAction();
		// new_icon.addActionListener(viewaction); // 내부클래스로 액션 리스너를 상속받은 클래스로
	}

	// Windows 처럼 message 제외한 나머지 부분은 NULL 로 만들기 위한 함수
	public byte[] MakePacket(String msg) {
		byte[] packet = new byte[BUF_LEN];
		byte[] bb = null;
		int i;
		for (i = 0; i < BUF_LEN; i++)
			packet[i] = 0;
		try {
			bb = msg.getBytes("euc-kr");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
		for (i = 0; i < bb.length; i++)
			packet[i] = bb[i];
		return packet;
	}

	// Server에게 network으로 전송
	public void SendMessage(String msg) {
		try {
			ChatMsg obcm = new ChatMsg(UserName, "200", msg);
			oos.writeObject(obcm);
		} catch (IOException e) {
			// AppendText("dos.write() error");
			AppendText("oos.writeObject() error");
			try {
				ois.close();
				oos.close();
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.exit(0);
			}
		}
	}

	public void SendObject(Object ob) { // 서버로 메세지를 보내는 메소드
		try {
			oos.writeObject(ob);
		} catch (IOException e) {
			// textArea.append("메세지 송신 에러!!\n");
			AppendText("SendObject Error");
		}
	}
	
	class ChatListAction implements ActionListener // 내부클래스로 액션 이벤트 처리 클래스. 채팅 목록
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			ChatMsg msg = new ChatMsg(UserName, "500", UserName);
			SendObject(msg);
			JavaObjClientChatListView view = new JavaObjClientChatListView(UserName, Mainview);
			setVisible(false);
		}
	}
	
}
