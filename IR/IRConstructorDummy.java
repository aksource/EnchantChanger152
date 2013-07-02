package IR;

import IR.IRUtil.IRInvalidValueException;
import IR.IRUtil.Record;

/**
 * 何も生成しないコンストラクタ
 * @author gnsk
 *
 */
public class IRConstructorDummy extends IRConstructorBase {

	@Override
	public Object construct(Record record) throws IRInvalidValueException {
		return null;
	}

}
