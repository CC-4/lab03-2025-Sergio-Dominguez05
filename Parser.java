/*
    Laboratorio No. 3 - Recursive Descent Parsing
    CC4 - Compiladores

    Clase que representa el parser

    Actualizado: agosto de 2021, Luis Cu
*/

import java.util.LinkedList;
import java.util.Stack;

public class Parser {

    // Puntero next que apunta al siguiente token
    private int next;
    // Stacks para evaluar en el momento
    private Stack<Double> operandos;
    private Stack<Token> operadores;
    // LinkedList de tokens
    private LinkedList<Token> tokens;

    // Funcion que manda a llamar main para parsear la expresion
    public boolean parse(LinkedList<Token> tokens) {
        this.tokens = tokens;
        this.next = 0;
        this.operandos = new Stack<Double>();
        this.operadores = new Stack<Token>();

        // Recursive Descent Parser
        // Imprime si el input fue aceptado
        System.out.println("Aceptada? " + S());

        // Shunting Yard Algorithm
        // Imprime el resultado de operar el input
        // System.out.println("Resultado: " + this.operandos.peek());

        // Verifica si terminamos de consumir el input
        if(this.next != this.tokens.size()) {
            return false;
        }
        return true;
    }

    // Verifica que el id sea igual que el id del token al que apunta next
    // Si si avanza el puntero es decir lo consume.
    private boolean term(int id) {
        if(this.next < this.tokens.size() && this.tokens.get(this.next).equals(id)) {
            
            // Codigo para el Shunting Yard Algorithm
            /*
            if (id == Token.NUMBER) {
				// Encontramos un numero
				// Debemos guardarlo en el stack de operandos
				operandos.push( this.tokens.get(this.next).getVal() );

			} else if (id == Token.SEMI) {
				// Encontramos un punto y coma
				// Debemos operar todo lo que quedo pendiente
				while (!this.operadores.empty()) {
					popOp();
				}
				
			} else {
				// Encontramos algun otro token, es decir un operador
				// Lo guardamos en el stack de operadores
				// Que pushOp haga el trabajo, no quiero hacerlo yo aqui
				pushOp( this.tokens.get(this.next) );
			}
			*/

            this.next++;
            return true;
        }
        return false;
    }

    // Funcion que verifica la precedencia de un operador
    private int pre(Token op) {
        /* TODO: Su codigo aqui */

        /* El codigo de esta seccion se explicara en clase */

        switch(op.getId()) {
            case Token.LPAREN:
                return 0;
        	case Token.PLUS:
        		return 1;
            case Token.MINUS:
                return 1;
        	case Token.MULT:
        		return 2;
            case Token.DIV:
        		return 2;
            case Token.MOD:
        		return 2;
            case Token.EXP:
        		return 3;
            case Token.UNARY:
        		return 4;
        	default:
        		return -1;
        }
    }

    private void popOp() {
        Token op = this.operadores.pop();
        int id = op.getId();
        /* TODO: Su codigo aqui */

        /* El codigo de esta seccion se explicara en clase */

        if (op.equals(Token.PLUS)) {
        	double a = this.operandos.pop();
        	double b = this.operandos.pop();
        	// print para debug, quitarlo al terminar
        	this.operandos.push(a + b);
        } else if (op.equals(Token.MULT)) {
        	double a = this.operandos.pop();
        	double b = this.operandos.pop();
        	// print para debug, quitarlo al terminar
        	this.operandos.push(a * b);
        }
        else if (op.equals(Token.MINUS)) {
        	double a = this.operandos.pop();
        	double b = this.operandos.pop();
        	// print para debug, quitarlo al terminar
        	this.operandos.push(a - b);
        }
        else if (op.equals(Token.DIV)) {
        	double a = this.operandos.pop();
        	double b = this.operandos.pop();
        	// print para debug, quitarlo al terminar
        	this.operandos.push(a / b);
        }
        else if (op.equals(Token.MOD)) {
        	double a = this.operandos.pop();
        	double b = this.operandos.pop();
        	// print para debug, quitarlo al terminar
        	this.operandos.push(a % b);
        }
        else if (op.equals(Token.EXP)) {
        	double a = this.operandos.pop();
        	double b = this.operandos.pop();
        	// print para debug, quitarlo al terminar
        	this.operandos.push(Math.pow(b, a));
        }
        else if (op.equals(Token.UNARY)) {
        	double a = this.operandos.pop();
        	// print para debug, quitarlo al terminar
        	this.operandos.push(-a);
        }
    }

    private void pushOp(Token op) {
        /* TODO: Su codigo aqui */
        int id = op.getId();

        if (id == Token.LPAREN){
            this.operadores.push(op);
        }
        if (id == Token.RPAREN){
            while(this.operadores.peek().getId() != Token.LPAREN){
                popOp();
            }
            if (this.operadores.peek().getId() != Token.LPAREN){
                this.operadores.pop();
            }
            return;
        }
        Token t = op;
        if (id == Token.MINUS){
            t = new Token(Token.UNARY);
        }
        int p = pre(t);
        while (this.operadores != null){
            Token top = this.operadores.peek();
            if (top.getId() == Token.LPAREN){
                break;
            }
            int ptop = pre(top);
            boolean quitar = (ptop > p) || (ptop == p && rAsociative(t));
            if (quitar == true){
                popOp();
            } else {
                break;
            }
        }
        this.operadores.push(t);
        /* Casi todo el codigo para esta seccion se vera en clase */
    	
    	// Si no hay operandos automaticamente ingresamos op al stack

    	// Si si hay operandos:
    		// Obtenemos la precedencia de op
        	// Obtenemos la precedencia de quien ya estaba en el stack
        	// Comparamos las precedencias y decidimos si hay que operar
        	// Es posible que necesitemos un ciclo aqui, una vez tengamos varios niveles de precedencia
        	// Al terminar operaciones pendientes, guardamos op en stack

    }
    private int idSig(){
        return this.tokens.get(this.next).getId();
    }
    private boolean S() {
        return E() && term(Token.SEMI);
    }

    private boolean E() {
        if (!B()){
            return false;
        }else {
            return A();
        }
    }
    private boolean A(){
        if (idSig() == Token.PLUS || idSig() == Token.MINUS) {
            int op = idSig();
            if (!term(op)) return false;
            if (!B()) return false;
            return A();
        }
        return true;
    }
    private boolean B() {
        if (!D()) return false;
        return C();
    }
    private boolean C() {
        if (idSig() == Token.MULT || idSig() == Token.DIV || idSig() == Token.MOD) {
            int op = idSig();
            if (!term(op)) return false;
            if (!D()) return false;
            return C();
        }
        return true;
    }
    private boolean D() {
        if (!G()) return false;
        return F();
    }
    private boolean F() {
        if (idSig() == Token.EXP) {
            if (!term(Token.EXP)) return false;
            if (!G()) return false;
            return F();
        }
        return true; 
    }
    private boolean G() {
        if (idSig() == Token.UNARY) {
            if (!term(Token.UNARY)) return false;
            return H();
        }
        return I();
    }
    private boolean H() {
        return I();
    }
    private boolean I() {
        if (idSig() == Token.LPAREN) {
            return term(Token.LPAREN) && E() && term(Token.RPAREN);
        } else if (idSig() == Token.NUMBER) {
            return term(Token.NUMBER);
        }
        return false;
    }

    private boolean rAsociative(Token op){
        int id = op.getId();
        return (id == Token.EXP) || (id == Token.UNARY);
    }
    /* TODO: sus otras funciones aqui */
}
