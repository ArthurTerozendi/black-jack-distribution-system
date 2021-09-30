package ERE.DS.BlackJack;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String args[]) throws Exception {
        int aux = 0;
        boolean stop = false;

        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

        DatagramSocket clientSocket = new DatagramSocket();

        while (!stop) {
            if (aux == 0) {
                aux++;
                System.out.println("Deseja jogar (s/n)");
                String sentence = inFromUser.readLine();

                if (sentence.equals("n")) break;
                else if (!sentence.equals("s")) {
                    System.out.println("Comando desconhecido");
                    aux--;
                } else {
                    sendMsg(sentence, clientSocket);

                    stop = receiveMsg(clientSocket);
                }
            } else {
                System.out.println("Desejas outra (s/n)");
                String sentence = inFromUser.readLine();

                if (sentence.equals("n")) stop = true;
                else if (!sentence.equals("s")) {
                    System.out.println("Comando desconhecido");
                    break;
                }

                sendMsg(sentence, clientSocket);

                stop = receiveMsg(clientSocket);
            }

        }

        clientSocket.close();
    }

    private static void sendMsg(String msg, DatagramSocket clientSocket) throws IOException {
        byte[] sendData = new byte[1024];
        InetAddress IPAddress = InetAddress.getByName("localhost");
        sendData = msg.getBytes();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);

        clientSocket.send(sendPacket);
    }

    private static boolean receiveMsg(DatagramSocket clientSocket) throws IOException {
        byte[] receiveData = new byte[1024];

        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);

        String response = new String(receivePacket.getData());

        System.out.println("FROM SERVER:" + response);

        return response.contains("Estourou");
    }
}