package regex;

public class NumAST extends AST {

	Token num;

	public NumAST(Token num) {
		this.num = num;
	}

	public int visit() {
		return Integer.parseInt(num.value);
	}

	public String toString() {
		return num.value;
	}

	public String toLispStyle() {
		return toString();
	}

	public String toBackStyle() {
		return toString();
	}
}
