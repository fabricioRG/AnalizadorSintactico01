package analizadorLexico;

/**
 * @author fabricio
 * @Title: Token
 * @Description: description
 *
 * Changes History
 */
public enum Token {
    IDENTIFICADOR("Identificador"), NUMERO("Numero Entero"), ESCRIBIR("ESCRIBIR"), FIN("FIN"), 
    REPETIR("REPETIR"), INICIAR("INICIAR"), SI("SI"), VERDADERO("VERDADERO"),
    FALSO("FALSO"), ENTONCES("ENTONCES"), LITERAL("Literal"), COMENTARIO("Comentario de linea"), 
    SUMA("Suma"), RESTA("Resta"), MULTIPLICACION("Multiplicacion"), DIVISION("Division"), 
    ERROR("Error"), IGUAL("Igual"), PARENTESIS_DERECHO("Parentesis Derecho"), PARENTESIS_IZQUIERDO("Parentesis Izquierdo");
    
    private String nombreToken;
    
    private Token (String nombreToken){
            this.nombreToken = nombreToken;
    }
    
    public String getNombreToken(){
        return this.nombreToken;
    }
    
}
