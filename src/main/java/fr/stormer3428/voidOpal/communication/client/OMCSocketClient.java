package fr.stormer3428.voidOpal.communication.client;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Base64;
//import java.util.Base64;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import fr.stormer3428.voidOpal.communication.OMCEncryptionUtils;
import fr.stormer3428.voidOpal.communication.OMCPluginIdentifier;
import fr.stormer3428.voidOpal.communication.OMCProtocol;

public class OMCSocketClient implements Closeable{

	private final ExecutorService executor = Executors.newSingleThreadExecutor();
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;

	public OMCSocketClient(String ip, int port) throws UnknownHostException, IOException, RuntimeException {
		socket = new Socket();
		int attemptsLeft = 3;
		while(
				attemptsLeft > 0 
				&&
				!socket.isConnected()
				) try {
					attemptsLeft--;
					socket.connect(new InetSocketAddress(ip, port), 10000);
				}catch (SocketTimeoutException e) {
//					OMCLogger.systemError("Connection to " + ip + ":" + port + " timed out" + (attemptsLeft == 0 ? "" : ", retrying " + attemptsLeft + " more time" + (attemptsLeft>1?"s":"")));
					if(attemptsLeft == 0) throw new RuntimeException(e.getClass().getSimpleName() + " Server request timeout", e);
				}
		out = new PrintWriter(socket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

	public String sendMessage(String header, String message) {
		Future<String> future = executor.submit(new Message(header, message));
		try { return future.get(10, TimeUnit.SECONDS);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			e.printStackTrace();
		}
		System.out.println("Server did not respond " + socket.getInetAddress().toString() + ":" + socket.getPort());
		return null;
	}

	@Override
	public void close() throws IOException {
		in.close();
		out.close();
		socket.close();
	}

//	public void print(String s) {
//		System.out.println("[client]\t: ("+s.length()+") " + s);
//	}

	private class Message implements Callable<String> {
		private final String message;
		private final String header;
		public Message(String header, String message) {this.message = message; this.header = header;}
		public String call() throws Exception {
//			print("Sending data to " + socket.getInetAddress().toString() + ":" + socket.getPort());

			String data = "";

			out.println(); 

			//		Step 1 |	get PubKey from server

			data = in.readLine(); 

			final PublicKey serverPublicKey;
			{
				String b64ServerPublicKey = data;
				byte[] encodedServerPublicKey = Base64.getDecoder().decode(b64ServerPublicKey);
				serverPublicKey = OMCEncryptionUtils.RSA.parsePublicKey(encodedServerPublicKey);
			}

			//		Step 2 |	get cyphered PubKey from client

			final KeyPair clientKeyPair = OMCEncryptionUtils.RSA.generateKeyPair();
			{
				byte[] encodedClientPublicKey = clientKeyPair.getPublic().getEncoded();

				byte[] encodedClientPublicKey1 = Arrays.copyOfRange(encodedClientPublicKey, 0, 128);
				byte[] encodedClientPublicKey2 = Arrays.copyOfRange(encodedClientPublicKey, 128, encodedClientPublicKey.length);

				byte[] encryptedClientPublicKey1 = OMCEncryptionUtils.RSA.encrypt(serverPublicKey, encodedClientPublicKey1);
				byte[] encryptedClientPublicKey2 = OMCEncryptionUtils.RSA.encrypt(serverPublicKey, encodedClientPublicKey2);

				String b64ClientPublicKey1 = Base64.getEncoder().encodeToString(encryptedClientPublicKey1);
				String b64ClientPublicKey2 = Base64.getEncoder().encodeToString(encryptedClientPublicKey2);

				out.println(b64ClientPublicKey1);
				out.println(b64ClientPublicKey2);
			}

			//		Step 3 |	get iv from server

			data = in.readLine(); 

			final PrivateKey clientPrivateKey = clientKeyPair.getPrivate();
			final IvParameterSpec iv;
			{
				String b64Iv = data;
				byte[] encryptedIv = Base64.getDecoder().decode(b64Iv);
				byte[] encodedIv = OMCEncryptionUtils.RSA.decrypt(clientPrivateKey, encryptedIv);
				iv = new IvParameterSpec(encodedIv);

			}

			//		Step 4 |	get AES key from server

			data = in.readLine();

			final SecretKey AESKey;
			{
				String b64AESKey = data;
				byte[] encryptedAESKey = Base64.getDecoder().decode(b64AESKey);
				byte[] encodedAESKey = OMCEncryptionUtils.RSA.decrypt(clientPrivateKey, encryptedAESKey);
				AESKey = OMCEncryptionUtils.AES.parseKey(encodedAESKey);
			}


			//		Step 4.5 |	identify client

			{
				byte[] encodedMessage = OMCPluginIdentifier.local().toString().getBytes();
				byte[] encryptedMessage = OMCEncryptionUtils.AES.encrypt("AES/CBC/PKCS5Padding", encodedMessage, AESKey, iv);
				String b64Message = Base64.getEncoder().encodeToString(encryptedMessage);

				out.println(b64Message);
			}


			//		Step 4.6 |	get request header from client

			{
				byte[] encodedMessage = header.getBytes();
				byte[] encryptedMessage = OMCEncryptionUtils.AES.encrypt("AES/CBC/PKCS5Padding", encodedMessage, AESKey, iv);
				String b64Message = Base64.getEncoder().encodeToString(encryptedMessage);

				out.println(b64Message);
			}


			//		Step 5 |	get request from client

			{
				byte[] encodedMessage = message.getBytes();
				byte[] encryptedMessage = OMCEncryptionUtils.AES.encrypt("AES/CBC/PKCS5Padding", encodedMessage, AESKey, iv);
				String b64Message = Base64.getEncoder().encodeToString(encryptedMessage);

				out.println(b64Message);
			}

			//		Step 6 |	get response from server

			data = getMultilineResponse();

			final String response;
			{
				String b64Response = data;
				byte[] encryptedResponse = Base64.getDecoder().decode(b64Response);
				byte[] encodedResponse = OMCEncryptionUtils.AES.decrypt("AES/CBC/PKCS5Padding", encryptedResponse, AESKey, iv);

				response = new String(encodedResponse);
			}
//			print(OMCUtil.byteArrayToHexString(response.getBytes()));
			return response;
		}
	}

	private String getMultilineResponse() throws IOException {
		StringBuilder response = new StringBuilder();
		String current = "";
		while(!current.endsWith(OMCProtocol.EOF)) {
			response.append("\n");
			current = in.readLine();
			response.append(current);
		}
		return response.substring(1, response.length() - 2);
	}
}
