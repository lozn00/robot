package cn.qssq666.robot.ide.mycode.common.language;
import cn.qssq666.robot.ide.mycode.common.Language;
//ignore_start
public class LanguageMarkDown extends Language {
	private static Language sInstance = null;

	private final static String[] keywords = { "#" ,"```","**","*","--"};

	private final static char[] operators = {
		'#',
	};

	public static Language getInstance() {
		if (sInstance == null) {
			sInstance = new LanguageMarkDown();
		}
		return sInstance;
	}

	private LanguageMarkDown() {
		super.setKeywords(keywords);
		super.setOperators(operators);
	}
	
	@Override
	public boolean isLineAStart(char c){
		return false;
	}
	
	@Override
	public boolean isLineBStart(char c){
		return (c == '#');
	}

	@Override
	public boolean isLineStart(char c0, char c1){
		return false;
	}

	@Override
	public boolean isMultilineStartDelimiter(char c0, char c1){
		return false;
	}
}

//ignore_end