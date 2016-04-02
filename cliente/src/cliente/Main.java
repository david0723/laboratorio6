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
				int p = Integer.parseInt(textField.getText()) ;
				if(p!= channel)
				{
					client.setPort(channel=p);
				}
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
		textField.setText("12345");
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
				    System.out.println("Selected file: " + selectedFile.getAbsolutePath());
				}
			}
		});
		mnOption.add(mntmUploadVideo);
		
		JMenuItem mntmChangeChannel = new JMenuItem("Change channel");
		mnOption.add(mntmChangeChannel);
		
		JMenuItem mntmPauseplay = new JMenuItem("pause/play");
		mnOption.add(mntmPauseplay);
		
		JList list = new JList();
		list.setBounds(283, 93, 140, 140);
		frame.getContentPane().add(list);
		channel = Integer.parseInt(textField.getText());
		
		client = new Client(this, channel, "224.0.0.23");
		(new Thread(client)).start();
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
			
//			JOptionPane j = new JOptionPane("Logged In");
//			j.createDialog("Succes Bitch");
//			j.setVisible(true);
		}
	}
}
