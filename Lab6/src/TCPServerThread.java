import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Thread que maneja una conexion TCP
 * @author marianaviro
 *
 */
public class TCPServerThread extends Thread
{
	private final static String DATA = "./data/";
	private final static String AUTH = "auth.txt";
	private static final int FILE_SIZE = 6022386;
	private static final String USERS = "./scripts/users/";
	
	
	private PrintWriter out;
	
	private Socket socket = null;
	
	
	
	private boolean libre;
	private BufferedReader inFromClient;
	private DataOutputStream outToClient;
	private InputStreamReader inputStreamReader;
	private InputStream inputStream;
	
	public TCPServerThread()
	{
		setLibre(true);		
	}
		
	public boolean isLibre()
	{
		return libre;
	}

	private void setLibre(boolean libre) 
	{
		this.libre = libre;
	}

	public void procesar( Socket s )
	{
		System.out.println("procesando...");
		// Asignar el socket que le fue enviado
		socket = s;
		setLibre(false);
	}

	public void run() 
	{
		System.out.println("running...");
		// Recibir informacion por el Input Stream del Socket
//		try
//		{
//			BufferedReader in = new BufferedReader( new InputStreamReader( socket.getInputStream()));
//			System.out.println("try");
//			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
//			String message="";
//			while ((message = in.readLine()) != null)
//			{
//				System.out.println("println1");
//				System.out.println("message: "+message);
//				
//	        	if (message=="credenciales")
//	        	{
//	        		out.flush();
//	        		out.writeBytes("waiting" + '\n');
//	        		System.out.println("println2");
//	        		if (handleAuthentication(in.readLine()))
//	        		{
//	        			out.writeBytes("credenciales:ok"+'\n');
//	        		}
//	        	}  
//			}
//            socket.close();
//            out.close();
//            setLibre(true);
//			 
//        }
//		
//		catch (IOException e) 
//		{
//			System.err.println("Problemas en el puerto TCP: " + e.getMessage());
//        }
		
		String message = read();
        if (message.contains("credenciales"))
        {

        	if (handleAuthentication(message))
        	{
        		System.out.println("Authentication OK");
        		write("credenciales:ok");
        	}
        	else
        	{
        		System.out.println("Authentication Pailas");
        	}
        }
        else if(message.contains("playlist"))
        {
        	write(getPlaylist((message.split(":"))[1]));
        }
        else if(message.contains("video"))
        {
        	write("ok,thanks");
        	downloadVideo(message.split(":"));
        }

		
//        try 
//        {
//        	String clientSentence;
//            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
//			clientSentence = inFromClient.readLine();
//			System.out.println("Received: " + clientSentence);
//	        outToClient.writeBytes("funciona"+'\n');
//		}
//        catch (IOException e) 
//        {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        
		
	}
	


	private void downloadVideo(String[] split) 
	{
		int bytesRead;
	    int current = 0;
	    
	    int fileSize = Integer.parseInt(split[4]);
		
		byte [] mybytearray  = new byte [fileSize];
		try 
		{
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(USERS+split[2]+"/"+split[1]));
			System.out.println("Initiating download...");
			
			bytesRead = inputStream.read(mybytearray,0,mybytearray.length);
			
			

			current = bytesRead;

		      do 
		      {
		    	  System.out.println("Bytes Read: "+bytesRead);
		    	
		          bytesRead = inputStream.read(mybytearray, current, (mybytearray.length-current));
		          if(bytesRead >= 0) current += bytesRead;
		      } 
		      while(bytesRead > 0);
		      System.out.println("DONE");
		      
		      bos.write(mybytearray);
		      bos.flush();
		      bos.close();
		} 
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private String getPlaylist(String string) 
	{
		String resp = "";
		File folder = new File("./scripts/users/"+string+"/");
		File[] listOfFiles = folder.listFiles();

	    for (int i = 0; i < listOfFiles.length; i++) 
	    {
			if (listOfFiles[i].isFile() ) 
			{
				if(listOfFiles[i].getName().contains("mp4"))
				{
					resp = resp +listOfFiles[i].getName()+":";
				}
			} 
	    }
	    return resp;
		    
		
	}

	private String read() 
	{
		
        String clientSentence="";
		try 
		{
			if (inFromClient == null)
			{
				inputStream = socket.getInputStream();
				inputStreamReader =  new InputStreamReader(inputStream);
				inFromClient = new BufferedReader(inputStreamReader);
			}
			clientSentence = inFromClient.readLine();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
        System.out.println("Received: " + clientSentence);
        return clientSentence;
	}
	private void write(String s) 
	{
		try 
		{
	        if (outToClient == null)
	        {
	        	outToClient = new DataOutputStream(socket.getOutputStream());
	        }
			outToClient.writeBytes(s+'\n');
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}

	}

	public BufferedReader getIn()
	{
		BufferedReader b = null;
		try
		{
			b = new BufferedReader( new InputStreamReader( socket.getInputStream()));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return b;
	}

	private boolean handleAuthentication(String info) 
	{
		System.out.println("Auth: "+info);
//    	// Procesar las credenciales
//    	String[] credenciales = info.split(",");
//    	
//    	//Guardar con la clase auxiliar
//    	try 
//    	{
//			SecureStorage.storeUser(credenciales[0], credenciales[1]);
//		} 
//    	catch (Exception e) 
//    	{
//			e.printStackTrace();
//		}
//    	
		String[] s = info.split(":");
		String userpsw="";
    	try 
    	{
			userpsw = new Scanner(new File("./scripts/users/"+s[1]+"/auth.txt")).useDelimiter("\\Z").next();
			System.out.println("Saved userpswd:" + userpsw);
			System.out.println("client userpswd"+s[1]+s[2]);
		} 
    	catch (FileNotFoundException e) 
    	{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return  (userpsw.contains(s[2]));
	}

}
