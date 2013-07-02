package IR.IRUtil;

public class RecordSword extends RecordItem {
	public static final String TYPE = "sword";

	public String toolMaterial;
	public int maxUse;
	public int damage;
	public int enchantability;

	@Override
	public String getRecordType() {
		return TYPE;
	}
}
