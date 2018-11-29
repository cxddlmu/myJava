package regex;

import org.junit.Test;

import java.util.Scanner;

import static org.junit.Assert.assertEquals;

public class TestInterpreter {
	@Test
	public void testUnaryOp() throws Exception{
		Lexer lexer = new Lexer("1---2");
		Parser parser = new Parser(lexer);

		int r = new InterpreterVistor(parser).eval();
		System.out.println("UnaryOp:"+r);
		assertEquals(r, -1);
	}
	@Test
	public void testTranslate() throws Exception {
//		Lexer lexer = new Lexer("(1*(2+1))");
		Lexer lexer = new Lexer("1--+2");
		Parser parser = new Parser(lexer);
		AST node = parser.parse();
		System.out.println("--------------------------");
		System.out.println(lexer.text);
		System.out.println(node.toBackStyle());
		System.out.println(node.toLispStyle());
		System.out.println("--------------------------");
	}

	@Test
	public void testVist() throws Exception {
		Lexer lexer = new Lexer("(5*(2+1))");
		Parser parser = new Parser(lexer);

		int r = new InterpreterVistor(parser).eval();
		System.out.println(r);
		assertEquals(r, 15);

	}

	@Test
	public void test() throws Exception {
		Lexer lexer = new Lexer("(1*(2+1))");
		int r = new Interpreter(lexer).expr();
		System.out.println(r);
		assertEquals(r, 3);

	}

	@Test
	public void test1() throws Exception {
		Lexer lexer = new Lexer("1*2+1*2*1+1-1");
		int r = new Interpreter(lexer).expr();
		System.out.println(r);
		assertEquals(r, 4);

	}
	//�﷨����
	@Test 
	public void testUnexcept(){
		Lexer lexer = new Lexer("1*2 1");
		Parser parser;
		try {
			parser = new Parser(lexer);
			parser.parse();
			assert(false);
		} catch (Exception e1) {
			assert(true);
		}
	
	}
	public static void main(String[] args) {
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);

		while (true) {
			System.out.println("������ʽ,�磺1+2");
			try {

				String text = sc.next();

				Lexer lexer = new Lexer(text);
				Interpreter interpreter = new Interpreter(lexer);
				int result = interpreter.expr();
				print(result);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	private static void print(int r) {
		System.out.println(r);

	}
}
