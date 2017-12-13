package tictactoenn;

import Common.*;
import giveAdvice.giveAdvice;
import java.util.Random;
import java.util.Scanner;

public class TicTacToeNNrand {

    private static Symbol playingas;
    private static Symbol notplayingas;
    private static boolean myturn;

    public static void main(String args[]) {
        Scanner reader = new Scanner(System.in);
        giveAdvice adgiv;
        notplayingas = Symbol.X;
        playingas = Symbol.O;
        adgiv = new giveAdvice("X");
        int draws = 0;
        int AIwins = 0;
        int randWins = 0;
        int games = 0;
        while (games < 1000) {
            myturn = true;
            Field mainfield = new Field();
            games++;
            System.out.println("Game #" + games);
            int it = 0;
            while (mainfield.checkWin().equals(Symbol.EMPTY) && it < 9) {
                it++;
                if (myturn) {
                    int answ = -1;
                    boolean don = true;
                    while (don) {
                        Random r = new Random();
                        answ = r.nextInt(9);
                        if (answ > -1 && answ < 9 && mainfield.getSymbol(answ / 3, answ % 3).equals(Symbol.EMPTY)) {
                            don = false;
                        }
                    }
                    mainfield.placeSymbol(playingas, answ / 3, answ % 3);
                    myturn = !myturn;
                } else {
                    Field advt = adgiv.giveA(mainfield);
                    boolean don = false;
                    for (int a = 0; a < 3 && !don; a++) {
                        for (int b = 0; b < 3 && !don; b++) {
                            Field toadv = mainfield.deepClone();
                            toadv.placeSymbol(notplayingas, a, b);
                            if (toadv.compareTo(advt)) {
                                don = true;
                                mainfield.placeSymbol(notplayingas, a, b);
                            }
                        }
                    }
                    myturn = !myturn;
                }
            }
            if (mainfield.checkWin().equals(Symbol.X)) {
                AIwins++;
            } else if (mainfield.checkWin().equals(Symbol.O)) {
                randWins++;
            } else {
                draws++;
            }
        }
        System.out.println("AI - wins:" + AIwins);
        System.out.println("Random - wins:" + randWins);
        System.out.println("Draws:" + draws);
    }

    private static void drawField(Field fie) {
        System.out.println("Field  Placeat");
        for (int a = 0; a < 9; a += 3) {
            String toPrint1 = "";
            String toPrint2 = "";
            for (int b = 0; b < 3; b++) {
                if (fie.getField()[a / 3][b].equals(Symbol.EMPTY)) {
                    toPrint1 += "E";
                    toPrint2 += Integer.toString(a + b);
                }
                if (fie.getField()[a / 3][b].equals(Symbol.X)) {
                    toPrint1 += "X";
                    toPrint2 += " ";
                }
                if (fie.getField()[a / 3][b].equals(Symbol.O)) {
                    toPrint1 += "O";
                    toPrint2 += " ";
                }
                if (b != 2) {
                    toPrint1 += "|";
                    toPrint2 += "|";
                }
            }
            System.out.println(toPrint1 + "  ^  " + toPrint2);
            System.out.println("-----     -----");
        }
        System.out.println(" ");
        System.out.println("----------");
    }

}
