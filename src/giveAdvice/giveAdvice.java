package giveAdvice;

import Common.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class giveAdvice {
    
    HashMap<Field,Field> hmx = new HashMap<>();
    
    public giveAdvice(String symb){
        try {
            BufferedReader br;
            if(symb.equalsIgnoreCase("O")){
                br = new BufferedReader(new FileReader("fieldDbO.txt"));
            }
            else if(symb.equalsIgnoreCase("X")){
                br = new BufferedReader(new FileReader("fieldDbX.txt"));
            }
            else{
                return;
            }
            
            try {
                String line = br.readLine();

                while (line != null) {
                    String[] temp = line.split("#");
                    hmx.put(strToF(temp[0]), strToF(temp[1]));
                    line = br.readLine();
                }
            } finally {
                br.close();
            }
        } catch (IOException e) {
            System.out.println("Exception while building database");
        }
    }
    
    public Field giveA(Field fie){
        for(Field search : hmx.keySet()){
            if(search.compareTo(fie)){
                return hmx.get(search);
            }
        }
        return null;
    }
    
    
    private Field strToF(String str){
        Field fie = new Field();
        for(int a = 0; a < str.length(); a++){
            switch(str.charAt(a)){
                case 'E':
                    fie.placeSymbol(Symbol.EMPTY, a/3, a%3);
                    break;
                case 'O':
                    fie.placeSymbol(Symbol.O, a/3, a%3);
                    break;
                case 'X':
                    fie.placeSymbol(Symbol.X, a/3, a%3);
                    break;
                default:
                    System.out.println("Error while converting String to a Field");
            }
        }
        return fie;
    }
}
