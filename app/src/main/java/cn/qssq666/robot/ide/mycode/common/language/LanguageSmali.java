package cn.qssq666.robot.ide.mycode.common.language;
import cn.qssq666.CoreLibrary0;import cn.qssq666.robot.ide.mycode.common.Language;

public class LanguageSmali extends Language {
	private static Language sInstance = null;

	private final static String[] keywords = { "iput-object", "iput-boolean",
			"iput", "new-array", "aput-object", "const", "move", "goto",
			"check-cast", "const-class", "if-eq", "if-ne", "if-lt", "if-le",
			"if-gt", "if-ge", "return-void", "const-string", "invoke-static",
			"sput-object", "invoke-virtual", "invoke-super",
			"invoke-interface", "invoke-direct", "new-instance", "public",
			"enum", "private", "protected", "synthetic", "annotation",
			"implements", "static", "final", "constructor", ".end", ".catch",
			".locals", ".method", ".field", ".interface", ".class", ".super",
			".parameter", ".line", ".registers", ".prologue", ".annotation" };

	private final static char[] operators = {};

	public static Language getInstance() {
		if (sInstance == null) {
			sInstance = new LanguageSmali();
		}
		return sInstance;
	}

	private LanguageSmali() {
		super.setKeywords(keywords);
		super.setOperators(operators);
	}

	@Override
	public boolean isLineAStart(char c) {
		return false;
	}

	@Override
	public boolean isLineBStart(char c) {
		return (c == '#');
	}

	@Override
	public boolean isLineStart(char c0, char c1) {
		return false;
	}

	@Override
	public boolean isMultilineStartDelimiter(char c0, char c1) {
		return false;
	}
}
