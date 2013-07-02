package IR;

import net.minecraft.item.Item;
import IR.IRUtil.IRInvalidValueException;
import IR.IRUtil.Record;
import IR.IRUtil.RecordItem;

public class IRConstructorItem extends IRConstructorBase {

	@Override
	public Object construct(Record record) throws IRInvalidValueException {
		if (record.id < 0 && Item.itemsList.length <= record.getItemListIndex())
			throw new IRInvalidValueException("ItemIDは256から" + (Item.itemsList.length-1) + "までの値を設定してください");

		return construct((RecordItem)record);
	}

	protected Object construct(RecordItem record) throws IRInvalidValueException {
		Item item = new IRForgeItem(record.id, record.textureFile);
		return setParams(item, record);
	}

	protected Object setParams(Item item, RecordItem record) {
		item.setUnlocalizedName("InstantRecipesItem" + record.getItemListIndex() + record.name);
		item.setMaxStackSize(record.stackSize);
//		item.setIconIndex(record.textrueIndex);
		if (record.isFull3d) item.setFull3D();

		return super.setParams(item, record);
	}
}
