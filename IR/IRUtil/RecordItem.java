package IR.IRUtil;

public class RecordItem extends Record {
	public static final String TYPE = "item";

	public boolean isFull3d;
	public int stackSize;

	@Override
	public String getRecordType() {
		return TYPE;
	}

	@Override
	public int getItemListIndex() {
		return id + 256;
	}

}
