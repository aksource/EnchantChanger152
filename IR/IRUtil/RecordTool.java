package IR.IRUtil;

public class RecordTool extends RecordItem {
	public static final String TYPE = "tool";

	public int toolType;
	public String toolMaterial;
	public int maxUse;
	public float efficiency;
	public int damage;
	public int enchantability;

	@Override
	public String getRecordType() {
		return TYPE;
	}
}
