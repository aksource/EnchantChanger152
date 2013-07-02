package IR.IRUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * InstantRecipesファイルフォーマット違反を表現する例外
 * 違反が発生した位置を追跡出来るようにオブジェクトやアレイを解析する際は、
 * この例外をキャッチしてmarkLocationでフィールド名を記録し再スローすること
 * @author webnaut
 *
 */
public class IRInvalidFormatException extends Exception {

	private List<String> tracer = new ArrayList<String>();

	public IRInvalidFormatException() {
		super();
	}

	public IRInvalidFormatException(String message, Throwable cause) {
		super(message, cause);
	}

	public IRInvalidFormatException(String message) {
		super(message);
	}

	public IRInvalidFormatException(String message, String location) {
		super(message);
		tracer.add(location);
	}

	public IRInvalidFormatException(String message, Throwable cause, String location) {
		super(message, cause);
		tracer.add(location);
	}

	public IRInvalidFormatException(Throwable cause) {
		super(cause);
	}

	public void markLocation(String location) {
		tracer.add(location);
	}

	@Override
	public String getMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.getMessage()).append(" (");

		for (int i = 0; i < tracer.size(); i++) {
			if (i != 0) sb.append("/");
			sb.append(tracer.get(tracer.size() - i - 1));
		}

		return sb.append(")").toString();
	}
}
