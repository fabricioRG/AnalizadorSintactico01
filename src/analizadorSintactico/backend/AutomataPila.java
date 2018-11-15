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
    private List<Token> tokens = null;
    private List<ErrorSintactico> errores = null;
    public final NoTerminal NO_TERMINAL_INICIAL = NoTerminal.S1;
    private NoTerminal noTerminal = NO_TERMINAL_INICIAL;
    private Token token = null;
    private int posicion = 0;
    private int posicionTemporal = 0;
    private int contador = 0;
    private int contadorTemporalPila = 0; //Contador utilizado para llevar la posicion en la lectura de los tokens de entrada
    private int contadorDePila = 0; //Contador utilizado para indicar la posicion del pila de entrada a evaluar
    private int contadorS = 0; //Contador utilizado por parte del terminal S
    private int posicionToken = 0;
    private int estado = 0;
    private boolean error = false; //Si existe algun errores cambiara a true
    private int option = 0;
    private boolean done = false;

    public AutomataPila(List tokens) {
        this.pila = new Stack<Token>();
        this.pilaLexemas = new Stack<>();
        this.tokens = tokens;
        this.errores = new LinkedList<>();
    }

    /**
     * Metodo encargado de iniciar el automata, asi tambien se encarga de manejar los posibles
     * errores que se encuentre con las cadenas de entrada
     */
    public void iniciarAPD() {
        try {
            posicionTemporal = posicion;
            noTerminal = NO_TERMINAL_INICIAL;
            this.error = false;
            iniciarProceso();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
            pilaLexemas.clear();
            this.contador++;
            iniciarAPD();
        }
    }

    /**
     * Metodo principal el cual va cambiando de "No Terminal" dependiendo de la cadena de entrada,
     * el cual es capaz de detectar si una cadena no es correcta, no coincide o no esta completa
     */
    private void iniciarProceso() {
        switch (noTerminal) {
            case S:
            case S1:
                terminal(TipoToken.ESCRIBIR);
                option = 1;
                noTerminal = NoTerminal.S1P;
                break;
            case S1P:
                noTerminal = NoTerminal.S1BP;
                option = 0;
                iniciarProceso();
                option = 1;
                noTerminalFinal();
                break;
            case S1BP:
                contadorS = contador;
                switch (contadorS) {
                    case 0:
                        terminal(TipoToken.LITERAL);
                        break;
                    case 1:
                        terminal(TipoToken.NUMERO);
                        break;
                    case 2:
                        terminal(TipoToken.IDENTIFICADOR);
                        break;
                    default:
                        contadorS = 0;
                        error = true;
                        noTerminal = NoTerminal.ERROR;
                        break;
                }
                break;
            case ERROR:
                error = true;
                break;
                case S2:
            case S2P:
            case S2BP:
            case S2TP:
            case S2CP:
            case S2QP:
            case S3:
            case S3P:
            case S3BP:
            case S3TP:
            case S4:
            case S5:
        }
        if (option == 1) {
            iniciarProceso();
        } else if (option == 2 || option == 3) {
            if (option == 2) {
                System.out.println("########Cadena correcta########");
            } else if (option == 3) {
                System.out.println("######Cadena no correcta######");
                agregarError(null);
            }
            if (contadorDePila >= tokens.size()) {
                System.out.println("##Se ha terminado la lectura##");
                for (ErrorSintactico error : errores) {
                    System.out.println("####################");
                    for (Token token1 : error.getTokensError()) {
                        System.out.println(token1.getTipoToken());
                    }
                }
            } else {
                iniciarAPD();
            }
        }
    }
    
    //Metodo utilizado unicamente si el TipoToken es final, el cual se encarga de verificar errores y otras procesos
    private void noTerminalFinal() {
        if (iniciarPila() && !error) {
            terminal(TipoToken.FIN);
            option = 2;
            desapilar();
            contador = 0;
        } else {
            option = 3;
        }
    }
    
    /**
     * Metodo que agrega en una pila "pila" los tokens de entrada, siempre y cuando estos cumplan con las
     * condiciones de tener un TipoToken inicial y uno final, de lo contrario se tomara como error y se retornara
     * como valor boolean "false", pero si no se encuentra ningun error entonces se retorna "true"
     * @return 
     */
    private boolean iniciarPila() {
        pila.clear();
        List<Token> error = new LinkedList<>();
        int contador = contadorDePila;
        if (contador < tokens.size()) { //Se verifica que el tamaño del contador sea menor a la longitud de la lista de entrada
            if (esTipoTokenInicial(tokens.get(contador).getTipoToken())) { //Si es un TipoToken definido como inicial se inicia el proceso
                System.out.println(pila.push(tokens.get(contador)).getTipoToken()); //Se agrega a la pila "pila" lo que se vaya leyendo
                while (!esTipoTokenFinal(tokens.get(contador).getTipoToken())) { //Reconocer tokens hasta que se encuentre un TipoToken final
                    contador++;
                    if((contador >= tokens.size())){
                        contadorTemporalPila = contador;
                        return false;
                    } else if (esTipoTokenInicial(tokens.get(contador).getTipoToken())){ //
                        contadorTemporalPila = contador -1;
                        return false;
                    }
                    System.out.println(pila.push(tokens.get(contador)).getTipoToken());
                }
                contadorTemporalPila = contador;
                return true;
            } else {
                while (!esTipoTokenInicial(tokens.get(contador).getTipoToken()) && !(contador >= tokens.size())) {
                    error.add(tokens.get(contador));
                    contador++;
                }
                ErrorSintactico es = new ErrorSintactico(error);
                contadorTemporalPila = contador - 1;
                agregarError(es);
                return false;
            }
        } else {
            return false;
        }
    }
    
    //Metodo cuya funcion es apilar en "pilaLexemas" el TipoToken de parametro y aumentar el contador "posicionTemporal"
    private void terminal(TipoToken tt) {
        System.out.println(pilaLexemas.push(tt));
    }

    /**
     * Metodo que compara dos pilas, la primera que contiene los tokens de entrada y la segunda que contiene
     * una pila de TipoTokens generados por el automata de pila
     * Puede contener errores, pues es posible que no conicidan las dos pilas, por lo que es atrapado en el metodo inicial
     * @throws ArrayIndexOutOfBoundsException 
     */
    private void desapilar() throws ArrayIndexOutOfBoundsException {
        int contInterno = posicionToken;
        while (!pilaLexemas.isEmpty()) {
            if (pila.peek().getTipoToken() == pilaLexemas.peek()) { //Compara los TipoToken de los Tokens
                System.out.println(pilaLexemas.pop());
                System.out.println(pila.pop().getTipoToken());
            } else {
                throw new ArrayIndexOutOfBoundsException("/////Coincidencia no correcta\\\\\\\\\\");
            }
        }
        posicionToken = contInterno;
        contadorDePila = contadorTemporalPila + 1;
        posicion = posicionTemporal;
    }

    /**
     * Metodo que recibe como parametro un objeto ErrorSintactico el cual se agrega a una lista de
     * ErrorSintactico para guardar los errores obtenidos en los procesos anteriores y de esta manera tener
     * una mas adecuada de mostrar los errores existentes
     * @param es 
     */
    private void agregarError(ErrorSintactico es) {
        if (es == null) { //Si el parametro es nulo se procede a crear una lista de errores
            List<Token> error = new LinkedList<>();
            for (Token token1 : pila) {
                error.add(token1);
            }
            ErrorSintactico eS = new ErrorSintactico(error); //Se crea un objeto ErrorSintactico que contendra estos errores
            this.errores.add(eS);
            contadorTemporalPila++;
        } else {
            this.errores.add(es); //Si no es nulo se agrega el parametro a la lista de errores glabal en el sistema
        }
        contadorDePila = contadorTemporalPila;
        pilaLexemas.clear();
        this.contador = 0;
        option = 3;
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
