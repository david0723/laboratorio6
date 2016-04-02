package cliente;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerConnection implements Runnable
{
	private int port;
	private String ip;
	private Socket soc;
	private DataOutputStream outToServer;
	private BufferedReader in;

	public ServerConnection(int xport, String xip)
	{
		port = xport;
		ip=xip;
//		try 
//		{
//			soc = new Socket(ip, port);
//		} 
//		catch (UnknownHostException e)
//		{
//			e.printStackTrace();
//		} 
//		catch (IOException e) 
//		{
//			e.printStackTrace();
//		}
	}
	public String sendMessage(String message)
	{
		String resp = ";";
		try
		{
			if (soc == null)
			{
				System.out.println("soc is null");
				soc = new Socket(ip, port);
				outToServer = new DataOutputStream(soc.getOutputStream());
				in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
			}
			
	        outToServer.writeBytes(message + '\n');
	        System.out.println("message sended to server: "+message);
	        System.out.println("waiting for response...");
            resp = in.readLine();
            System.out.println("response: "+resp);
//	        soc.close();
		} 
		catch (UnknownHostException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return resp;
        
	}
	public String receiveString() 
	{
		
		String response ="";
		try 
		{
//			ServerSocket serversocket = new ServerSocket(port);
//			soc = serversocket.accept();
			
            BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            response = in.readLine();
//            serversocket.close();
			
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		System.out.println("Server said: "+response);
		return response;
	}
	public boolean checkLogin(String s)
	{
		if (sendMessage("credenciales:"+s).contains("credenciales:ok"))
		{
			System.out.println("credenciales:ok");
			return true;
		}
		return false;
	}
	
	public void updatePlaylist() 
	{
		sendMessage("getplaylist");
		receiveString();
		
	}
	public void accept(ServerSocket s)
	{
		try 
		{
			soc = s.accept();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	public void run() 
	{
		
	}
}
