package regex;

enum TokenType {
	INTEGER, PLUS('+'), MINUS('-'), MUL('*'), DIV('/'), EOF, LPAREN('('), RPAREN(
			')');
	private char opChar;
	static String ops="+-*/";
	
	public static boolean isOps(char current){
		return 	ops.contains(current+"");
		}
	TokenType(char c) {
		this.opChar = c;
	}

	TokenType() {
	}

	public char getC() {
		return opChar;
	}

	public void setC(char c) {
		this.opChar = c;
	}
}