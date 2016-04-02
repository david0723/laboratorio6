import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Hashtable;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * Clase que sirve para guardar y autenticar usuarios de forma segura
 */
public class SecureStorage {

	private static Hashtable<String, String> hash;

	private static Hashtable<String, SecretKey> keys;
	private static KeyGenerator keyGen;

	/**
	 * Guarda un nuevo usuario con su clave.
	 * @param user - El usuario a guardar
	 * @param pass - La clave del usuario
	 * @throws Exception Si el usuario ya existe o si ocurre un error.
	 */
	public static synchronized void storeUser(String user, String pass) throws Exception{

		initHash();
		initKeyGen();
		initKeys();

		//Generar la clave de encriptacion
		SecretKey key = keyGen.generateKey();

		//Inicializar el cifrado
		Cipher desCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		desCipher.init(Cipher.ENCRYPT_MODE,key);

		//Encriptar la clave
		//Informacion del password literal para encriptar
		byte[] passwordIn = pass.getBytes();
		byte[] cifrado = desCipher.doFinal(passwordIn);
		String passCifrada = Base64.getEncoder().encodeToString(cifrado);

		//Guardar en el hash
		String oldPass = hash.put(user, passCifrada);
		//Verifica si ya existia el usuario
		if (oldPass != null){
			hash.put(user, oldPass);
			throw new Exception("User is already stored");
		}
		
		//Guardar la llave para desencriptar
		keys.put(user, key);
	}
	
	/**
	 * Autentica a un usuario dado el usuario y la clave ingresada.
	 * @param user - El usuario que se desea autenticar.
	 * @param pass - La clave ingresada
	 * @return True si la autenticacion es exitosa, False de lo contrario.
	 * @throws Exception si el usuario no existe.
	 */
	public static boolean authenticate(String user, String pass) throws Exception
	{
		
		initHash();
		initKeys();

		//Buscar la clave y la llave cifrada en el hash
		String passCifrada = hash.get(user);
		SecretKey key = keys.get(user);

		if (user == null || user.equals("")){
			throw new Exception("User invalid.");
		}

		//Inicializar el cifrado
		Cipher desCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		desCipher.init(Cipher.DECRYPT_MODE,key,desCipher.getParameters());
		
		//Desencriptar la clave guardada y comparar con la entrada
		byte[] passDecrypted = desCipher.doFinal(Base64.getDecoder().decode(passCifrada));
		return pass.equals(new String(passDecrypted));

	}
	
	@Deprecated
	/**
	 * Muestra la contraseña en texto plano (solo para debuggear).
	 * @param user - El usuario de quien se desea la clave
	 * @return La clave del usuario
	 * @throws Exception - Si el usuario buscado no existe.
	 */
	public static String getRawPassword(String user) throws Exception
	{
		
		initHash();
		initKeys();

		//Buscar la clave y la llave cifrada en el hash
		String passCifrada = hash.get(user);
		SecretKey key = keys.get(user);

		if (user == null || user.equals(""))
		{
			throw new Exception("User invalid.");
		}

		//Inicializar el cifrado
		Cipher desCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		desCipher.init(Cipher.DECRYPT_MODE,key,desCipher.getParameters());
		
		//Desencriptar la clave guardada y comparar con la entrada
		byte[] passDecrypted = desCipher.doFinal(Base64.getDecoder().decode(passCifrada));
		return new String(passDecrypted);
	}
	
	@Deprecated
	/**
	 * Devuelve la tabla con la inforacion de los usuarios y las claves cifradas.
	 * @return La tabla de informacion.
	 */
	public static Hashtable<String, String> getHash()
	{
		return hash;
	}


	//Inits
	private static void initHash()
	{
		if (hash == null)
		{
			hash = new Hashtable<String, String>();
		}
	}

	private static void initKeyGen()
	{
		if (keyGen == null)
		{
			try
			{
				keyGen = KeyGenerator.getInstance("DES");
			} 
			catch (NoSuchAlgorithmException e) 
			{
				e.printStackTrace();
			}
		}
	}

	private static void initKeys()
	{
		if (keys == null)
		{
			keys = new Hashtable<String, SecretKey>();
		}
	}
}
