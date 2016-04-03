package cliente;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JList;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class Main {

	private int TCP_PORT = 9090;
	private String IP_ADDRESS = "localhost";
	private boolean loggedIn;
	private String user;
	
	private Client client;
	private Main self;
	private JFrame frame;
	private JPanel panel;
	private JTextField textField;
	private int channel;
	private ServerConnection connection;
	private String group;
	private JList list;
	private JPanel panel_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() 
			{
				try 
				{
					Main window = new Main();
					window.frame.setVisible(true);
				} 
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() 
	{
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		loggedIn = false;
		
		self = this;
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		panel = new JPanel();
		panel.setBounds(24, 93, 216, 139);
		frame.getContentPane().add(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		JButton btnListen = new JButton("Listen");
		btnListen.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				updateIpPort();
				
				client.setPort(channel);
				client.setGroup(group);
				
				System.out.println("switch");
				client.change();
				System.out.println(client.getOn());
				
				
			}
		});
		btnListen.setBounds(118, 20, 122, 29);
		frame.getContentPane().add(btnListen);
		JLabel lblChannel = new JLabel("Channel");
		lblChannel.setBounds(24, 65, 61, 16);
		frame.getContentPane().add(lblChannel);
		textField = new JTextField();
		textField.setText("6:12350");
		textField.setBounds(106, 61, 134, 28);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 444, 22);
		frame.getContentPane().add(menuBar);
		
		JMenu mnOption = new JMenu("option");
		menuBar.add(mnOption);
		
		JMenuItem mntmLogIn = new JMenuItem("Log in");
		mntmLogIn.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				System.out.println("login");

				

			}
			@Override
			public void mousePressed(MouseEvent e) 
			{
				System.out.println("login");
				LoginDialog login = new LoginDialog(frame, true, self);
				login.setVisible(true);
			}
		});
		mnOption.add(mntmLogIn);
		
		JMenuItem mntmUploadVideo = new JMenuItem("Upload video");
		mntmUploadVideo.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				int result = fileChooser.showOpenDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) 
				{
				    File selectedFile = fileChooser.getSelectedFile();
				    connection= new ServerConnection(TCP_PORT, IP_ADDRESS);
				    connection.sendVideo(selectedFile, user);
				    System.out.println("Selected file: " + selectedFile.getAbsolutePath());
				}
			}
		});
		mnOption.add(mntmUploadVideo);
		
		JMenuItem mntmChangeChannel = new JMenuItem("Change channel");
		mnOption.add(mntmChangeChannel);
		
		JMenuItem mntmPauseplay = new JMenuItem("pause/play");
		mnOption.add(mntmPauseplay);
		
		panel_1 = new JPanel();
		panel_1.setBounds(274, 65, 155, 168);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		list = new JList();
		panel_1.add(list, BorderLayout.CENTER);
		
		updateIpPort();
		
		client = new Client(this, channel, group);
		(new Thread(client)).start();
	}
	public void updateIpPort()
	{
		String text = textField.getText();
		String ipPort[] = text.split(":");
		channel = Integer.parseInt(ipPort[1]);
		group = ipPort[0];
		System.out.println("CHANNEL -> "+channel);
		System.out.println("GROUP   -> "+group);
	}
	public void refresh(ImageIcon img)
	{
		System.out.println("Refreshing Panel");
		JLabel label = new JLabel("", img, JLabel.CENTER);
		panel.removeAll();
		panel.add( label, BorderLayout.CENTER );
		panel.repaint();
		panel.updateUI();
	}


	public void logIn(String string) 
	{
		connection= new ServerConnection(TCP_PORT, IP_ADDRESS);
		if(loggedIn = connection.checkLogin(string))
		{
			user = (string.split(","))[0];
			System.out.println("MAIN -> login ok");
			
			JOptionPane.showMessageDialog(frame, "Log In Succesful");
//			updatePlaylist();
			

		}
	}
	public void updatePlaylist()
	{
		connection= new ServerConnection(TCP_PORT, IP_ADDRESS);
		String[] vids = connection.getPlaylist(user);
		panel_1.removeAll();
		list = new JList(vids);
		panel_1.add(list, BorderLayout.CENTER);
		panel_1.repaint();
		panel_1.updateUI();
	}
}
