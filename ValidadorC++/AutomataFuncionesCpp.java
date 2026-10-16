import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AutomataFuncionesCpp {


    /* Definición de tipos de token y estructura de datos.
       Creado por: Leonardo Damian Hernandez Hernandez */
    enum TipoToken {
        TIPO_DATO, IDENTIFICADOR, PAREN_IZQ, PAREN_DER, LLAVE_IZQ, LLAVE_DER, COMA, DESCONOCIDO
    }

    static class Token {
        TipoToken tipo;
        String valor;

        Token(TipoToken tipo, String valor) {
            this.tipo = tipo;
            this.valor = valor;
        }
    }

    // Leonardo Damian Hernandez Hernandez
    
    public static void main(String[] args) {
        String rutaArchivo = args.length > 0 ? args[0] : "codigo_prueba.txt"; 
        
        try {
            String codigo = leerArchivo(rutaArchivo);
            
            // Llamadas a los módulos de mis compañeros
            List<Token> tokens = analizarLexico(codigo);
            boolean esValida = analizarSintaxisAutomata(tokens);
            
            if (esValida) {
                System.out.println(" ÉXITO: El archivo contiene una estructura de función C++ VÁLIDA.");
            } else {
                System.out.println(" ERROR: La función C++ NO está escrita correctamente.");
            }
            
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
            System.out.println("Por favor asegúrate de tener un archivo llamado 'funcion_prueba.txt'.");
        }
    }

    private static String leerArchivo(String ruta) throws IOException {
        StringBuilder contenido = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                contenido.append(linea).append("\n");
            }
        }
        return contenido.toString();
    }
    
//Diana Nicole
private static List<Token> analizarLexico(String codigo) {
        List<Token> tokens = new ArrayList<>();
        
        String regexTipo = "\\b(int|void|double|float|char|string|bool)\\b";
        String regexId = "\\b[a-zA-Z_][a-zA-Z0-9_]*\\b";
        
        String regexGeneral = String.format("(?<TIPO>%s)|(?<ID>%s)|(?<PARENIZQ>\\()|(?<PARENDER>\\))|(?<LLAVEIZQ>\\{)|(?<LLAVEDER>\\})|(?<COMA>,)|(?<ESPACIOS>\\s+)|(?<DESCONOCIDO>.)", regexTipo, regexId);
        
        Pattern pattern = Pattern.compile(regexGeneral);
        Matcher matcher = pattern.matcher(codigo);
        
        while (matcher.find()) {
            if (matcher.group("ESPACIOS") != null) continue;
            if (matcher.group("TIPO") != null) tokens.add(new Token(TipoToken.TIPO_DATO, matcher.group("TIPO")));
            else if (matcher.group("ID") != null) tokens.add(new Token(TipoToken.IDENTIFICADOR, matcher.group("ID")));
            else if (matcher.group("PARENIZQ") != null) tokens.add(new Token(TipoToken.PAREN_IZQ, "("));
            else if (matcher.group("PARENDER") != null) tokens.add(new Token(TipoToken.PAREN_DER, ")"));
            else if (matcher.group("LLAVEIZQ") != null) tokens.add(new Token(TipoToken.LLAVE_IZQ, "{"));
            else if (matcher.group("LLAVEDER") != null) tokens.add(new Token(TipoToken.LLAVE_DER, "}"));
            else if (matcher.group("COMA") != null) tokens.add(new Token(TipoToken.COMA, ","));
            else tokens.add(new Token(TipoToken.DESCONOCIDO, matcher.group("DESCONOCIDO")));
        }
        return tokens;
    }
// Yurem Jesus Aco Luna
    private static boolean analizarSintaxisAutomata(List<Token> tokens) {
        int estado = 0;
        int conteoLlaves = 0; 
        
        for (Token t : tokens) {
            switch (estado) {
                case 0:
                    if (t.tipo == TipoToken.TIPO_DATO) estado = 1;
                    else return false;
                    break;
                case 1:
                    if (t.tipo == TipoToken.IDENTIFICADOR) estado = 2;
                    else return false;
                    break;
                case 2:
                    if (t.tipo == TipoToken.PAREN_IZQ) estado = 3;
                    else return false;
                    break;
                case 3:
                    if (t.tipo == TipoToken.TIPO_DATO) estado = 4;
                    else if (t.tipo == TipoToken.PAREN_DER) estado = 6;
                    else return false;
                    break;
                case 4:
                    if (t.tipo == TipoToken.IDENTIFICADOR) estado = 5;
                    else return false;
                    break;
                case 5:
                    if (t.tipo == TipoToken.COMA) estado = 3;
                    else if (t.tipo == TipoToken.PAREN_DER) estado = 6;
                    else return false;
                    break;
                case 6:
                    if (t.tipo == TipoToken.LLAVE_IZQ) {
                        estado = 7;
                        conteoLlaves++;
                    } else return false;
                    break;
                case 7:
                    if (t.tipo == TipoToken.LLAVE_IZQ) conteoLlaves++;
                    else if (t.tipo == TipoToken.LLAVE_DER) {
                        conteoLlaves--;
                        if (conteoLlaves == 0) estado = 8;
                    }
                    break;
                case 8:
                    return false; 
            }
        }
        
        return estado == 8; 
    }
    
}

