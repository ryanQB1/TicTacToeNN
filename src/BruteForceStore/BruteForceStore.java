package BruteForceStore;

import Common.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BruteForceStore {

    private static class fieldPoss {

        Field thefield = new Field();

        float winchance;
        float losechance;
        float drawchance;

        boolean finished;
        boolean touse;

        int iteration;
        int weight;

        List<fieldPoss> nexts = new ArrayList<>();
    }

    private static class finField {

        Field thefield;
        Field prefField;
    }

    private static BufferedWriter out;
    private static BufferedWriter out2;
    private static List<finField> finlistx = new ArrayList<>();
    private static List<finField> finlisto = new ArrayList<>();

    public static void main(String[] args) {
        fieldPoss mainfield = new fieldPoss();
        boolean xsturn = false;
        Symbol s;
        int iteration = 1;
        List<fieldPoss> thisgen = new ArrayList<>();
        List<fieldPoss> prevgen = new ArrayList<>();
        prevgen.add(mainfield);
        while (true) {
            System.out.println("Iteration: " + iteration);
            if (xsturn) {
                s = Symbol.X;
            } else {
                s = Symbol.O;
            }
            if (prevgen.isEmpty()) {
                break;
            }
            for (fieldPoss poss : prevgen) {
                if (!poss.finished) {
                    for (int a = 0; a < 3; a++) {
                        for (int b = 0; b < 3; b++) {
                            if (poss.thefield.getSymbol(a, b).equals(Symbol.EMPTY)) {
                                Field temp = new Field(poss.thefield.deepClone().getField());
                                temp.placeSymbol(s, a, b);
                                fieldPoss ff;
                                ff = new fieldPoss();
                                ff.thefield = temp;
                                ff.iteration = iteration;
                                ff.touse = !xsturn;
                                Symbol won;
                                if (!(won = ff.thefield.checkWin()).equals(Symbol.EMPTY)) {
                                    if (won.equals(Symbol.X)) {
                                        ff.winchance = 1;
                                        ff.drawchance = 0;
                                        ff.losechance = 0;
                                    } else if (won.equals(Symbol.O)) {
                                        ff.winchance = 0;
                                        ff.drawchance = 0;
                                        ff.losechance = 1;
                                    }
                                    ff.finished = true;
                                }
                                if (iteration == 9 && won.equals(Symbol.EMPTY)) {
                                    ff.winchance = 0;
                                    ff.drawchance = 1;
                                    ff.losechance = 0;
                                    ff.finished = true;
                                }
                                if(!ff.finished){
                                    thisgen.add(ff);
                                }
                                poss.nexts.add(ff);
                            }
                        }
                    }
                }
            }
            iteration++;
            prevgen.clear();
            thisgen.forEach((poss) -> {
                prevgen.add(poss);
            });
            thisgen.clear();
            xsturn = !xsturn;
        }
        try {
            out = new BufferedWriter(new FileWriter("fieldDbO.txt"));
            out2 = new BufferedWriter(new FileWriter("fieldDbX.txt"));
        } catch (IOException e) {
            System.out.println("Exception");
        }
        mainfield = recPoss(mainfield);
        System.out.println("drawchance: " + mainfield.drawchance);
        System.out.println("losechance: " + mainfield.losechance);
        System.out.println("winchance: " + mainfield.winchance);

        for (finField ff : finlistx) {
            try {
                out2.write(fieldtostring(ff.thefield) + "#" + fieldtostring(ff.prefField) + "\n");
            } catch (IOException ex) {
                System.out.println("Exception");
            }
        }
        for (finField ff : finlisto) {
            try {
                out.write(fieldtostring(ff.thefield) + "#" + fieldtostring(ff.prefField) + "\n");
            } catch (IOException ex) {
                System.out.println("Exception");
            }
        }
        try {
            out.close();
            out2.close();
        } catch (IOException ex) {
            System.out.println("Exception");
        }
    }

    private static fieldPoss listcontainsposs(List<fieldPoss> lst, Field fie) {
        for (fieldPoss fp : lst) {
            if (fp.thefield.singleCompareTo(fie)) {
                return fp;
            }
        }
        return null;
    }

    private boolean fieldPossEqual(fieldPoss first, fieldPoss second) {
        return first.thefield.compareTo(second.thefield);
    }

    private static fieldPoss recPoss(fieldPoss poss) {
        if (poss.finished) {
            return poss;
        }
        for (fieldPoss f : poss.nexts) {
            f = recPoss(f);
            poss.drawchance += f.drawchance;
            poss.losechance += f.losechance;
            poss.winchance += f.winchance;
        }
        poss.winchance = poss.winchance / (float) poss.nexts.size();
        poss.drawchance = poss.drawchance / (float) poss.nexts.size();
        poss.losechance = poss.losechance / (float) poss.nexts.size();
        poss.finished = true;

        if (poss.touse) {
            finField ff = new finField();
            ff.thefield = poss.thefield.deepClone();
            float finlosechance = 99999;
            float finwinchance = 0;
            fieldPoss finFielpos = new fieldPoss();
            for (fieldPoss f : poss.nexts) {
                if (f.losechance < finlosechance) {
                    finlosechance = f.losechance;
                    finwinchance = f.winchance;
                    finFielpos = f;
                } else if (f.losechance == finlosechance && f.winchance > finwinchance) {
                    finlosechance = f.losechance;
                    finwinchance = f.winchance;
                    finFielpos = f;
                }
            }
            ff.prefField = finFielpos.thefield.deepClone();
            finlistx.add(ff);
        } else {
            finField ff = new finField();
            ff.thefield = poss.thefield.deepClone();
            float finlosechance = 0;
            float finwinchance = 99999;
            fieldPoss finFielpos = new fieldPoss();
            for (fieldPoss f : poss.nexts) {
                if (f.winchance < finwinchance) {
                    finlosechance = f.losechance;
                    finwinchance = f.winchance;
                    finFielpos = f;
                } else if (f.winchance == finwinchance && f.losechance > finlosechance) {
                    finlosechance = f.losechance;
                    finwinchance = f.winchance;
                    finFielpos = f;
                }
            }
            ff.prefField = finFielpos.thefield.deepClone();
            finlisto.add(ff);
        }

        return poss;
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

    public static String properfieldtostring(Field fie) {
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
            tostr += "\n";
        }
        return tostr;
    }
}
