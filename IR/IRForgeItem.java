package IR;

import net.minecraft.item.Item;

/**
 * レコードを具象化するクラス
 * 対応するRecordが継承関係にあってもJAVAの制約上こちらは継承を使えないので厄介
 * （例えばIRForgeItemを継承したIRForgeFoodクラスは作れない）
 * 派生クラスで新たに付け加えられた値や、コンストラクタ内でしか指定できない値は、基本的にコンストラクタ内で済ます
 * 継承元のクラスでもともと外部から変更可能な値は、継承関係の恩恵を得るためにIRConstructor内のsetParamsメソッドで行う
 * @author gnsk
 *
 */
public class IRForgeItem extends Item{

	protected String textureFile;

	public IRForgeItem(int id, String textureFile) {
		super(id);
//		this.textureFile = textureFile;
	}

//	@Override
//	public String getTextureFile() {
//		return textureFile;
//	}
}
