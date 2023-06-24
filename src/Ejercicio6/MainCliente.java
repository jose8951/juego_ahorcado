package Ejercicio6;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class MainCliente extends Thread {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("PROGRAMA CLIENTE INICIADO ...");
        Socket cliente = null;
        //CREO FLUJO DE ENTRADA AL SERVIDOR
        DataInputStream flujoEntrada = null;
        //CREO FLUJO DE SALIDA AL SERVIDOR
        DataOutputStream flujoSalida = null;
        String respuesta = "";
        String letra;

        try {
            cliente = new Socket("localhost", 6000);

            flujoSalida = new DataOutputStream(cliente.getOutputStream());
            flujoEntrada = new DataInputStream(cliente.getInputStream());

            while (respuesta.contains("Game over") == false && respuesta.contains("Has acertado la palabra") == false) {
                //ENVÍO UN SALUDO AL SERVIDOR
                System.out.print("Dame una letra: ");
                letra = sc.nextLine();
                flujoSalida.writeUTF(letra);
                respuesta = flujoEntrada.readUTF();
                //EL SERVIDOR ME ENVÍA UN MENSAJE
                System.out.println("Recibiendo del SERVIDOR: \n\t" + respuesta);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        //CERRAR SREAMS Y SOCKETS
        try {
            flujoEntrada.close();
            flujoSalida.close();
            cliente.close();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}
