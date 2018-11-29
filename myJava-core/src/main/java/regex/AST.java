package regex;

public abstract class AST {
	public abstract int visit();

	public abstract String toLispStyle();

	public abstract String toBackStyle();
}
