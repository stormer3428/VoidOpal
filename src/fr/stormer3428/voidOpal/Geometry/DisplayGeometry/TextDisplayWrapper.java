package fr.stormer3428.voidOpal.Geometry.DisplayGeometry;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.TextDisplay;
import org.bukkit.entity.Display.Billboard;
import org.bukkit.entity.Display.Brightness;
import org.bukkit.entity.TextDisplay.TextAlignment;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Matrix4f;

public class TextDisplayWrapper extends DisplayWrapper<TextDisplay>{

	public TextDisplayWrapper(Location origin) {this(origin, new Vector());}
	public TextDisplayWrapper(Location origin, Vector location) {this(origin, location, origin.getDirection());}
	public TextDisplayWrapper(Location origin, Vector location, Vector direction) {
		super(TextDisplay.class, origin, location, direction);
		setBillboard(Billboard.CENTER);
		setShadowRadius(0);
	}

	private int lineWidth = 2048;
	private byte textOpacity = 127;
	private Color backgroundColor = Color.WHITE;
	private String text = "";
	private boolean defaultBackground = true;
	private boolean seeThrough = true;
	private boolean shadowed = true;
	private TextAlignment textAlignment = TextAlignment.CENTER;

	@Override
	public boolean create() {
		if(!super.create()) return false;
		TextDisplay display = getDisplay();
		display.setAlignment(textAlignment);
		display.setBackgroundColor(backgroundColor);
		display.setDefaultBackground(defaultBackground);
		display.setLineWidth(lineWidth);
		display.setSeeThrough(seeThrough);
		display.setShadowed(shadowed);
		display.setText(text);
		display.setTextOpacity(textOpacity);
		return true;
	}
	
	public TextDisplayWrapper setText(String text) {this.text = text;return this;}
	public TextDisplayWrapper setShadowed(boolean shadowed) {this.shadowed = shadowed;return this;}
	public TextDisplayWrapper setLineWidth(int lineWidth) {this.lineWidth = lineWidth;return this;}
	public TextDisplayWrapper setSeeThrough(boolean seeThrough) {this.seeThrough = seeThrough;return this;}
	public TextDisplayWrapper setTextOpacity(byte textOpacity) {this.textOpacity = textOpacity;return this;}
	public TextDisplayWrapper setTextAlignment(TextAlignment textAlignment) {this.textAlignment = textAlignment;return this;}
	public TextDisplayWrapper setBackgroundColor(Color backgroundColor) {this.backgroundColor = backgroundColor;return this;}
	public TextDisplayWrapper setDefaultBackground(boolean defaultBackground) {this.defaultBackground = defaultBackground;return this;}

	@Override public TextDisplayWrapper setScale(double scale) {this.scale = scale; updatePosition(); return this;}
	@Override public TextDisplayWrapper setWorld(World world) {super.setWorld(world); return this;}
	@Override public TextDisplayWrapper setOrigin(Vector origin) {super.setOrigin(origin); return this;}
	@Override public TextDisplayWrapper setOrigin(Location origin) {super.setOrigin(origin); return this;}
	@Override public TextDisplayWrapper setGlowing(boolean glowing) {super.setGlowing(glowing); return this;}
	@Override public TextDisplayWrapper setLocation(Vector loc) {super.setLocation(loc); return this;}
	@Override public TextDisplayWrapper setBillboard(Billboard billboard) {super.setBillboard(billboard); return this;}
	@Override public TextDisplayWrapper setDirection(Vector direction) {super.setDirection(direction); return this;}
	@Override public TextDisplayWrapper setViewRange(float viewRange) {super.setViewRange(viewRange); return this;}
	@Override public TextDisplayWrapper setBrightness(Brightness brightness) {super.setBrightness(brightness); return this;}
	@Override public TextDisplayWrapper setShadowRadius(float shadowRadius) {super.setShadowRadius(shadowRadius); return this;}
	@Override public TextDisplayWrapper setTransformation(Transformation transformation) {super.setTransformation(transformation); return this;}
	@Override public TextDisplayWrapper setGlowColorOverride(Color glowColorOverride) {super.setGlowColorOverride(glowColorOverride); return this;}
	@Override public TextDisplayWrapper setInterpolationDelay(int interpolationDelay) {super.setInterpolationDelay(interpolationDelay); return this;}
	@Override public TextDisplayWrapper setTransformationMatrix(Matrix4f transformationMatrix) {super.setTransformationMatrix(transformationMatrix); return this;}
	
	public int getLineWidth() {return lineWidth;}
	public byte getTextOpacity() {return textOpacity;}
	public String getText() {return text;}
	public Color getBackgroundColor() {return backgroundColor;}
	public boolean isDefaultBackground() {return defaultBackground;}
	public boolean isSeeThrough() {return seeThrough;}
	public boolean isShadowed() {return shadowed;}
	public TextAlignment getTextAlignment() {return textAlignment;}
}
