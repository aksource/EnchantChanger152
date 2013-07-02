package IR.IRUtil;

public class RecordArmor extends RecordItem {

	public static final String TYPE = "armor";

	public int armorType;
	public String armorMaterial;
	public int damageReductionAmount;
	public int durability;
	public int enchantability;
	public String armorTextureFilePath;

	@Override
	public String getRecordType() {
		return TYPE;
	}
}
