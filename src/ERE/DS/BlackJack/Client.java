package ERE.DS.BlackJack;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String args[]) throws Exception {
        int aux = 0;
        boolean stop = false;
        while (!stop) {
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

            DatagramSocket clientSocket = new DatagramSocket();

            InetAddress IPAddress = InetAddress.getByName("localhost");

            byte[] sendData = new byte[1024];
            byte[] receiveData = new byte[1024];
            if (aux == 0) {
                aux++;
                System.out.println("Deseja jogar (s/n)");
                String sentence = inFromUser.readLine();
                sendData = sentence.getBytes();
                if (sentence.equals("n")) break;
                else if (!sentence.equals("s")) {
                    System.out.println("Comando desconhecido");
                    aux--;
                } else {
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);

                    clientSocket.send(sendPacket);

                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

                    clientSocket.receive(receivePacket);

                    String modifiedSentence = new String(receivePacket.getData());

                    System.out.println("FROM SERVER:" + modifiedSentence);

                    clientSocket.close();
                }
            } else {
                System.out.println("Desejas outra (s/n)");
                String sentence = inFromUser.readLine();
                sendData = sentence.getBytes();

                if (sentence.equals("n")) stop = true;
                else if (!sentence.equals("s")) {
                    System.out.println("Comando desconhecido");
                    break;
                }
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);

                clientSocket.send(sendPacket);

                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

                clientSocket.receive(receivePacket);

                String modifiedSentence = new String(receivePacket.getData());

                System.out.println("FROM SERVER:" + modifiedSentence);

                if(modifiedSentence.contains("Estorou")) {
                    stop = true;
                }

                clientSocket.close();

            }

        }

    }
}