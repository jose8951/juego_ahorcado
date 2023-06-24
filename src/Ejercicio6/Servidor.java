/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ejercicio6;

import java.io.*;
import java.net.*;
import java.util.*;


/**
 *
 * @author Usuario
 */
public class Servidor extends Thread {

    private Socket socketCliente;

    // Flujo de salida a través del cual enviaremos información al proceso cliente 
    // conectado a través del socket
    private DataOutputStream flujoEscrituraCliente;

    // Flujo de entrada a través del cual recibiremos información desde el proceso cliente
    private DataInputStream flujoEntradaCliente;

    private int numCliente;
    
    private String[]posiblesCadenas = {"Futbol","Mesa","Invitacion","Clasificacion","Esternocleidomastoideo"};
    
    private String incognita;
    private String progreso;
    private int numFallos;
    private boolean acertado;

    public Servidor(Socket socketCliente, int numCliente) throws IOException {
        this.socketCliente = socketCliente;
        // Flujo de salida a través del cual enviaremos información al proceso cliente 
        // conectado a través del socket
        this.flujoEscrituraCliente = new DataOutputStream(this.socketCliente.getOutputStream());

        // Flujo de entrada a través del cual recibiremos información desde el proceso cliente
        this.flujoEntradaCliente = new DataInputStream(this.socketCliente.getInputStream());

        this.numCliente = numCliente;
        
        Random rd = new Random();
        int n = rd.nextInt(5);
        incognita = posiblesCadenas[n];
        progreso=""+incognita.charAt(0);
        for(int i=1;i<incognita.length();i++){
            progreso=progreso+"-";
        }
        System.out.println("--------->"+incognita);
        System.out.println("--------->"+progreso);
        acertado=false;
        numFallos=0;
    }

    @Override
    public void run() {

        String resultado, peticionCliente = null,respuesta;

        System.out.printf("Iniciado juego en el hilo del servidor %d.\n", this.numCliente);

        try {
            while(numFallos<5 && acertado == false){
                peticionCliente = flujoEntradaCliente.readUTF();
                if (!peticionCliente.isEmpty()) {
                    System.out.println("Recicienbo del CLIENTE: \n\t" + peticionCliente);
                    respuesta=calcularRespuesta(peticionCliente);
                    flujoEscrituraCliente.writeUTF(respuesta);
                }
            }
            // Cerramos la comunicación con el cliente
            flujoEscrituraCliente.close();
            flujoEntradaCliente.close();
            this.socketCliente.close();

        } catch (SocketException ex) {
            System.out.printf("Error de socket: %s\n", ex.getMessage());
        } catch (IOException ex) {
            System.out.printf("Error de E/S: %s\n", ex.getMessage());
        }

        System.out.printf(
                "Hilo servidor %d: Fin de la conexión con el cliente.\n", this.numCliente);

    }

    private String calcularRespuesta(String peticionCliente) {
        char letra = peticionCliente.charAt(0);
        boolean encontrado=false;
        String respuesta="";
        for(int i=0; i<this.incognita.length();i++){
            if(incognita.charAt(i)==letra){
                progreso = progreso.substring(0,i)+letra+progreso.substring(i+1);
                encontrado = true;
            }
        }
        if(encontrado){
            if(incognita.equals(progreso)==true){
                respuesta = "Has acertado la palabra. Fin del programa.- "+progreso;
                acertado=true;
            }else{
                respuesta = "Letra correcta.- "+progreso;
            }
        }else{
            numFallos++;
            if(numFallos<5){
                respuesta = "Letra incorrecta.- "+progreso;
            }else{
                respuesta = "Game over.-";
            }
        }
        return respuesta;
    }
}
