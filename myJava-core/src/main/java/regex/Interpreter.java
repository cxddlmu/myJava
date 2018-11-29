package regex;

public class Interpreter {
	Lexer lexer;
	Token current_token;

	public Interpreter(Lexer lexer) throws Exception {
		this.lexer = lexer;
		this.current_token = lexer.get_next_token();
	}

	public void error() {
		try {
			throw new Exception("��ʽ����ı��ʽ");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int term() throws Exception {
		int result = factor();
		while (current_token.type == TokenType.DIV || current_token.type == TokenType.MUL) {
			if (current_token.type == TokenType.MUL) {
				eat(TokenType.MUL);
				result *= factor();
			} else if (current_token.type == TokenType.DIV) {
				eat(TokenType.DIV);
				result /= factor();
			}

		}

		return result;

	}

	public int factor() throws Exception {
		if (current_token.type == TokenType.INTEGER) {
			int value = Integer.parseInt(current_token.value);
			eat(TokenType.INTEGER);
			return value;
		} else if (current_token.type == TokenType.LPAREN) {
			eat(TokenType.LPAREN);
			int result = expr();
			eat(TokenType.RPAREN);
			return result;
		}
		error();
		return 0;

	}

	public int expr() throws Exception {

		int result = term();
		while (current_token.type == TokenType.PLUS
				|| current_token.type == TokenType.MINUS) {
			if (current_token.type == TokenType.PLUS) {
				eat(TokenType.PLUS);
				result += term();
			} else if (current_token.type == TokenType.MINUS) {
				eat(TokenType.MINUS);
				result -= term();
			}

		}

		return result;

	}

	public void eat(TokenType type) throws Exception {
		if (this.current_token.type == type) {
			current_token = lexer.get_next_token();
			return;
		}
		error();
	}
}