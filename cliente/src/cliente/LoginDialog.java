package cliente;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

class LoginDialog extends JDialog implements ActionListener, Runnable
{
	private String LOGIN = "LOGIN";
	private String CANCEL = "CANCEL";

    private final JLabel jlblUsername = new JLabel("Username");
    private final JLabel jlblPassword = new JLabel("Password");

    private final JTextField jtfUsername = new JTextField(15);
    private final JPasswordField jpfPassword = new JPasswordField();

    private final JButton jbtOk = new JButton("Login");
    private final JButton jbtCancel = new JButton("Cancel");

    private final JLabel jlblStatus = new JLabel(" ");
    private Main main;

    public LoginDialog() 
    {
        this(null, true, null);
    }

    public LoginDialog(final JFrame parent, boolean modal, Main m) 
    {
    	main = m;
    	

        JPanel p3 = new JPanel(new GridLayout(2, 1));
        p3.add(jlblUsername);
        p3.add(jlblPassword);

        JPanel p4 = new JPanel(new GridLayout(2, 1));
        p4.add(jtfUsername);
        p4.add(jpfPassword);

        JPanel p1 = new JPanel();
        p1.add(p3);
        p1.add(p4);

        JPanel p2 = new JPanel();
        
        jbtOk.setActionCommand(LOGIN);
        jbtOk.addActionListener(this);
        
        jbtCancel.setActionCommand(CANCEL);
        jbtCancel.addActionListener(this);
        
        p2.add(jbtOk);
        p2.add(jbtCancel);

        JPanel p5 = new JPanel(new BorderLayout());
        p5.add(p2, BorderLayout.CENTER);
        p5.add(jlblStatus, BorderLayout.NORTH);
        jlblStatus.setForeground(Color.RED);
        jlblStatus.setHorizontalAlignment(SwingConstants.CENTER);

        setLayout(new BorderLayout());
        add(p1, BorderLayout.CENTER);
        add(p5, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);


        
    }

	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getActionCommand().equals(LOGIN))
		{
//			(new Thread(this)).start();
			main.logIn(jtfUsername.getText()+","+jpfPassword.getText());
			setVisible(false);
		}
		else if (e.getActionCommand().equals(CANCEL))
		{
			setVisible(false);
		}
		// TODO Auto-generated method stub
		
	}

	public void run() 
	{
		main.logIn(jlblUsername.getText()+","+jlblPassword);
	}
}