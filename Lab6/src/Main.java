import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Clase que recibe las conexiones, identifica si es una conexion TCP o UDP y la dirige al
 * socket correspondiente
 * @author marianaviro
 *
 */
public class Main 
{


	private final static int puertoTCP = 9090;
	
	private final static int N = 10;	
	
	public static void main(String[] args) 
	{

		//Crear el socket TCP con el puerto
        try ( ServerSocket TCPServerSocket = new ServerSocket(puertoTCP) )
        { 
        	
        	boolean listening = true;
            ArrayList<TCPServerThread> threads = new ArrayList<TCPServerThread>();
            
            // Crear un pool fijo de N threads
            for (int i = 0; i < N; i++)
            {
            	TCPServerThread actual = new TCPServerThread();
            	threads.add(actual);
            }
        	
            Socket s = null;
            int contador = 0;
            
            //Escucha permanentemente por el socket
            while ( listening ) 
            {
            	System.out.println("SERVER LISTENING");
            	
            	s = TCPServerSocket.accept();
            	System.out.println("Connection Accepted");
            	            	
            	//Si recibe una solicitud de conexion, busca un thread libre y le envia el socket para que lo procese
            	while( s != null )
            	{
            		
            		TCPServerThread thread = threads.get(contador);
            		if( thread.isLibre() )
            		{
            			thread.procesar(s);
            			thread.start();
            			s = null;
            			contador = 0;
            		}
            		else
            		{
            			//Si no hay un thread libre, empieza la busqueda de nuevo
            			if( contador == threads.size() )
            				contador = 0;
            			else
            				contador++;
            		}
            	}
	        }            
	    }
        catch (IOException eT) 
        {
            System.err.println("Problemas escuchando en el puerto TCP " + puertoTCP + ": " + eT.getMessage());
            System.exit(-1);
        }
	}
}
