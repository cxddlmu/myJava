package regex;

public class Token {
	TokenType type;
	String value;

	public Token(TokenType type, String value) {
		this.type = type;
		this.value = value;
	}
	public Token(TokenType type, char c) {
		this.type = type;
		this.value = c+"";
	}
	public String toString() {
		return String.format("Token({%s}, {%s})", type, value);
	}
}
