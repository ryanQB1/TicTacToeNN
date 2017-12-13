package tictactoenn;

import Common.*;
import giveAdvice.giveAdvice;
import java.util.Scanner;

public class TicTacToeNN {
    
    private static Symbol playingas;
    private static Symbol notplayingas;
    private static boolean myturn;

    public static void main(String args[]){
        Scanner reader = new Scanner(System.in);
        giveAdvice adgiv;
        System.out.println("Who do you wanna play as?  (O starts first, X starts second)");
        String ans = reader.nextLine();
        if(ans.equalsIgnoreCase("X")){
            notplayingas=Symbol.O;
            playingas=Symbol.X;
            myturn=false;
            adgiv = new giveAdvice("O");
        }
        else if(ans.equalsIgnoreCase("O")){
            notplayingas=Symbol.X;
            playingas=Symbol.O;
            myturn=true;
            adgiv = new giveAdvice("X");
        }
        else{
            System.out.println("Not a valid player, exiting!");
            return;
        }
        Field mainfield = new Field();
        int it = 0;
        while(mainfield.checkWin().equals(Symbol.EMPTY)&&it<9){
            it++;
            if(myturn){
                drawField(mainfield);
                int answ = -1;
                boolean don = true;
                while(don){
                    System.out.println("Place your next symbol");
                    answ = reader.nextInt();
                    if(answ>-1 && answ<9 && mainfield.getSymbol(answ/3, answ%3).equals(Symbol.EMPTY)) don = false;
                }
                mainfield.placeSymbol(playingas, answ/3, answ%3);
                myturn=!myturn;
            }
            else{
                drawField(mainfield);
                Field advt = adgiv.giveA(mainfield);
                boolean don = false;
                for(int a = 0; a < 3 && !don; a++){
                    for(int b = 0; b < 3 && !don; b++){
                        Field toadv = mainfield.deepClone();
                        toadv.placeSymbol(notplayingas, a, b);
                        if(toadv.compareTo(advt)){
                            don=true;
                            mainfield.placeSymbol(notplayingas, a, b);
                        }
                    }
                }
                myturn=!myturn;
            }
        }
        drawField(mainfield);
        System.out.print("Game done!");
        if(mainfield.checkWin().equals(Symbol.X)){
            if(playingas.equals(Symbol.X)){
                System.out.println(" You won! (This shouldn't happen)");
            }
            else{
                System.out.println(" You lost! What a surprise!");
            }
        }
        else if(mainfield.checkWin().equals(Symbol.O)){
            if(playingas.equals(Symbol.O)){
                System.out.println(" You won! (This shouldn't happen)");
            }
            else{
                System.out.println(" You lost! What a surprise!");
            }
        }
        else{
            System.out.println(" It's a draw! Congratz");
        }
    }
    
    private static void drawField(Field fie){
        System.out.println("Field  Placeat");
        for(int a = 0; a < 9; a+=3){
            String toPrint1="";
            String toPrint2="";
            for(int b = 0; b < 3; b++){
                if(fie.getField()[a/3][b].equals(Symbol.EMPTY)){
                    toPrint1 += "E";
                    toPrint2 += Integer.toString(a+b);
                }
                if(fie.getField()[a/3][b].equals(Symbol.X)){
                    toPrint1 += "X";
                    toPrint2 += " ";
                }
                if(fie.getField()[a/3][b].equals(Symbol.O)){
                    toPrint1 += "O";
                    toPrint2 += " ";
                }
                if(b!=2){
                    toPrint1 +="|";
                    toPrint2 +="|";
                }
            }
            System.out.println(toPrint1 + "  ^  " + toPrint2);
            System.out.println("-----     -----");
        }
        System.out.println(" ");
        System.out.println("----------");
    }
    
}
