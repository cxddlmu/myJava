package regex.json;

public class Token {
	JsonTokenType type;
	String value;

	public Token(JsonTokenType type, String value) {
		this.type = type;
		this.value = value;
	}
	public Token(JsonTokenType type, char c) {
		this.type = type;
		this.value = c+"";
	}
	public String toString() {
		return String.format("Token({%s}, {%s})", type, value);
	}
}
