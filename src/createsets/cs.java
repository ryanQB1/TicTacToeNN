package createsets;

import Common.Field;
import Common.Symbol;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class cs {

    public static void main(String args[]) {
        boolean oturn;
        List<Field> lf = new ArrayList<>();
        for (int a = 1; a < 8; a += 2) {
            int m = 0;
            switch (a) {
                case 1:
                    m = 6;
                    break;
                case 3:
                    m = 250;
                    break;
                case 5:
                    m = 1100;
                    break;
                case 7:
                    m = 600;
                    break;
            }
            System.out.println("Creating #" + a);
            for (int amn = 0; amn < m; amn++) {
                Field temp = new Field();
                oturn = true;
                for (int b = 0; b < a; b++) {
                    boolean good = true;
                    Random r = new Random();
                    int q = r.nextInt(3);
                    int w = r.nextInt(3);
                    if (oturn) {
                        if (temp.getSymbol(q, w).equals(Symbol.EMPTY)) {
                            temp.placeSymbol(Symbol.O, q, w);
                        } else {
                            b--;
                            good = false;
                        }

                    } else {
                        if (temp.getSymbol(q, w).equals(Symbol.EMPTY)) {
                            temp.placeSymbol(Symbol.X, q, w);
                        } else {
                            b--;
                            good = false;
                        }

                    }
                    if(good) oturn = !oturn;
                }
                boolean fr = false;
                if (!temp.checkWin().equals(Symbol.EMPTY)) {
                    amn--;
                    fr = true;
                }
                for (Field q : lf) {
                    if (q.singleCompareTo(temp)) {
                        amn--;
                        fr = true;
                    }
                }
                if(!fr) lf.add(temp);
            }
        }
        Collections.shuffle(lf);
        BufferedWriter out2 = null;
        try {
            out2 = new BufferedWriter(new FileWriter("Training.txt"));
        } catch (IOException ex) {
            System.out.println("Can't write");
        }
        for (Field ff : lf) {
            try {
                out2.write(fieldtostring(ff) + "\n");
            } catch (IOException ex) {
                System.out.println("Exception");
            }
        }
        try {
            out2.close();
        } catch (IOException ex) {
            System.out.println("Exception");
        }
    }

    private static String fieldtostring(Field fie) {
        String tostr = "";
        Symbol[][] symf = fie.getField();
        for (int a = 0; a < 3; a++) {
            for (int b = 0; b < 3; b++) {
                if (symf[a][b].equals(Symbol.EMPTY)) {
                    tostr += "E";
                }
                if (symf[a][b].equals(Symbol.X)) {
                    tostr += "X";
                }
                if (symf[a][b].equals(Symbol.O)) {
                    tostr += "O";
                }
            }
        }
        return tostr;
    }

}
