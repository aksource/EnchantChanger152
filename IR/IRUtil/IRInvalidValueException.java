package IR.IRUtil;

/**
 * レコードの値に不正がありItemやBlockを追加できなかった際に発生する例外
 * ユーザーが原因を特定できるように原因となったレコードを指定すること
 * @author gnsk
 *
 */
public class IRInvalidValueException extends Exception {

	public IRInvalidValueException(String s, Throwable throwable) {
		super(s, throwable);
	}

	public IRInvalidValueException(String s) {
		super(s);
	}

	public IRInvalidValueException(Throwable throwable) {
		super(throwable);
	}

	Record record;

	public IRInvalidValueException() {
		super();
	}

	public IRInvalidValueException(String message, Throwable cause, Record causeRecord) {
		super(message, cause);
		setCauseRecord(causeRecord);
	}

	public IRInvalidValueException(String message, Record causeRecord) {
		super(message);
		setCauseRecord(causeRecord);
	}

	public IRInvalidValueException(Throwable cause, Record causeRecord) {
		super(cause);
		setCauseRecord(causeRecord);
	}

	public void setCauseRecord(Record causeRecord) {
		this.record = causeRecord;
	}

	@Override
	public String getMessage() {
		if (this.record == null) return super.getMessage();
		StringBuilder sb = new StringBuilder();
		return sb.append(super.getMessage()).append(" (id : ").append(record.id).append(", name : ").append(record.name).append(")").toString();
	}
}
