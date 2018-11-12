package analizadorLexico;

/**
 * @author fabricio
 * @Title: Token
 * @Description: description
 *
 * Changes History
 */
public enum TipoToken {
    IDENTIFICADOR("Identificador"), NUMERO("Numero Entero"), ESCRIBIR("ESCRIBIR", "ESCRIBIR"), FIN("FIN", "FIN"),
    REPETIR("REPETIR", "REPETIR"), INICIAR("INICIAR", "INICIAR"), SI("SI", "SI"), VERDADERO("VERDADERO", "VERDADERO"),
    FALSO("FALSO", "FALSO"), ENTONCES("ENTONCES", "ENTONCES"), LITERAL("Literal"), PUNTO("Punto", "."), PUNTO_Y_COMA("Punto y coma", ";"),
    COMA("Coma", ","), DOS_PUNTOS("Dos puntos", ":"), COMENTARIO("Comentario de linea"),
    SUMA("Suma", "+"), RESTA("Resta", "-"), MULTIPLICACION("Multiplicacion", "*"), DIVISION("Division", "/"),
    ERROR("Error"), IGUAL("Igual", "="), PARENTESIS_DERECHO("Parentesis Derecho", ")"), PARENTESIS_IZQUIERDO("Parentesis Izquierdo", "("),
    CORCHETE_DERECHO("Corchete Derecho", "]"), CORCHETE_IZQUIERDO("Corchete Izquierdo", "["),
    LLAVE_DERECHA("Llave derecha", "}"), LLAVE_IZQUIERDA("Llave Izquierda", "{");

    private String nombreToken = null;
    private String lexema = null;

    private TipoToken(String nombreToken) {
        this.nombreToken = nombreToken;
    }

    private TipoToken(String nombreToken, String lexema) {
        this.nombreToken = nombreToken;
        this.lexema = lexema;
    }

    public String getNombreToken() {
        return this.nombreToken;
    }

    public String getLexema() {
        return this.lexema;
    }

}
