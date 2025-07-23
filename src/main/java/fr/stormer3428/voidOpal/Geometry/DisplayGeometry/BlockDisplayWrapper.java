package fr.stormer3428.voidOpal.Geometry.DisplayGeometry;

//public class BlockDisplayWrapper implements DisplayWrapper{
//
//	private final BlockDisplay blockDisplay;
//	private Location origin;
//	private Vector direction;
//	private Vector loc = new Vector(0,0,0);
//	private double scale = 1.0;
//		
//	public BlockDisplayWrapper(BlockDisplay blockDisplay) {
//		this.blockDisplay = blockDisplay;
//		this.origin = blockDisplay.getLocation();
//		this.direction = origin.getDirection();
//	}
//	
//	private BlockDisplayWrapper updatePosition() {
//		if(blockDisplay.isDead()) return this;
//		Location worldLocation = origin.clone().add(loc.clone().multiply(scale));
//		worldLocation.setDirection(direction);
//		blockDisplay.teleport(worldLocation);
//		return this;
//	}
//	
//	@Override
//	public DisplayWrapper setLocation(Vector loc) {
//		this.loc = loc;
//		return updatePosition();
//	}
//
//	@Override
//	public DisplayWrapper setWorld(World world) {
//		this.origin.setWorld(world);
//		return updatePosition();
//	}
//	
//	@Override
//	public void kill() {
//		blockDisplay.remove();
//	}
//	
//	@Override
//	public DisplayWrapper rotateAroundAxis(Vector axis, double radians) {
//		this.direction.rotateAroundAxis(axis, radians);
//		this.loc.rotateAroundAxis(axis, radians);
//		return updatePosition();
//	}
//	
//	@Override
//	public DisplayWrapper rotateAroundX(double radians) {
//		this.direction.rotateAroundX(radians);
//		this.loc.rotateAroundX(radians);
//		return updatePosition();
//	}
//	
//	@Override
//	public DisplayWrapper rotateAroundY(double radians) {
//		this.direction.rotateAroundY(radians);
//		this.loc.rotateAroundY(radians);
//		return updatePosition();
//	}
//	
//	@Override
//	public DisplayWrapper rotateAroundZ(double radians) {
//		this.direction.rotateAroundZ(radians);
//		this.loc.rotateAroundZ(radians);
//		return updatePosition();
//	}
//	
//	@Override
//	public DisplayWrapper setOrigin(Vector origin) {
//		return setOrigin(origin.toLocation(blockDisplay.getWorld()));
//	}
//	
//	@Override
//	public DisplayWrapper setOrigin(Location origin) {
//		this.origin = origin;
//		return updatePosition();
//	}
//
//	public Vector getDirection() {
//		return direction;
//	}
//
//	@Override
//	public DisplayWrapper setDirection(Vector direction) {
//		this.direction = direction;
//		return updatePosition();
//	}
//
//	@Override
//	public DisplayWrapper setScale(double scale) {
//		this.scale = scale;
//		this.blockDisplay.setDisplayHeight((float) scale);
//		this.blockDisplay.setDisplayWidth((float) scale);
//		return updatePosition();
//	}
//	
//	
//}
