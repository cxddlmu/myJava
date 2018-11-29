package regex;

public class Parser {
    Lexer lexer;
    Token current_token;

    public Parser(Lexer lexer) throws Exception {
        this.lexer = lexer;
        this.current_token = lexer.get_next_token();
    }

    private void error() throws Exception {
        throw new Exception("��ʽ����ı��ʽ");
    }

    private AST term() throws Exception {
        AST node = factor();
        while (current_token.type == TokenType.DIV
                || current_token.type == TokenType.MUL) {
            Token token = current_token;
            if (current_token.type == TokenType.MUL) {
                eat(TokenType.MUL);

            } else if (current_token.type == TokenType.DIV) {
                eat(TokenType.DIV);

            }
            node = new BinAST(node, token, factor());
        }

        return node;

    }

    private AST factor() throws Exception {
        if (current_token.type == TokenType.PLUS) {
            Token token = current_token;
            eat(TokenType.PLUS);
            return new UnaryOp(token, factor());
        } else if (current_token.type == TokenType.MINUS) {
            Token token = current_token;
            eat(TokenType.MINUS);
            return new UnaryOp(token, factor());
        } else if (current_token.type == TokenType.INTEGER) {
            Token token = current_token;
            eat(TokenType.INTEGER);
            return new NumAST(token);
        } else if (current_token.type == TokenType.LPAREN) {
            eat(TokenType.LPAREN);
            AST node = expr();
            eat(TokenType.RPAREN);
            return node;
        }
        error();
        return null;
    }

    private AST expr() throws Exception {

        AST node = term();
        while (current_token.type == TokenType.PLUS
                || current_token.type == TokenType.MINUS) {
            Token op = current_token;
            if (current_token.type == TokenType.PLUS) {
                eat(TokenType.PLUS);

            } else if (current_token.type == TokenType.MINUS) {
                eat(TokenType.MINUS);

            }
            node = new BinAST(node, op, term());
        }

        return node;

    }

    private void eat(TokenType type) throws Exception {
        if (this.current_token.type == type) {
            current_token = lexer.get_next_token();
            return;
        }
        error();
    }

    public AST parse() throws Exception {
        AST expr = expr();
        if (current_token.type != TokenType.EOF) {
            error();
        }
        return expr;
    }
}
