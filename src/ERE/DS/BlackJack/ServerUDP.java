package ERE.DS.BlackJack;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ServerUDP {
    public static void main(String args[]) throws Exception
    {

        DatagramSocket serverSocket = new DatagramSocket(9876);

        byte[] receiveData = new byte[1024];
        byte[] sendData  = new byte[1024];

        ArrayList<String> cards = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            cards.add("A");
            cards.add("2");
            cards.add("3");
            cards.add("4");
            cards.add("5");
            cards.add("6");
            cards.add("7");
            cards.add("8");
            cards.add("9");
            cards.add("10");
            cards.add("J");
            cards.add("Q");
            cards.add("K");
        }
        Collections.shuffle(cards);
        int aux = 0;
        int total = 0;
        while(true)
        {
            DatagramPacket receivePacket =
                    new DatagramPacket(receiveData, receiveData.length);

            serverSocket.receive(receivePacket);

            String sentence = new String(receivePacket.getData());
            System.out.println(sentence);
            InetAddress IPAddress = receivePacket.getAddress();

            int port = receivePacket.getPort();
            String send = "";
            if (aux == 0) {
                aux++;
                String card = cards.remove(cards.size() - 1);
                total += convertToInt(card);
                send += card + ", ";
                card = cards.remove(cards.size() - 1);
                total += convertToInt(card);
                send += card + " - Total: " + total;
                send += total > 21 ? " Estorou" : "";
            }
            else {
                byte[] aux1 = new byte[1024];
                aux1[0] = "s".getBytes(StandardCharsets.UTF_8)[0];
                byte[] aux2 = sentence.getBytes(StandardCharsets.UTF_8);
                if (Arrays.equals(aux1, aux2)) {
                    String card = cards.remove(cards.size() - 1);
                    total += convertToInt(card);
                    send = card + " - Total: " + total;
                    send += total > 21 ? " Estorou" : "";
                }
                else {
                    aux = 0;
                    send = "Total: " + total;
                    total = 0;
                }
            }
            if (send.contains("Estorou")) {
                aux = 0;
                total = 0;
            }
            sendData = send.getBytes();

            DatagramPacket sendPacket =
                    new DatagramPacket(sendData, sendData.length, IPAddress,
                            port);

            serverSocket.send(sendPacket);
        }
    }

    private static int convertToInt(String card) {
        if (card.equals("A")) {
            return 1;
        }
        return card.equals("J") || card.equals("Q") || card.equals("K") ? 10 : Integer.parseInt(card);
    }
}
