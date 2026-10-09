

import java.util.Scanner;

public class validacionFuncion {

    public static boolean validarFuncion(String cadena) {
        int estado = 0;
        int i = 0;

        while (i < cadena.length()) {
            char c = cadena.charAt(i);

            switch (estado) {
                case 0:
                    if (c == 'v') estado = 1;
                    else return false;
                    break;

                case 1:
                    if (c == 'o') estado = 2;
                    else return false;
                    break;

                case 2:
                    if (c == 'i') estado = 3;
                    else return false;
                    break;

                case 3:
                    if (c == 'd') estado = 4;
                    else return false;
                    break;

                case 4:
                    if (c == ' ') estado = 5;
                    else return false;
                    break;

                case 5:
                    // Primera letra del nombre
                    if ((c >= 'a' && c <= 'z') ||
                        (c >= 'A' && c <= 'Z') ||
                        c == '_')
                        estado = 6;
                    else
                        return false;
                    break;

                case 6:
                    // Resto del nombre de la funcion
                    if ((c >= 'a' && c <= 'z') ||
                        (c >= 'A' && c <= 'Z') ||
                        (c >= '0' && c <= '9') ||
                        c == '_') {
                        estado = 6;
                    }
                    else if (c == '(') {
                        estado = 7;
                    }
                    else
                        return false;
                    break;

                case 7:
                    if (c == ')') estado = 8;
                    else return false;
                    break;

                case 8:
                    return false;
            }

            i++;
        }

        return estado == 8;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String cadena;

        System.out.print("Escribe una funcion: ");
        cadena = scanner.nextLine();

        if (validarFuncion(cadena))
            System.out.println("Funcion correcta");
        else
            System.out.println("Funcion incorrecta");

        scanner.close();
    }
}
