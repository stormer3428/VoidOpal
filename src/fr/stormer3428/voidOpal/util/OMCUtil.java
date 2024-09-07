package fr.stormer3428.voidOpal.util;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;

import net.md_5.bungee.api.ChatColor;

public class OMCUtil {

	private static final Pattern hexColorCodePattern = Pattern.compile("#[a-fA-F0-9]{6}");
	public static final int[] values = 			{1000,  900, 500,  400, 100,   90,  50,   40,  10,    9,   5,    4,   1};
	public static final String[] romanLetters = { "M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
	public static final Map<BlockFace, int[][]> BLOCKFACE_ADJACENCY_MAP = Map.of(
			BlockFace.UP,new int[][] {{-1, 0,-1},{ 0, 0,-1},{ 1, 0,-1},{-1, 0, 0},{ 0, 0, 0},{ 1, 0, 0},{-1, 0, 1},{ 0, 0, 1},{ 1, 0, 1}},
			BlockFace.DOWN,new int[][] {{-1, 0,-1},{ 0, 0,-1},{ 1, 0,-1},{-1, 0, 0},{ 0, 0, 0},{ 1, 0, 0},{-1, 0, 1},{ 0, 0, 1},{ 1, 0, 1}},
			BlockFace.EAST,new int[][] {{ 0,-1,-1},{ 0, 0,-1},{ 0, 1,-1},{ 0,-1, 0},{ 0, 0, 0},{ 0, 1, 0},{ 0,-1, 1},{ 0, 0, 1},{ 0, 1, 1}},
			BlockFace.WEST,new int[][] {{ 0,-1,-1},{ 0, 0,-1},{ 0, 1,-1},{ 0,-1, 0},{ 0, 0, 0},{ 0, 1, 0},{ 0,-1, 1},{ 0, 0, 1},{ 0, 1, 1}},
			BlockFace.NORTH,new int[][] {{-1,-1, 0},{ 0,-1, 0},{ 1,-1, 0},{-1, 0, 0},{ 0, 0, 0},{ 1, 0, 0},{-1, 1, 0},{ 0, 1, 0},{ 1, 1, 0}},
			BlockFace.SOUTH,new int[][] {{-1,-1, 0},{ 0,-1, 0},{ 1,-1, 0},{-1, 0, 0},{ 0, 0, 0},{ 1, 0, 0},{-1, 1, 0},{ 0, 1, 0},{ 1, 1, 0}}
			);

	public static final ArrayList<Material> PICKAXES = new ArrayList<Material>();
	public static final ArrayList<Material> SHOVELS = new ArrayList<Material>();
	public static final ArrayList<Material> HOES = new ArrayList<Material>();
	public static final ArrayList<Material> SWORDS = new ArrayList<Material>();
	public static final ArrayList<Material> AXES = new ArrayList<Material>();

	public static final ArrayList<Material> TOOLS = new ArrayList<Material>();
	
	static {
		PICKAXES.add(Material.DIAMOND_PICKAXE);
		PICKAXES.add(Material.GOLDEN_PICKAXE);
		PICKAXES.add(Material.IRON_PICKAXE);
		PICKAXES.add(Material.NETHERITE_PICKAXE);
		PICKAXES.add(Material.STONE_PICKAXE);
		PICKAXES.add(Material.WOODEN_PICKAXE);

		SHOVELS.add(Material.DIAMOND_SHOVEL);
		SHOVELS.add(Material.GOLDEN_SHOVEL);
		SHOVELS.add(Material.IRON_SHOVEL);
		SHOVELS.add(Material.NETHERITE_SHOVEL);
		SHOVELS.add(Material.STONE_SHOVEL);
		SHOVELS.add(Material.WOODEN_SHOVEL);
		
		HOES.add(Material.DIAMOND_HOE);
		HOES.add(Material.GOLDEN_HOE);
		HOES.add(Material.IRON_HOE);
		HOES.add(Material.NETHERITE_HOE);
		HOES.add(Material.STONE_HOE);
		HOES.add(Material.WOODEN_HOE);
		
		SWORDS.add(Material.DIAMOND_SWORD);
		SWORDS.add(Material.GOLDEN_SWORD);
		SWORDS.add(Material.IRON_SWORD);
		SWORDS.add(Material.NETHERITE_SWORD);
		SWORDS.add(Material.STONE_SWORD);
		SWORDS.add(Material.WOODEN_SWORD);
		
		AXES.add(Material.DIAMOND_AXE);
		AXES.add(Material.GOLDEN_AXE);
		AXES.add(Material.IRON_AXE);
		AXES.add(Material.NETHERITE_AXE);
		AXES.add(Material.STONE_AXE);
		AXES.add(Material.WOODEN_AXE);

		TOOLS.addAll(PICKAXES);
		TOOLS.addAll(SHOVELS);
		TOOLS.addAll(HOES);
		TOOLS.addAll(SWORDS);
		TOOLS.addAll(AXES);
		TOOLS.add(Material.TRIDENT);
		TOOLS.add(Material.SHIELD);
		TOOLS.add(Material.BOW);
		TOOLS.add(Material.CROSSBOW);
		TOOLS.add(Material.FISHING_ROD);
		TOOLS.add(Material.FLINT_AND_STEEL);
		TOOLS.add(Material.CARROT_ON_A_STICK);
		TOOLS.add(Material.WARPED_FUNGUS_ON_A_STICK);
	}

	public static final ArrayList<Material> HELMETS = new ArrayList<Material>();
	public static final ArrayList<Material> CHESTPLATES = new ArrayList<Material>();
	public static final ArrayList<Material> LEGGINGS = new ArrayList<Material>();
	public static final ArrayList<Material> BOOTS = new ArrayList<Material>();
	
	public static final ArrayList<Material> ARMOR = new ArrayList<Material>();

	static {

		HELMETS.add(Material.CHAINMAIL_HELMET);
		HELMETS.add(Material.DIAMOND_HELMET);
		HELMETS.add(Material.GOLDEN_HELMET);
		HELMETS.add(Material.IRON_HELMET);
		HELMETS.add(Material.LEATHER_HELMET);
		HELMETS.add(Material.NETHERITE_HELMET);
		
		CHESTPLATES.add(Material.CHAINMAIL_CHESTPLATE);
		CHESTPLATES.add(Material.DIAMOND_CHESTPLATE);
		CHESTPLATES.add(Material.GOLDEN_CHESTPLATE);
		CHESTPLATES.add(Material.IRON_CHESTPLATE);
		CHESTPLATES.add(Material.LEATHER_CHESTPLATE);
		CHESTPLATES.add(Material.NETHERITE_CHESTPLATE);
		
		LEGGINGS.add(Material.CHAINMAIL_LEGGINGS);
		LEGGINGS.add(Material.DIAMOND_LEGGINGS);
		LEGGINGS.add(Material.GOLDEN_LEGGINGS);
		LEGGINGS.add(Material.IRON_LEGGINGS);
		LEGGINGS.add(Material.LEATHER_LEGGINGS);
		LEGGINGS.add(Material.NETHERITE_LEGGINGS);

		BOOTS.add(Material.CHAINMAIL_BOOTS);
		BOOTS.add(Material.DIAMOND_BOOTS);
		BOOTS.add(Material.GOLDEN_BOOTS);
		BOOTS.add(Material.IRON_BOOTS);
		BOOTS.add(Material.LEATHER_BOOTS);
		BOOTS.add(Material.NETHERITE_BOOTS);

		ARMOR.addAll(HELMETS);
		ARMOR.addAll(CHESTPLATES);
		ARMOR.addAll(LEGGINGS);
		ARMOR.addAll(BOOTS);
		ARMOR.add(Material.TURTLE_HELMET);
	}

	public static String intToRoman(int num){
		StringBuilder roman = new StringBuilder();
		for(int i = 0; i < values.length; i++){
			while(num >= values[i]){
				num = num - values[i];
				roman.append(romanLetters[i]);
			}
		}
		return roman.toString();
	}

	public static float noteBlockToPitch(int note) {
		return (float) ((Math.pow(2, note/12f))/2f);
	}

	public static String translateChatColor(String s) {
		if(s == null) return null;
		s = ChatColor.translateAlternateColorCodes('&', s);
		s = ChatColor.translateAlternateColorCodes('$', s);
		s = ChatColor.translateAlternateColorCodes('§', s);

		Matcher hexMatcher = hexColorCodePattern.matcher(s);
		while(hexMatcher.find()) {
			String color = s.substring(hexMatcher.start(), hexMatcher.end());
			s = s.replace(color, ChatColor.of(color) + "");
			hexMatcher = hexColorCodePattern.matcher(s);
		}
		return s;
	}

	public static double map(double input_start, double input_end, double output_start, double output_end, double input) {
		return map(input, input_start, input_end, output_start, output_end, 3);
	}

	public static double map(double sourceNumber, double fromA, double fromB, double toA, double toB, int decimalPrecision ) {
		double deltaA = fromB - fromA;
		double deltaB = toB - toA;
		double scale  = deltaB / deltaA;
		double negA   = -1 * fromA;
		double offset = (negA * scale) + toA;
		double finalNumber = (sourceNumber * scale) + offset;
		int calcScale = (int) Math.pow(10, decimalPrecision);
		return (double) Math.round(finalNumber * calcScale) / calcScale;
	}

	public static String gradientFromHex(String text, String from, String to) {
		return gradientFromInt(text, Integer.parseInt(from, 16), Integer.parseInt(to, 16));
	}

	public static String gradientFromInt(String text, int from, int to) {
		return gradient(text, Color.fromRGB(from), Color.fromRGB(to));
	}

	public static String gradient(String text, Color colorA, Color colorB) {
		int rA = colorA.getRed();
		int gA = colorA.getGreen();
		int bA = colorA.getBlue();
		int rB = colorB.getRed();
		int gB = colorB.getGreen();
		int bB = colorB.getBlue();

		StringBuilder builder = new StringBuilder();
		int i = 0;
		for(char c : text.toCharArray()) {
			i++;
//			builder.append(String.format("§x§%01§%01§%01§%01§%01§%01", 
			builder.append(String.format("#%02x%02x%02x", 
					(int) map(0, text.length(), rA, rB, i),
					(int) map(0, text.length(), gA, gB, i),
					(int) map(0, text.length(), bA, bB, i)
					));
			builder.append(c);
		}
		return OMCUtil.translateChatColor(builder.toString());		
	}

	public static String randomColorCode() {
		return String.format("#%06x", new Random().nextInt(0xFFFFFF));
	}

}














