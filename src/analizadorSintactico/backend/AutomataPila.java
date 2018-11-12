package analizadorSintactico.backend;

import analizadorLexico.TipoToken;
import analizadorLexico.Token;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author fabricio
 */
public class AutomataPila {

    private Stack<Token> pila = null;
    private Stack<TipoToken> pilaLexemas = null;
    private List<Token> tokens = null;
    public final NoTerminal NO_TERMINAL_INICIAL = NoTerminal.S;
    private NoTerminal noTerminal = NO_TERMINAL_INICIAL;
    private Token token = null;
    private int posicion = 0;
    private int posicionTemporal = 0;
    private int contador = 0;
    private int contadorPila = 0;
    private int contadorS = 0; //Contador utilizado por parte del terminal S
    private int posicionToken = 0;
    private int estado = 0;
    private boolean error = false;
    private int option = 0;
    private boolean done = false;

    public AutomataPila(List tokens) {
        this.pila = new Stack<Token>();
        this.pilaLexemas = new Stack<>();
        this.tokens = tokens;
        this.posicionToken = tokens.size();
    }

    public void iniciarAPD() {
//        if (APD1() == 2) {
//            System.out.println("Cadena valida!!!");
//        } else {
//            System.out.println("Cadena no valida!!!");
//        }
        try {
            posicionTemporal = posicion;
            noTerminal = NO_TERMINAL_INICIAL;
            this.error = false;
            iniciarPila();
            iniciarProceso();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
            pilaLexemas.clear();
            contador++;
            iniciarAPD();
        }
    }

    private void terminal(TipoToken tt) {
        System.out.println(pilaLexemas.push(tt));
        posicionTemporal++;
    }

    private void iniciarProceso() {
        switch (noTerminal) {
            case S:
                noTerminal = NoTerminal.ESCRIBIR;
                option = 0;
                iniciarProceso();
                option = 1;
                noTerminal = NoTerminal.SP;
                break;
            case ESCRIBIR:
                terminal(TipoToken.ESCRIBIR);
                break;
            case SP:
                noTerminal = NoTerminal.SBP;
                option = 0;
                iniciarProceso();
                option = 1;
                noTerminal = NoTerminal.FIN;
                break;
            case SBP:
                contadorS = contador;
                switch (contadorS) {
                    case 0:
                        noTerminal = NoTerminal.LITERAL;
                        break;
                    case 1:
                        noTerminal = NoTerminal.NUMERO;
                        break;
                    case 2:
                        noTerminal = NoTerminal.IDENTIFICADOR;
                        break;
                    default:
                        contadorS = 0;
                        noTerminal = NoTerminal.ERROR;
                        break;
                }
                iniciarProceso();
                break;
            case FIN:
                noTerminalFinal(TipoToken.FIN);
                break;
            case LITERAL:
                terminal(TipoToken.LITERAL);
                break;
            case NUMERO:
                terminal(TipoToken.NUMERO);
                break;
            case IDENTIFICADOR:
                terminal(TipoToken.IDENTIFICADOR);
                break;
            case ERROR:
                option = 0;
                error();
                break;
        }
        if (option == 1) {
            iniciarProceso();
        } else if (option == 2) {
            System.out.println("########Cadena correcta########");
            if (posicion >= tokens.size()) {
                System.out.println("##Se ha terminado la lectura##");
            } else {
                iniciarAPD();
            }
        }
    }

    private void desapilar() throws ArrayIndexOutOfBoundsException {
        int contInterno = posicionToken;
        while (!pilaLexemas.isEmpty()) {
            contInterno--;
            TipoToken tt = tokens.get(contInterno).getTipoToken();
            if (tt == pilaLexemas.peek()) {
                System.out.println(pilaLexemas.pop());
            } else {
                throw new ArrayIndexOutOfBoundsException("Error!!!!!!!!!!!");
            }
        }

        posicionToken = contInterno;
    }

    private void iniciarPila(){
        pila.clear();
        int contador = 0;
        if (esTipoTokenInicial(tokens.get(contador).getTipoToken())) {
            System.out.println(pila.push(tokens.get(contador)));
            while (!esTipoTokenFinal(tokens.get(contador).getTipoToken())) {                
                contador++;
                System.out.println(pila.push(tokens.get(contador)));
            }
        }
    }
    
    private void error() {
        pilaLexemas.clear();

    }

    private void noTerminalFinal(TipoToken tt) {
        terminal(tt);
        desapilar();
        posicion = posicionTemporal;
        option = 2;
        contador = 0;
    }

    private boolean esTipoTokenInicial(TipoToken tt) {
        return tt == TipoToken.ESCRIBIR;
    }

    private boolean esTipoTokenFinal(TipoToken tt) {
        return tt == TipoToken.FIN;
    }

    private enum NoTerminal {
        S, ESCRIBIR, SP, SBP, FIN, LITERAL, NUMERO, IDENTIFICADOR, ERROR
    }
}
