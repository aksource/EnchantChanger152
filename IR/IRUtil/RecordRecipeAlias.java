package IR.IRUtil;

public class RecordRecipeAlias extends Record {
	public static final String TYPE = "recipe";

	@Override
	public String getRecordType() {
		return TYPE;
	}

	@Override
	public int getItemListIndex() {
		return id;
	}

}
