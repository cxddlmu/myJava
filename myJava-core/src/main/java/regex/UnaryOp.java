package regex;

public class UnaryOp extends AST {
	Token op;
	AST expr;

	public UnaryOp(Token op, AST expr) {
		this.op = op;
		this.expr = expr;
	}

	@Override
	public int visit() {
		if (op.type == TokenType.PLUS) {
			return this.expr.visit();
		}

		return 0 - this.expr.visit();
	}

	@Override
	public String toLispStyle() {

		return "(" + op.value + " " + expr.toLispStyle() + ")";
	}

	@Override
	public String toBackStyle() {

		return expr.toBackStyle() + op.value;
	}

	public String toString() {
		return op.value + expr.toString();
	}
}
