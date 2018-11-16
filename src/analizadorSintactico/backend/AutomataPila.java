package analizadorSintactico.backend;

import analizadorLexico.TipoToken;
import analizadorLexico.Token;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author fabricio
 */
public class AutomataPila {

    private Stack<Token> pila = null;
    private Stack<TipoToken> pilaLexemas = null;
    private Stack<TipoToken> pilaSistema = null;
    private Stack<TipoToken> pilaAuxiliar = null;
    private List<Token> errores = null;
    private int counterSystem = 0;
    private int counterSystemAux = 0;
    private int counterTemporalSystem = 0;
    private int counterTemporalSystemAux = 0;
    private int counterOptions = 0;
    private boolean subproceso = false;
    public static final TipoToken[] escribir1 = {TipoToken.FIN, TipoToken.LITERAL, TipoToken.ESCRIBIR};
    public static final TipoToken[] escribir2 = {TipoToken.FIN, TipoToken.NUMERO, TipoToken.ESCRIBIR};
    public static final TipoToken[] escribir3 = {TipoToken.FIN, TipoToken.IDENTIFICADOR, TipoToken.ESCRIBIR};
    public static final TipoToken[] repetir1 = {TipoToken.FIN, TipoToken.INICIAR, TipoToken.NUMERO, TipoToken.REPETIR};
    public static final TipoToken[] repetir2 = {TipoToken.FIN, TipoToken.INICIAR, TipoToken.IDENTIFICADOR, TipoToken.REPETIR};
    public static final TipoToken[] condicional1 = {TipoToken.FIN, TipoToken.ENTONCES, TipoToken.VERDADERO, TipoToken.SI};
    public static final TipoToken[] condicional2 = {TipoToken.FIN, TipoToken.ENTONCES, TipoToken.FALSO, TipoToken.SI};
    private List<Token> tokens = null;
    private List<ErrorSintactico> errors = null;
    public static final NoTerminal NO_TERMINAL_INICIAL = NoTerminal.S;
    public static final TipoToken TIPO_TOKEN_FINAL = TipoToken.FIN;
    private NoTerminal noTerminal = NO_TERMINAL_INICIAL;
    private int posicion = 0;
    private int option = 0;

    public AutomataPila(List tokens) {
        this.pila = new Stack<Token>();
        this.pilaLexemas = new Stack<>();
        this.tokens = tokens;
        this.errors = new LinkedList<>();
        this.pilaSistema = new Stack<>();
        this.pilaAuxiliar = new Stack<>();
        this.errores = new LinkedList<>();
    }

    /**
     * Metodo encargado de iniciar el automata, asi tambien se encarga de
     * manejar los posibles errores que se encuentre con las cadenas de entrada
     */
    public void iniciarAPD() {
        try {
            noTerminal = NO_TERMINAL_INICIAL;
            if (counterSystem < tokens.size()) {
                obtenerPila();
                counterTemporalSystem = counterSystem;
                iniciarProceso();
            } else {
                System.out.println("##Se ha terminado la lectura##");
                for (Token err : errores) {
                    System.out.println("Error: " + err.getTipoToken());
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
            pilaSistema.clear();
            counterOptions++;
            iniciarAPD();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            iniciarAPD();
        }
    }

    /**
     * Metodo principal el cual va cambiando de "No Terminal" dependiendo de la
     * cadena de entrada, el cual es capaz de detectar si una cadena no es
     * correcta, no coincide o no esta completa
     */
    private void iniciarProceso() throws Exception {
        switch (noTerminal) {
            case S:
                Token token = tokens.get(counterSystem);
                option = 1;
                switch (token.getTipoToken()) {
                    case ESCRIBIR:
                        noTerminal = NoTerminal.S1;
                        break;
                    case REPETIR:
                        noTerminal = NoTerminal.S2;
                        break;
                    case SI:
                        noTerminal = NoTerminal.S3;
                        break;
                    case IDENTIFICADOR:
                        noTerminal = NoTerminal.S5;
                        break;
                    default:
                        noTerminal = NoTerminal.ERROR;
                        break;
                }
                break;
            case S1:
                terminal();
                option = 1;
                noTerminal = NoTerminal.S1P;
                break;
            case S1P:
                noTerminal = NoTerminal.S1BP;
                iniciarProceso();
                option = 2;
                terminal();
                break;
            case S1BP:
                option = 0;
                terminal();
                break;
            case ERROR:
                error();
                throw new Exception("Aqui hay un error");
            case S2:
                terminal();
                option = 1;
                noTerminal = NoTerminal.S2P;
                break;
            case S2P:
                noTerminal = NoTerminal.S2BP;
                iniciarProceso();
                noTerminal = NoTerminal.S2TP;
                break;
            case S2BP:
                option = 0;
                terminal();
                break;
            case S2TP:
                terminal();
                option = 1;
                noTerminal = NoTerminal.S2CP;
                break;
            case S2CP:
                
            case S2QP:
            
            case S3:
                terminal();
                option = 1;
                noTerminal = NoTerminal.S3P;
                break;
            case S3P:
                option = 0;
                noTerminal = NoTerminal.S3BP;
                iniciarProceso();
                terminal();
                option = 1;
                noTerminal = NoTerminal.S3TP;
                break;
            case S3BP:
                option = 0;
                terminal();
                break;
            case S3TP:
                Token tok = tokens.get(counterTemporalSystem);
                if (tok.getTipoToken() == TipoToken.ESCRIBIR) {
                    counterSystem = counterTemporalSystem;
                    subproceso = true;
                    iniciarAPD();
                }
                pilaSistema.push(TIPO_TOKEN_FINAL);
                option = 2;
                terminal();
                subproceso = false;
                break;
            case S4:
            case S5:
        }
        if (option == 1) {
            iniciarProceso();
        }
        if (option == 2 || option == 3) {
            if (option == 2) {
                System.out.println("########Cadena correcta########");
                counterOptions = 0;
            } else if (option == 3) {
                System.out.println("######Cadena no correcta######");
            }
            counterSystem = counterTemporalSystem;
            if (!subproceso) {
                if (counterSystem >= tokens.size()) {
                    System.out.println("##Se ha terminado la lectura##");
                    for (Token err : errores) {
                        System.out.println("Error: " + err.getTipoToken());
                    }
                } else {
                    iniciarAPD();
                }
            }
        }
    }

    private void obtenerPila() throws Exception {
        Token token = tokens.get(counterSystem);
        switch (token.getTipoToken()) {
            case ESCRIBIR:
                switch (counterOptions) {
                    case 0:
                        crearPila(escribir1);
                        break;
                    case 1:
                        crearPila(escribir2);
                        break;
                    case 2:
                        crearPila(escribir3);
                        break;
                    default:
                        counterSystem++;
                        counterOptions = 0;
                        errores.add(token);
                        throw new Exception("Se encontro un nuevo error");
                }
                break;
            case REPETIR:
                switch (counterOptions) {
                    case 0:
                        crearPila(repetir1);
                        break;
                    case 1:
                        crearPila(repetir2);
                        break;
                    default:
                        break;

                }
                break;
            case SI:
                switch (counterOptions) {
                    case 0:
                        crearPila(condicional1);
                        break;
                    case 1:
                        crearPila(condicional2);
                        break;
                    default:
                        counterSystem++;
                        counterOptions = 0;
                        errores.add(token);
                        throw new Exception("Se encontro un nuevo error");
                }
                break;
//            case IDENTIFICADOR:
//                break;
            default:
                counterSystem++;
                errores.add(token);
                throw new Exception("Se encontro un nuevo error!!!");
        }
    }

    private void crearPila(TipoToken[] tt) {
        pilaSistema.clear();
        for (int i = 0; i < tt.length; i++) {
            System.out.println(pilaSistema.push(tt[i]));
        }
    }

    private void terminal() throws ArrayIndexOutOfBoundsException, Exception {
        if (counterTemporalSystem < tokens.size()) {
            Token token = tokens.get(counterTemporalSystem);
            System.out.println(token.getTipoToken());
            if (pilaSistema.peek() == token.getTipoToken()) {
                pilaSistema.pop();
                counterTemporalSystem++;
            } else {
                counterTemporalSystem++;
                throw new ArrayIndexOutOfBoundsException("/////Coincidencia no correcta\\\\\\\\\\");
            }
        } else {
            errores.add(tokens.get(counterSystem));
            counterSystem++;
            throw new Exception("////Nuevo error\\\\\\\\");
        }
    }

    private void error() {

    }

    //Retorna verdadero si el parametro coincide con alguno de los TipoToken definidos como Iniciales
    private boolean esTipoTokenInicial(TipoToken tt) {
        return tt == TipoToken.ESCRIBIR;
    }

    //Retorna verdadero si el parametro coincide con alguno de los TipoToken definidos como Finales
    private boolean esTipoTokenFinal(TipoToken tt) {
        return tt == TipoToken.FIN;
    }

    private enum NoTerminal {
        S, S1, S1P, S1BP, S2, S2P, S2BP, S2TP, S2CP, S2QP, S3, S3P, S3BP, S3TP, S4, S5, M, O, ERROR
    }

    private class ErrorSintactico {

        private List<Token> tokensError;

        public ErrorSintactico(List<Token> tokensError) {
            this.tokensError = tokensError;
        }

        public List<Token> getTokensError() {
            return tokensError;
        }

        public void setTokensError(List<Token> tokensError) {
            this.tokensError = tokensError;
        }
    }

}
