package regex;

public class InterpreterVistor {
	Parser parser;
	public InterpreterVistor(Parser parser){
		this.parser=parser;
	}
	int eval() throws Exception{
		return parser.parse().visit();
	}
}
