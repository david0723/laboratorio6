import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
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
	
	
	private PrintWriter out;
	
	private Socket socket = null;
	
	
	
	private boolean libre;
	
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
	


	private String read() 
	{
		
        String clientSentence="";
		try 
		{
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
	        DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
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
			userpsw = new Scanner(new File(DATA+AUTH)).useDelimiter("\\Z").next();
			System.out.println("Saved userpswd:" + userpsw);
			System.out.println("client userpswd"+s[1]);
		} 
    	catch (FileNotFoundException e) 
    	{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return  (userpsw.contains(s[1]));
	}

}
