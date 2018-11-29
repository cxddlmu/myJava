package regex;

public class BinAST extends AST {
	AST left;
	Token op;
	AST right;

	public BinAST(AST left, Token op, AST right) {
		this.left = left;
		this.op = op;
		this.right = right;
	}

	public int visit() {
		if (op.type == TokenType.PLUS) {
			return left.visit() + right.visit();
		} else if (op.type == TokenType.MINUS) {
			return left.visit() - right.visit();
		} else if (op.type == TokenType.MUL) {
			return left.visit() * right.visit();
		} else if (op.type == TokenType.DIV) {
			return left.visit() / right.visit();
		}

		return 0;
	}

	public String toString() {
		return left.toString() + op.value + right.toString();
	}

	public String toLispStyle() {
		return "(" + op.value + " " + left.toLispStyle() + " "
				+ right.toLispStyle() + ")";
	}

	public String toBackStyle() {
		return left.toBackStyle() + " " + right.toBackStyle() + " " + op.value;
	}
}
