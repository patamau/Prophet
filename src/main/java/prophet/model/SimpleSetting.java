package prophet.model;

public class SimpleSetting implements ISetting {
	
	public static final String SETTING_NAME_DEF = "Unnamed";

	private String name;
	private IWorld world;
	
	public SimpleSetting() {
		this(SETTING_NAME_DEF);
	}
	
	public SimpleSetting(final String name) {
		this.name = name;
		this.world = new SimpleWorld();
	}
	
	public void reset() {
		this.name = SETTING_NAME_DEF;
		this.world.reset();
	}
	
	public void init() {
		this.world.update();
	}
	
	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public IWorld getWorld() {
		return world;
	}

}
