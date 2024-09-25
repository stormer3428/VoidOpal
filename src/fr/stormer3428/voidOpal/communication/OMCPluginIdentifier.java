package fr.stormer3428.voidOpal.communication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;

public final class OMCPluginIdentifier{

	private final String macAddr;
	private final InetAddress ip;
	private final int port;
	private final String eulaHash;
	
	public static OMCPluginIdentifier local() {
		return new OMCPluginIdentifier(getMacAddress(), null, Bukkit.getPort(), new String(getEulaHash()));
	}

	private OMCPluginIdentifier(String macAddr, InetAddress ip, int port, String eulaHash) {
		this.macAddr = macAddr;
		this.ip = ip;
		this.port = port;
		this.eulaHash = eulaHash;
	}
	
	public InetAddress getIp(){return ip;}
	public String getMacAddr(){return macAddr;}
	public int getPort(){return port;}

	@Override public String toString(){
		return "{"+String.join("|", macAddr, port + "", eulaHash) + "}";
	}

	public static OMCPluginIdentifier parse(InetAddress inetAddress, String serialized) {
		String[] split = serialized
				.substring(1, serialized.length() - 2)
				.split("|");
		String macAddr = split[0];
		int port = Integer.parseInt(split[1]);
		String eulaHash = split[2];
		return new OMCPluginIdentifier(macAddr, inetAddress, port, eulaHash);
	}

	public static String getMacAddress() {
		try {
			InetAddress localHost = InetAddress.getLocalHost();
			NetworkInterface ni = NetworkInterface.getByInetAddress(localHost);
			byte[] hardwareAddress = ni.getHardwareAddress();
			String[] hexadecimal = new String[hardwareAddress.length];
			for (int i = 0; i < hardwareAddress.length; i++) {
				hexadecimal[i] = String.format("%02X", hardwareAddress[i]);
			}
			String macAddress = String.join("-", hexadecimal);
			return macAddress;
		} catch (UnknownHostException | SocketException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static String getEulaHash() {
		try {
			File eula = new File(Bukkit.getUpdateFolderFile().getParentFile().getParentFile(), "eula.txt");
			try (BufferedReader reader = new BufferedReader(new FileReader(eula))) {
				if(!reader.readLine().startsWith("#")) {
					generateMojangText(eula);
					return getEulaHash();
				}
			}
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] digest = md.digest(Files.readAllBytes(eula.toPath()));
			return Base64.getEncoder().encodeToString(digest);
		} catch (IOException | NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static final SimpleDateFormat sdfDate = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy");

	private static void generateMojangText(File eula) throws IOException {
		List<String> text = Arrays.asList(
				"#By changing the setting below to TRUE you are indicating your agreement to our EULA (https://aka.ms/MinecraftEULA).",
				"#"+sdfDate.format(new Date()),
				"eula=true"
				);
		Files.write(eula.toPath(), text, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
	}
	
}
