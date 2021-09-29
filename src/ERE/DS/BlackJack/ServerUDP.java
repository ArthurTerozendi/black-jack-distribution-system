package ERE.DS.BlackJack;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collection;
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
        boolean stop = false;
        while(!stop)
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
            }
            else {
                if (sentence.equals("y")) {
                    String card = cards.remove(cards.size() - 1);
                    total += convertToInt(card);
                    send = card + " - Total: " + total;
                }
                else {
                    send = "Total: " + total;
                    stop = true;
                }
            }

            sendData = send.getBytes();

            DatagramPacket sendPacket =
                    new DatagramPacket(sendData, sendData.length, IPAddress,
                            port);

            serverSocket.send(sendPacket);
        }

        serverSocket.close();
    }

    private static int convertToInt(String card) {
        return card.equals("J") || card.equals("Q") || card.equals("K") ? 10 : Integer.parseInt(card);
    }
}
