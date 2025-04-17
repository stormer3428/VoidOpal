package fr.stormer3428.voidOpal.communication.server;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
//import java.util.Base64;
import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import fr.stormer3428.voidOpal.communication.OMCEncryptionUtils;
import fr.stormer3428.voidOpal.communication.OMCPluginIdentifier;
import fr.stormer3428.voidOpal.communication.OMCProtocol;
import fr.stormer3428.voidOpal.plugin.OMCCore;
import fr.stormer3428.voidOpal.plugin.PluginTied;

public abstract class SocketServer implements Closeable, PluginTied{

	private final int port;
	private final ServerSocket serverSocket;
	private final Thread thread = new Thread() { @Override public void run() {
		System.out.println("Starting socket server on port " + port);
		try { while(!Thread.currentThread().isInterrupted()) {
			Socket clientSocket;
			clientSocket = serverSocket.accept();
			new RequestHandlerThread(SocketServer.this, clientSocket).start();
		}} catch (IOException e) {e.printStackTrace();}
	}};

	//	public static SocketServer start(int port) {try { return new SocketServer(port);} catch (IOException e) { e.printStackTrace(); return null;}}

	protected SocketServer(int port) throws IOException {
		this.port = port;
		this.serverSocket = new ServerSocket(port);
		OMCCore.getOMCCore().registerPluginTied(this);
	}

	@Override public void onPluginEnable(){thread.start();}
	@Override public void onPluginReload() {}
	@Override public void onPluginDisable(){
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		thread.interrupt();
	}

	@Override public void close() throws IOException { thread.interrupt();}

	private static final class RequestHandlerThread extends Thread {
		protected final Socket socket;
		private final SocketServer server;

		public RequestHandlerThread(SocketServer server, Socket clientSocket) {
			this.server = server;
			this.socket = clientSocket;
			System.out.println("Incoming connection from " + socket.getInetAddress().toString());
		}

		public void print(String s) {System.out.println("[server]\t: ("+s.length()+") " + s);}

		BufferedReader in = null;
		PrintWriter out = null;
		public void run() {
			try {
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);
			} catch (IOException e) {return;}

			try (socket){
				String data = in.readLine();

				print("Connection from " + socket.getInetAddress().toString());

				//			Step 1 |	get PubKey from server

				print("Sending pubKey");
				final KeyPair serverKeyPair = OMCEncryptionUtils.RSA.generateKeyPair();
				{
					byte[] encodedServerPublicKey = serverKeyPair.getPublic().getEncoded();
					String b64ServerPublicKey = Base64.getEncoder().encodeToString(encodedServerPublicKey);

					out.println(b64ServerPublicKey);
				}
				print("PubKey sent");


				//			Step 2 |	get cyphered PubKey from client
				
				print("Receiving cyphered pubKey");
				final PrivateKey serverPrivateKey = serverKeyPair.getPrivate();
				final PublicKey clientPublicKey;
				{

					data = in.readLine(); 
					String b64ClientPublicKey1 = data;
					data = in.readLine(); 
					String b64ClientPublicKey2 = data;


					byte[] encodedClientPublicKey1;
					{
						byte[] encryptedClientPublicKey = Base64.getDecoder().decode(b64ClientPublicKey1);
						byte[] encodedClientPublicKey = OMCEncryptionUtils.RSA.decrypt(serverPrivateKey, encryptedClientPublicKey);

						encodedClientPublicKey1 = encodedClientPublicKey;
					}

					byte[] encodedClientPublicKey2;
					{
						byte[] encryptedClientPublicKey = Base64.getDecoder().decode(b64ClientPublicKey2);
						byte[] encodedClientPublicKey = OMCEncryptionUtils.RSA.decrypt(serverPrivateKey, encryptedClientPublicKey);

						encodedClientPublicKey2 = encodedClientPublicKey;
					}

					byte[] encodedClientPublicKey = Arrays.copyOf(encodedClientPublicKey1, encodedClientPublicKey1.length + encodedClientPublicKey2.length);
					System.arraycopy(encodedClientPublicKey2, 0, encodedClientPublicKey, encodedClientPublicKey1.length, encodedClientPublicKey2.length);

					clientPublicKey = OMCEncryptionUtils.RSA.parsePublicKey(encodedClientPublicKey);
				}
				print("Cyphered pubKey received!");

				//			Step 3 |	get iv from server

				print("Sending iv");
				final IvParameterSpec iv = OMCEncryptionUtils.AES.generateIv();
				{
					byte[] encodedIv = iv.getIV();
					byte[] encryptedIv = OMCEncryptionUtils.RSA.encrypt(clientPublicKey, encodedIv);
					String b64Iv = Base64.getEncoder().encodeToString(encryptedIv);

					out.println(b64Iv);
				}
				print("iv sent");

				//			Step 4 |	get AES key from server

				print("Sending AES key");
				final SecretKey AESKey = OMCEncryptionUtils.AES.generateAESKey();
				{
					byte[] encodedAESKey = AESKey.getEncoded();
					byte[] encryptedAESKey = OMCEncryptionUtils.RSA.encrypt(clientPublicKey, encodedAESKey);
					String b64AESKey = Base64.getEncoder().encodeToString(encryptedAESKey);

					out.println(b64AESKey);
				}
				print("AES key sent");


				//			Step 4.5 |	identify client

				print("Identyfying client");
				data = in.readLine(); 
				final OMCPluginIdentifier identifier;
				{
					String b64Message = data;
					byte[] encryptedMessage = Base64.getDecoder().decode(b64Message);
					byte[] encodedMessage = OMCEncryptionUtils.AES.decrypt("AES/CBC/PKCS5Padding", encryptedMessage, AESKey, iv);

					identifier = OMCPluginIdentifier.parse(socket.getInetAddress(), new String(encodedMessage));
				}
				print("Client identified as :");
				print(identifier.toString());


				//			Step 4.6 |	get request header from client

				print("Receiving request header");
				data = in.readLine(); 
				final String header;
				{
					String b64Message = data;
					byte[] encryptedMessage = Base64.getDecoder().decode(b64Message);
					byte[] encodedMessage = OMCEncryptionUtils.AES.decrypt("AES/CBC/PKCS5Padding", encryptedMessage, AESKey, iv);

					header = new String(encodedMessage);
				}
				print("Request header received :");
				print(header);

				//			Step 5 |	get request from client

				print("Receiving request body");
				data = in.readLine(); 
				final String request;
				{
					String b64Message = data;
					byte[] encryptedMessage = Base64.getDecoder().decode(b64Message);
					byte[] encodedMessage = OMCEncryptionUtils.AES.decrypt("AES/CBC/PKCS5Padding", encryptedMessage, AESKey, iv);

					request = new String(encodedMessage);
				}
				print("Request body received :");
				print(request);

				//			Step 6 |	get response from server

				print("Processing response");
				String serverResponse = server.processRequest(identifier, header, request);
				print("Sending response :");
//				print(OMCUtil.byteArrayToHexString(serverResponse.getBytes()));
				{
					byte[] encodedserverResponse = serverResponse.getBytes();
					byte[] encryptedserverResponse = OMCEncryptionUtils.AES.encrypt("AES/CBC/PKCS5Padding", encodedserverResponse, AESKey, iv);
					String b64serverResponse = Base64.getEncoder().encodeToString(encryptedserverResponse);

					out.println(b64serverResponse);
				}
				print("Response sent!");

				out.println(OMCProtocol.EOF);
			} catch (Exception e) {
				e.printStackTrace();
				try {
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	protected abstract String processRequest(OMCPluginIdentifier identifier, String header, String request);

}
