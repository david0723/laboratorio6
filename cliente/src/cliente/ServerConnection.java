package cliente;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
	private OutputStream outputStream;
	private FileInputStream fileInput;

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
				outputStream = soc.getOutputStream();
				outToServer = new DataOutputStream(outputStream);
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
		boolean response = false;
		if (sendMessage("credenciales:"+s).contains("credenciales:ok"))
		{
			System.out.println("credenciales:ok");
			response = true;
		}
		return response;
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
	public String[] getPlaylist(String user) 
	{
		return (sendMessage("playlist:"+user)).split(":");
		
	}
	public void sendVideo(File f, String user)
	{
		
		sendMessage("video:"+f.getName()+":"+user+":"+(int)f.length());
		
		byte [] mybytearray  = new byte [(int)f.length()];
		
		
        BufferedInputStream bis;
		try 
		{
			fileInput = new FileInputStream(f);
			bis = new BufferedInputStream(fileInput);
			bis.read(mybytearray,0,mybytearray.length);
			
//			int count;
//	        while ((count = fileInput.read(mybytearray)) > 0) 
//	        {
//	            outputStream.write(mybytearray, 0, count);
//	        }
			outToServer.write(mybytearray);
	        
		}
		catch (FileNotFoundException e)
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
