import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.rmi.RemoteException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientGUI {
	private JFrame frame;
	private JList<String> chatList;
	private DefaultListModel<String> chatListModel;
	private JTextField messageField;
	private JButton sendButton;
	private JList<String> userList;
	private DefaultListModel<String> userListModel;
	private JButton loginButton, registerButton, logoutButton;
	private JLabel statusLabel;
	private JLabel typingStatusLabel;
	private Client client;
	private JPopupMenu userListPopup;
	private JScrollPane chatScrollPane;

	public ClientGUI() {
		setupLookAndFeel();
		initialize();
		setupEventHandlers();
		setupUserListContextMenu();
	}

	private void setupLookAndFeel() {
		try {
			UIManager.setLookAndFeel(new FlatLightLaf());
			UIManager.put("Button.arc", 10);
			UIManager.put("Component.arc", 10);
			UIManager.put("TextComponent.arc", 10);
			UIManager.put("ProgressBar.arc", 10);
			UIManager.put("ScrollPane.arc", 10);
		} catch (Exception ex) {
			System.err.println("Failed to initialize FlatLaf");
		}
	}

	private void initialize() {
		frame = new JFrame("Chat Group RMI");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(900, 650);
		frame.setLayout(new BorderLayout());

		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

		statusLabel = new JLabel("Chưa kết nối");
		statusLabel.setForeground(Color.RED);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		loginButton = createStyledButton("Đăng nhập", new Color(76, 175, 80), "login.svg");
		registerButton = createStyledButton("Đăng ký", new Color(33, 150, 243), "register.svg");
		logoutButton = createStyledButton("Đăng xuất", new Color(244, 67, 54), "logout.svg");
		logoutButton.setEnabled(false);

		buttonPanel.add(loginButton);
		buttonPanel.add(registerButton);
		buttonPanel.add(logoutButton);

		topPanel.add(statusLabel, BorderLayout.WEST);
		topPanel.add(buttonPanel, BorderLayout.EAST);

		chatListModel = new DefaultListModel<>();
		chatList = new JList<>(chatListModel);
		chatList.setCellRenderer(new MessageCellRenderer());
		chatScrollPane = new JScrollPane(chatList);
		chatScrollPane.setBorder(BorderFactory.createTitledBorder("Tin nhắn nhóm"));

		userListModel = new DefaultListModel<>();
		userList = new JList<>(userListModel);
		userList.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		JScrollPane userScrollPane = new JScrollPane(userList);
		userScrollPane.setBorder(BorderFactory.createTitledBorder("Người dùng online"));
		userScrollPane.setPreferredSize(new Dimension(180, 0));

		JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		messageField = new JTextField();
		messageField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		sendButton = createStyledButton("Gửi", new Color(0, 150, 136), "send.svg");
		sendButton.setEnabled(false);

		typingStatusLabel = new JLabel("");
		typingStatusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
		typingStatusLabel.setForeground(Color.GRAY);

		JPanel messagePanel = new JPanel(new BorderLayout());
		messagePanel.add(messageField, BorderLayout.CENTER);
		messagePanel.add(sendButton, BorderLayout.EAST);

		JPanel fullBottomPanel = new JPanel(new BorderLayout());
		fullBottomPanel.add(typingStatusLabel, BorderLayout.NORTH);
		fullBottomPanel.add(messagePanel, BorderLayout.CENTER);
		fullBottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, chatScrollPane, userScrollPane);
		splitPane.setDividerLocation(650);
		splitPane.setResizeWeight(1.0);

		frame.add(topPanel, BorderLayout.NORTH);
		frame.add(splitPane, BorderLayout.CENTER);
		frame.add(fullBottomPanel, BorderLayout.SOUTH);

		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		try {
			client = new Client(this);
		} catch (RemoteException e) {
			showError("Lỗi kết nối RMI: " + e.getMessage());
		}
		updateConnectionStatus();
	}

	private JButton createStyledButton(String text, Color color, String iconName) {
		JButton button = new JButton(text);
		try {
			FlatSVGIcon icon = new FlatSVGIcon("resources/icons/" + iconName, 20, 20);
			button.setIcon(icon);
		} catch (Exception e) {
			System.err.println("Cannot load icon: " + iconName);
		}
		button.setBackground(color);
		button.setForeground(Color.WHITE);
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
		button.setFont(new Font("Segoe UI", Font.BOLD, 12));
		return button;
	}

	private void setupUserListContextMenu() {
		userListPopup = new JPopupMenu();
		JMenuItem privateMessageItem = new JMenuItem("Nhắn tin riêng");
		JMenuItem sendFileItem = new JMenuItem("Gửi tệp tin");

		privateMessageItem.addActionListener(e -> {
			String recipient = userList.getSelectedValue();
			if (recipient != null && !recipient.equals(client.getUsername())) {
				String message = JOptionPane.showInputDialog(frame, "Nhập tin nhắn riêng cho " + recipient + ":");
				if (message != null && !message.trim().isEmpty()) {
					try {
						client.sendPrivateMessage(recipient, message.trim());
					} catch (Exception ex) {
						showError("Lỗi gửi tin nhắn riêng: " + ex.getMessage());
					}
				}
			} else {
				showInfo("Vui lòng chọn một người dùng khác để nhắn tin.");
			}
		});

		sendFileItem.addActionListener(e -> {
			String recipient = userList.getSelectedValue();
			if (recipient != null && !recipient.equals(client.getUsername())) {
				JFileChooser fileChooser = new JFileChooser();
				int option = fileChooser.showOpenDialog(frame);
				if (option == JFileChooser.APPROVE_OPTION) {
					try {
						client.sendFile(recipient, fileChooser.getSelectedFile().getAbsolutePath());
					} catch (Exception ex) {
						showError("Lỗi gửi tệp tin: " + ex.getMessage());
					}
				}
			} else {
				showInfo("Vui lòng chọn một người dùng khác để gửi tệp tin.");
			}
		});

		userListPopup.add(privateMessageItem);
		userListPopup.add(sendFileItem);

		userList.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					int index = userList.locationToIndex(e.getPoint());
					if (index != -1) {
						userList.setSelectedIndex(index);
					}
				}
			}

			public void mouseReleased(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e) && userList.getSelectedIndex() != -1) {
					userListPopup.show(userList, e.getX(), e.getY());
				}
			}
		});
	}

	private void setupEventHandlers() {
		loginButton.addActionListener(this::handleLogin);
		registerButton.addActionListener(this::handleRegister);
		logoutButton.addActionListener(this::handleLogout);
		sendButton.addActionListener(this::handleSendMessage);
		messageField.addActionListener(this::handleSendMessage);

		messageField.getDocument().addDocumentListener(new DocumentListener() {
			private javax.swing.Timer typingTimer = new javax.swing.Timer(1500, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					notifyTyping(false);
				}
			});

			{
				typingTimer.setRepeats(false);
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				notifyTyping(true);
				typingTimer.restart();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				if (messageField.getText().isEmpty()) {
					notifyTyping(false);
					typingTimer.stop();
				} else {
					typingTimer.restart();
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
			}

			private void notifyTyping(boolean isTyping) {
				try {
					if (client.isConnected() && client.getUsername() != null) {
						client.getChatService().typingNotification(client.getUsername(), isTyping);
					}
				} catch (RemoteException ex) {
					System.err.println("Lỗi khi gửi thông báo gõ: " + ex.getMessage());
				}
			}
		});
	}

	private void handleLogin(ActionEvent e) {
		JTextField usernameField = new JTextField();
		JPasswordField passwordField = new JPasswordField();

		Object[] message = { "Tên đăng nhập:", usernameField, "Mật khẩu:", passwordField };

		int option = JOptionPane.showConfirmDialog(frame, message, "Đăng nhập", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);

		if (option == JOptionPane.OK_OPTION) {
			String username = usernameField.getText().trim();
			String password = new String(passwordField.getPassword());

			if (username.isEmpty() || password.isEmpty()) {
				showError("Vui lòng nhập đầy đủ thông tin!");
				return;
			}

			try {
				if (client.login(username, password)) {
					updateUIAfterLogin(username);
					loadChatHistory();
					showSuccess("Đăng nhập thành công!");
				} else {
					showError("Sai tên đăng nhập hoặc mật khẩu!");
				}
			} catch (Exception ex) {
				showError("Lỗi kết nối đến server: " + ex.getMessage());
			}
		}
	}

	private void handleRegister(ActionEvent e) {
		JTextField usernameField = new JTextField();
		JPasswordField passwordField = new JPasswordField();
		JPasswordField confirmPasswordField = new JPasswordField();

		Object[] message = { "Tên đăng nhập:", usernameField, "Mật khẩu:", passwordField, "Xác nhận mật khẩu:",
				confirmPasswordField };

		int option = JOptionPane.showConfirmDialog(frame, message, "Đăng ký", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);

		if (option == JOptionPane.OK_OPTION) {
			String username = usernameField.getText().trim();
			String password = new String(passwordField.getPassword());
			String confirmPassword = new String(confirmPasswordField.getPassword());

			if (username.isEmpty() || password.isEmpty()) {
				showError("Vui lòng nhập đầy đủ thông tin!");
				return;
			}

			if (!password.equals(confirmPassword)) {
				showError("Mật khẩu xác nhận không khớp!");
				return;
			}

			try {
				if (client.register(username, password)) {
					showSuccess("Đăng ký thành công! Bây giờ bạn có thể đăng nhập.");
				} else {
					showError("Tên đăng nhập đã tồn tại!");
				}
			} catch (Exception ex) {
				showError("Lỗi kết nối đến server: " + ex.getMessage());
			}
		}
	}

	private void handleLogout(ActionEvent e) {
		try {
			client.logout();
		} catch (Exception ex) {
			System.err.println("Logout error: " + ex.getMessage());
		}

		updateUIAfterLogout();
		showInfo("Đã đăng xuất thành công!");
	}

	private void handleSendMessage(ActionEvent e) {
		String messageContent = messageField.getText().trim();
		if (!messageContent.isEmpty() && client.isConnected()) {
			try {
				// Định dạng tin nhắn giống như cách server làm để hiển thị nhất quán
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", java.util.Locale.ENGLISH);
				String timestamp = sdf.format(new Date());
				String formattedMessage = "[" + timestamp + "] " + client.getUsername() + ": " + messageContent;

				// 1. Hiển thị tin nhắn trên GUI của người gửi ngay lập tức
				appendMessage(formattedMessage);

				// 2. Gửi tin nhắn đến server
				client.sendMessage(messageContent);

				// 3. Xóa trường nhập tin nhắn
				messageField.setText("");

			} catch (Exception ex) {
				showError("Lỗi gửi tin nhắn: " + ex.getMessage());
			}
		}
	}

	private void updateUIAfterLogin(String username) {
		SwingUtilities.invokeLater(() -> {
			loginButton.setEnabled(false);
			registerButton.setEnabled(false);
			logoutButton.setEnabled(true);
			sendButton.setEnabled(true);
			statusLabel.setText("Đã đăng nhập: " + username);
			statusLabel.setForeground(new Color(0, 128, 0));
			updateConnectionStatus();
		});
	}

	private void updateUIAfterLogout() {
		SwingUtilities.invokeLater(() -> {
			loginButton.setEnabled(true);
			registerButton.setEnabled(true);
			logoutButton.setEnabled(false);
			sendButton.setEnabled(false);
			statusLabel.setText("Chưa đăng nhập");
			statusLabel.setForeground(Color.RED);
			userListModel.clear();
			chatListModel.clear();
			updateConnectionStatus();
		});
	}

	private void updateConnectionStatus() {
		SwingUtilities.invokeLater(() -> {
			if (client.isConnected()) {
				statusLabel.setToolTipText("Đã kết nối đến server");
			} else {
				statusLabel.setToolTipText("Mất kết nối đến server");
			}
		});
	}

	public void appendMessage(String message) {
	    SwingUtilities.invokeLater(() -> {

	        JScrollBar verticalScrollBar = chatScrollPane.getVerticalScrollBar();
	        boolean isAtBottom = (verticalScrollBar.getValue() + verticalScrollBar.getVisibleAmount()) >= verticalScrollBar.getMaximum();

	        chatListModel.addElement(message);
	        if (isAtBottom) {
	            chatList.ensureIndexIsVisible(chatListModel.size() - 1);
	        }
	    });
	}

	public void updateOnlineUsersList(List<String> onlineUsers) {
		SwingUtilities.invokeLater(() -> {
			userListModel.clear();
			for (String user : onlineUsers) {
				userListModel.addElement(user);
			}
		});
	}

	public void receivePrivateMessage(String sender, String message) throws RemoteException {
		SwingUtilities.invokeLater(() -> {
			appendMessage("[Tin nhắn riêng từ " + sender + "] " + message);
		});
	}

	public void receiveFileNotification(String sender, String fileName) throws RemoteException {
		String serverFileName = sender + "_" + fileName;

		SwingUtilities.invokeLater(() -> {
			int option = JOptionPane.showConfirmDialog(frame,
					"Bạn đã nhận được tệp tin '" + fileName + "' từ " + sender + ".\nBạn có muốn tải về không?",
					"Nhận tệp tin", JOptionPane.YES_NO_OPTION);

			if (option == JOptionPane.YES_OPTION) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Chọn nơi lưu tệp tin");
				fileChooser.setSelectedFile(new File(fileName));
				int userSelection = fileChooser.showSaveDialog(frame);

				if (userSelection == JFileChooser.APPROVE_OPTION) {
					File fileToSave = fileChooser.getSelectedFile();
					try {
						byte[] fileData = client.downloadFile(serverFileName);
						if (fileData != null) {
							try (FileOutputStream fos = new FileOutputStream(fileToSave)) {
								fos.write(fileData);
								showSuccess("Tải tệp tin thành công!");
								System.out.println("File downloaded successfully to: " + fileToSave.getAbsolutePath());
							}
						} else {
							showError("Lỗi tải tệp: Tệp không tồn tại trên server.");
						}
					} catch (Exception ex) {
						showError("Lỗi tải tệp: " + ex.getMessage());
						ex.printStackTrace();
					}
				}
			}
		});
	}

	public void receiveTypingNotification(String username, boolean isTyping) throws RemoteException {
		SwingUtilities.invokeLater(() -> {
			if (isTyping) {
				typingStatusLabel.setText(username + " đang gõ...");
			} else {
				typingStatusLabel.setText("");
			}
		});
	}

	private void loadChatHistory() {
		try {
			List<String> history = client.getChatHistory();
			for (String message : history) {
				appendMessage(message);
			}
		} catch (Exception e) {
			System.err.println("Error loading chat history: " + e.getMessage());
		}
	}

	private void showError(String message) {
		SwingUtilities.invokeLater(() -> {
			JOptionPane.showMessageDialog(frame, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
		});
	}

	private void showSuccess(String message) {
		SwingUtilities.invokeLater(() -> {
			JOptionPane.showMessageDialog(frame, message, "Thành công", JOptionPane.INFORMATION_MESSAGE);
		});
	}

	private void showInfo(String message) {
		SwingUtilities.invokeLater(() -> {
			JOptionPane.showMessageDialog(frame, message, "Thông tin", JOptionPane.INFORMATION_MESSAGE);
		});
	}

	private class MessageCellRenderer extends JPanel implements ListCellRenderer<String> {
		private final JTextArea contentArea = new JTextArea();
		private final JLabel senderTimeLabel = new JLabel();

		private final JPanel contentPanel;

		public MessageCellRenderer() {
			setOpaque(true);
			setLayout(new BorderLayout(0, 2));
			setBorder(new EmptyBorder(5, 10, 5, 10));

			senderTimeLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
			senderTimeLabel.setForeground(Color.GRAY);

			contentArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
			contentArea.setLineWrap(true);
			contentArea.setWrapStyleWord(true);
			contentArea.setEditable(false);
			contentArea.setOpaque(false);
			contentArea.setFocusable(false);

			// Tạo panel mới để chứa contentArea
			contentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)); // Mặc định lề trái
			contentPanel.setOpaque(false); // Trong suốt để lấy màu nền của cha
			contentPanel.add(contentArea);

			add(senderTimeLabel, BorderLayout.NORTH);
			add(contentPanel, BorderLayout.CENTER); // Thêm contentPanel thay vì contentArea
		}

		@Override
		public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
		    String[] parts = value.split(": ", 2);
		    String header = parts[0];
		    String content = parts.length > 1 ? parts[1] : "";

		    String time = "";
		    String sender = "";
		    Pattern pattern = Pattern.compile("\\[(.*?)\\] (.*)");
		    Matcher matcher = pattern.matcher(header);
		    if (matcher.find()) {
		        time = matcher.group(1);
		        sender = matcher.group(2);
		    } else {
		        sender = header;
		    }

		    // Gán nội dung cho JTextArea
		    contentArea.setText(content);

		    // Tính toán độ rộng tối đa cho khung chat (40% chiều rộng của JList)
		    int listWidth = list.getWidth();
		    if (listWidth > 0) {
		        int maxWidth = (int) (listWidth * 0.4); // 40% độ rộng

		        // === DÒNG CODE ĐÃ SỬA LỖI ===
		        FontMetrics fm = contentArea.getFontMetrics(contentArea.getFont());
		        // Ước tính độ rộng trung bình bằng cách đo chuỗi alphabet
		        int charWidth = fm.stringWidth("abcdefghijklmnopqrstuvwxyz") / 26;
		        
		        int columns = (charWidth > 0) ? maxWidth / charWidth : 30; // Tránh chia cho 0
		        
		        contentArea.setColumns(columns); // Thiết lập độ rộng mong muốn
		    } else {
		        contentArea.setColumns(30); // Giá trị mặc định khi list chưa có kích thước
		    }

		 // Thiết lập màu nền và căn lề dựa trên người gửi
		    if (value.startsWith("[Tin nhắn riêng từ ")) {
		        this.setBackground(new Color(255, 239, 213));
		        senderTimeLabel.setHorizontalAlignment(SwingConstants.LEFT);
		        senderTimeLabel.setText(sender + " [" + time + "]");
		        ((FlowLayout) contentPanel.getLayout()).setAlignment(FlowLayout.LEFT);
		        // Căn lề chữ sang TRÁI
		        contentArea.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

		    } else if (client != null && client.getUsername() != null && sender.equals(client.getUsername())) {
		        this.setBackground(new Color(220, 248, 198));
		        senderTimeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		        senderTimeLabel.setText("Bạn [" + time + "]");
		        ((FlowLayout) contentPanel.getLayout()).setAlignment(FlowLayout.RIGHT);
		        // Căn lề chữ sang PHẢI
		        contentArea.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

		    } else {
		        this.setBackground(Color.WHITE);
		        senderTimeLabel.setHorizontalAlignment(SwingConstants.LEFT);
		        senderTimeLabel.setText(sender + " [" + time + "]");
		        ((FlowLayout) contentPanel.getLayout()).setAlignment(FlowLayout.LEFT);
		        // Căn lề chữ sang TRÁI
		        contentArea.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		    }

		    contentArea.setForeground(Color.BLACK);

		    return this;
		}
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new ClientGUI();
		});
	}
}