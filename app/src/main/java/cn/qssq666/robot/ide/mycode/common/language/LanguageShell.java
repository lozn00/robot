package cn.qssq666.robot.ide.mycode.common.language;
import cn.qssq666.CoreLibrary0;import cn.qssq666.robot.ide.mycode.common.Language;

public class LanguageShell extends Language {
	private static Language sInstance = null;

	private final static String[] keywords = { "echo", "read", "while", "if",
			"for", "then", "elif", "else","until", "in", "done", "do", "fi",
			"function", "exec", "source", "true", "false", "test", "-d", "-f",
			"-w","-r","-x","-e" };
	
	private final static char[] operators = {
		'+','-','*','/','=','{','}','[',']',
	};

	public static Language getInstance() {
		if (sInstance == null) {
			sInstance = new LanguageShell();
		}
		return sInstance;
	}

	private LanguageShell() {
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
