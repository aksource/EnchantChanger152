package IR.IRUtil;

public class RecordBlock extends Record {
	public static final String TYPE = "block";

	public static class DropItem {
		public int id;
		public int meta;
		public int min;
		public int max;
		public float chance;
	}

	public float hardness;
	public float blastRegistantce;
	public float lightValue;
	public String material;
	public String stepSound;
	public boolean isOpaqueCube;
	public float blockHeight;
	public int renderType;
	public boolean collidable;
	public DropItem[] dropItems;
	public boolean alphaBlending;

	@Override
	public String getRecordType() {
		return TYPE;
	}

	@Override
	public int getItemListIndex() {
		return id;
	}

}
