package ERE.DS.BlackJack;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ServerUDP {
    static boolean[] firstHand = new boolean[2];
    static int[] players = new int[2];
    static int[] total = new int[2];
    static ArrayList<String> cards = new ArrayList<>();
    public static void main(String args[]) throws Exception
    {
        DatagramSocket serverSocket = new DatagramSocket(9876);

        resetGame();

        byte[] receiveData = new byte[1024];
        boolean firstPlayer = true;
        boolean allPlayers = false;

        while(true)
        {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            serverSocket.receive(receivePacket);

            String sentence = new String(receivePacket.getData());
            System.out.println(receivePacket.getSocketAddress());
            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();

            if (players[0] == 0) {
                players[0] = port;
            }
            else if (players[1] == 0) {
                players[1] = port;
                allPlayers = true;
                System.out.println(players[0] + " " + players[1]);
            }
            else if (players[0] != port && players[1] != port) {
                sendMsg("Número máximo de jogadores alcançados", serverSocket, IPAddress, port);
            }

            String send = "";
            int turnPlayer = firstPlayer? players[0] : players[1];
            boolean firstH = firstPlayer ? firstHand[0] : firstHand[1];

            if (firstH && allPlayers && port == turnPlayer) {
                if (firstPlayer) firstHand[0] = false;
                else firstHand[1] = false;

                send += getCard(firstPlayer) + ", " + getCard(firstPlayer) + " - Total: ";
                int t = firstPlayer ? total[0] : total[1];
                send += t;
                send += t > 21 ? " Estorou" : "";
            }
            else if (port == turnPlayer){
                if (allPlayers) {
                    byte[] aux1 = new byte[1024];
                    aux1[0] = "s".getBytes()[0];
                    byte[] aux2 = sentence.getBytes();
                    if (Arrays.equals(aux1, aux2)) {
                        send = getCard(firstPlayer) + " - Total: ";
                        int t = firstPlayer ? total[0] : total[1];
                        send += t;
                        send += t > 21 ? " Estorou" : "";
                    } else {
                        send = "Total: " + total;
                        resetGame();
                    }
                }
            }

            if (send.contains("Estorou")) {
                resetGame();
            }

            if (allPlayers) {
                if (port == turnPlayer) {
                    sendMsg(send, serverSocket, IPAddress, turnPlayer);
                    firstPlayer = !firstPlayer;
                }
                else {
                    sendMsg("Esperando o outro jogador", serverSocket, IPAddress, port);
                }
            }
            else {
                sendMsg("Esperando o outro jogador", serverSocket, IPAddress, port);
            }
        }
    }

    private static void sendMsg(String msg, DatagramSocket serverSocket, InetAddress IPAddress, int port) throws IOException {

        byte[] sendData  = new byte[1024];
        sendData = msg.getBytes();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);

        serverSocket.send(sendPacket);
    }

    private static String getCard(boolean firstPlayer) {
        String card = cards.remove(cards.size() - 1);

        if (firstPlayer) total[0] += convertToInt(card);
        else total[1] += convertToInt(card);

        return card;
    }

    private static int convertToInt(String card) {
        if (card.equals("A")) {
            return 1;
        }
        return card.equals("J") || card.equals("Q") || card.equals("K") ? 10 : Integer.parseInt(card);
    }

    private static void resetGame() {
        for (int i = 0; i < 2; i++) {
            firstHand[i] = true;
            total[i] = 0;
            players[0] = 0;
        }
        cards = shuffleCards();
    }

    private static ArrayList<String> shuffleCards() {
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
        return cards;
    }
}
