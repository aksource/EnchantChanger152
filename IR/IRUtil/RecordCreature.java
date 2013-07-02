package IR.IRUtil;

public class RecordCreature extends Record {

	public static final String TYPE = "creature";

	public String model;
	public String modelTexture;
	public String entity;
	public int health;



	@Override
	public String getRecordType() {
		return TYPE;
	}

	@Override
	public int getItemListIndex() {
		return 127;
	}

}
