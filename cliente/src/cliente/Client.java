package cliente;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Client implements Runnable
{
	private final static String data = "./data/";
	private boolean on;
	private Main main;
	private int port;
	private String groupIp;
	public Client(Main m, int p, String ip_group)
	{
		on = false;
		main = m;
		setPort(p);
		groupIp = ip_group;
	}
	public boolean getOn()
	{
		return on;
	}
	public void turnOn()
	{
		on = true;
	}
	public void turnOff()
	{
		on = false;
	}
	public void change()
	{
		on = !on;
	}
	
	public void run()
	{ 
		System.out.println("client - run");
		int i =0;
		while (true)
		{
			System.out.println("enter the void");
			if(on)
			{
				byte[] bytes = null;
				DatagramSocket socket;
				try 
				{
					MulticastSocket multicastSocket = new MulticastSocket(port);
					InetAddress group = InetAddress.getByName(groupIp);
					multicastSocket.joinGroup(group);
					
					
//					socket = new DatagramSocket(port );
					System.out.println("Listening on port:"+port);
					
					byte[] buffer = new byte[10000];

			        DatagramPacket paquete = new DatagramPacket(buffer, buffer.length);
			        multicastSocket.receive(paquete);
			        System.out.println("receiving frame:"+i);
			        bytes = paquete.getData();

//			        socket.close();
			        multicastSocket.close();
			        
					BufferedImage img = null;
					try 
					{
						img = ImageIO.read(new ByteArrayInputStream(bytes));
//						File outputfile = new File(data+"image"+i+".jpeg");
//					    ImageIO.write(img, "jpeg", outputfile);
					} 
					catch (IOException e2) 
					{
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					i++;
					ImageIcon image = new ImageIcon(img);
					main.refresh(image);
			        
				} 
				catch (SocketException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				catch (IOException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
		
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}

}
