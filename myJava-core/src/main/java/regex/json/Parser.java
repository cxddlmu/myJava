package regex.json;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

public class Parser {
	Lexer lexer;
	Token current_token;

	public Parser() throws Exception {
		Lexer lexer = new Lexer(
				"{  \"k\"  : [{\"1\":1},2,3],\"k1\":{\"tt\":1}  }");//test json
		this.lexer = lexer;
		this.current_token = lexer.get_next_token();
	}

	private void eat(JsonTokenType type) throws Exception {
		if (this.current_token.type == type) {
			current_token = lexer.get_next_token();
			return;
		}
		error();
	}

	private void error() throws Exception {
			throw new Exception("��ʽ����ı��ʽ");
	}

	private ArrayList<Object> getList() throws Exception {
		ArrayList<Object> list = new ArrayList<>();
		list.add(getJson());
		while (current_token.type == JsonTokenType.COMMA) {

			eat(JsonTokenType.COMMA);
			list.add(getJson());

		}

		return list;
	}

	//
	private Object getJson() throws Exception {
		/**
		 * JSON:{key:v}|number|array|string
		 * key:string|(canbeJOSN)
		 * v:JOSN
		 * number:(0..9)+
		 * string:"(c)+"
		 * array:[json(,josn)*]
		 * û��ʵ�ֲ���ֵ  Ӧ�úܼ�  ���Ӵʷ����� 
		 */
		
		while (current_token.type != JsonTokenType.EOF) {
			if (current_token.type == JsonTokenType.NUMBER) {

				int i = Integer.parseInt(current_token.value);
				eat(JsonTokenType.NUMBER);

				return i;
			}
			if (current_token.type == JsonTokenType.LEFTBRACKET) {

				eat(JsonTokenType.LEFTBRACKET);
				ArrayList<Object> list = getList();
				eat(JsonTokenType.RIGHTBRACKET);
				return list;
			}

			if (current_token.type == JsonTokenType.LEFTbrace) {
				eat(JsonTokenType.LEFTbrace);
				HashMap<Object, Object> jo = getKVmap();

				eat(JsonTokenType.RIGHTbrace);
				return jo;
			}
			if (current_token.type == JsonTokenType.STRING) {

				return current_token.value;
			}
		}
		return null;
	}
	
	private HashMap<Object, Object> getKVmap() throws Exception {
		HashMap<Object, Object> jo = new HashMap<>();
		Object k = getJson();
		eat(JsonTokenType.STRING);
		eat(JsonTokenType.COLON);
		Object v = getJson();
		jo.put(k, v);
		while (current_token.type == JsonTokenType.COMMA) {

			eat(JsonTokenType.COMMA);
			k = getJson();
			eat(JsonTokenType.STRING);//(canbeJOSN)
			eat(JsonTokenType.COLON);
			v = getJson();
			jo.put(k, v);

		}
		return jo;
	}
	public Object parser() throws Exception{
		Object json = getJson();
		if(current_token.type!=JsonTokenType.EOF){
			error();
		}
		return json;
	}
	@Test
	public void testJson() throws Exception {

		Parser p = new Parser();
		System.out.println(p.getJson());
	}
}
