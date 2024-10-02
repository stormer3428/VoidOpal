package fr.stormer3428.voidOpal.communication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
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

import fr.stormer3428.voidOpal.logging.OMCLogger;
import fr.stormer3428.voidOpal.plugin.OMCCore;

public final class OMCPluginIdentifier implements Serializable{

	private static final long serialVersionUID = 6272441803003311223L;
	
	public final String licenceString;
	public final String pluginName;
	public final InetAddress ip;

	public boolean complete;
	public String macAddr;
	public int port;
	public String eulaHash;
	
	public static OMCPluginIdentifier local() {
		return new OMCPluginIdentifier(getLicenseString(), OMCCore.getJavaPlugin().getDescription().getName(), getMacAddress(), null, Bukkit.getPort(), getEulaHash(), false);
	}

	private static String getLicenseString() {
		return OMCCore.getOMCChildPlugin().getLicenseString();
	}

	private OMCPluginIdentifier(String licenceString, String pluginName, String macAddr, InetAddress ip, int port, String eulaHash, boolean complete) {
		this.licenceString = licenceString;
		this.pluginName = pluginName;
		this.macAddr = macAddr;
		this.ip = ip;
		this.port = port;
		this.eulaHash = eulaHash;
		this.complete = complete;
	}
	
	public OMCPluginIdentifier(String licenceString, String pluginName, InetAddress ip) {
		this(licenceString, pluginName, null, ip, -1, null, false);
	}

	public InetAddress getIp(){return ip;}
	public String getMacAddr(){return macAddr;}
	public int getPort(){return port;}

	@Override public String toString(){
		return "{"+String.join("|", licenceString, pluginName, macAddr, ip + "", port + "", eulaHash) + "}";
	}

	public static OMCPluginIdentifier parse(InetAddress inetAddress, String serialized) {
		String[] split = serialized
				.substring(1, serialized.length() - 2)
				.split("\\|");
		String licenceString = split[0];
		String pluginName = split[1];
		String macAddr = split[2];
		//split[3] ip, skipped
		int port = Integer.parseInt(split[4]);
		String eulaHash = split[5];
		return new OMCPluginIdentifier(licenceString, pluginName, macAddr, inetAddress, port, eulaHash, true);
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

	public boolean matches(OMCPluginIdentifier other) {
		if(complete && !other.complete) return other.matches(this);
		if(!licenceString.equals(other.licenceString)) {
			System.out.println("mismatched license string " + licenceString + " " + other.licenceString);
			return false;
		}
		if(!pluginName.equals(other.pluginName)) {
			System.out.println("mismatched pluginName " + pluginName + " " + other.pluginName);
			return false;
		}
		if(!ip.toString().equals(other.ip.toString())) {
			System.out.println("mismatched ip " + ip.toString() + " " + other.ip.toString());
			return false;
		}
		
		if(!complete) {
			System.out.println("Current identifier is incomplete, partial match accepted");
			return true;
		}

		if(!macAddr.equals(other.macAddr)) {
			System.out.println("mismatched macAddr " + macAddr + " " + other.macAddr);
			return false;
		}
		if(port != other.port) {
			System.out.println("mismatched port " + port + " " + other.port);
			return false;
		}
		if(!eulaHash.equals(other.eulaHash)) {
			System.out.println("mismatched eulaHash " + eulaHash + " " + other.eulaHash);
			return false;
		}
		System.out.println("matched");
		return true;
	}

	public void complete(OMCPluginIdentifier other) {
		if(complete) throw new RuntimeException("Tried to complete already complete identifier!");
		if(!other.complete) throw new RuntimeException("Tried to complete identifier with incomplete identifier!");
		OMCLogger.systemNormal("Completed license " + toString());
		OMCLogger.systemNormal("With " + other.toString());
		this.macAddr = other.macAddr;
		this.port = other.port;
		this.eulaHash = other.eulaHash;
		this.complete = true;
		OMCLogger.systemNormal(toString());
	}
	
}
