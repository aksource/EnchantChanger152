package IR.IRUtil;

public class RecordFood extends RecordItem {
	public static final String TYPE = "food";

	public int heal;
	public float saturationModifire;
	public boolean isWolfsFavoriteMeat;
	public int useDuration;
	public String useAction;
	public boolean alwaysEdible;
	public Effect effect;

	public static class Effect {
		public int id;
		public int duration;
		public int amplifier;
		public float probability;
	}

	@Override
	public String getRecordType() {
		return TYPE;
	}
}