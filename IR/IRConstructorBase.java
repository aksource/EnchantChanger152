package IR;

import net.minecraft.src.ModLoader;
import IR.IRUtil.IRInvalidValueException;
import IR.IRUtil.Record;

/**
 * Minecraftで用いられるItemクラスやBlockクラス（Minecraftオブジェクトと呼ぶことにする）を継承したクラスを生成するためのクラス
 * JAVAでは多重継承ができないので
 * @author gnsk
 *
 */
public abstract class IRConstructorBase {

	/**
	 * Minecraftオブジェクトクラスを継承したクラスを生成する
	 * 対応するRecordクラスの継承関係にそって基底クラスから順に対応するIRConstructorのconstructメソッドが呼び出されRecordの正当性の評価が行われる
	 * 最終的にRecordに対応するIRConstructorで実際のMinecraftオブジェクトが生成される
	 * @param record
	 * @return recordに対応したMinecraftオブジェクト（を継承したクラス）のインスタンス
	 * @throws IRInvalidValueException
	 */
	public abstract Object construct(Record record) throws IRInvalidValueException;

	/**
	 * 継承元のMinecraftオブジェクト（既存のクラスItemやItemToolなど）に対してRecordの設定項目を適用する
	 * 対応するMinecraftオブジェクトの継承関係にそって実際にオブジェクトを生成したクラスから順にsetParamsメソッドを呼び出していく
	 * @param object 既存のMinecraftオブジェクトのインスタンス(ItemクラスやらBlockクラスやら）
	 * @param record
	 * @return 引数のobject
	 */
	protected Object setParams(Object object, Record record) {
		addName(object, record);
		return object;
	}

	protected void addName(Object object, Record record) {
		ModLoader.addName(object, record.name);
		for (Record.NameEntry e : record.localNames) {
			ModLoader.addName(object, e.region, e.name);
		}
	}
}
