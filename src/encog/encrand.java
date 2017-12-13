package encog;

import Common.Field;
import Common.Symbol;
import giveAdvice.giveAdvice;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import org.encog.engine.network.activation.ActivationTANH;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.train.MLTrain;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;

public class encrand {

    private static Symbol playingas;
    private static Symbol notplayingas;
    private static boolean myturn;

    public static void main(String args[]) {

        BasicNetwork network = new BasicNetwork();

        //edit this block to change the layout of the NN
        network.addLayer(new BasicLayer(null, true, 9));
        network.addLayer(new BasicLayer(new ActivationTANH(), true, 12));
        network.addLayer(new BasicLayer(new ActivationTANH(), true, 9));
        network.addLayer(new BasicLayer(new ActivationTANH(), true, 9));
        network.addLayer(new BasicLayer(new ActivationTANH(), false, 9));
        network.getStructure().finalizeStructure();
        network.reset();

        giveAdvice gia = new giveAdvice("X");

        List<Field> probSet = null;
        double[][] soluSet = null;

        System.out.println("Initialized");

        try {
            probSet = getSet("Training.txt");
            soluSet = getSoluSet(gia, probSet);
        } catch (IOException ex) {
            System.out.println("Exception while creating sets");
        }

        System.out.println("Datasets created");

        double[][] finprobset = conv(probSet);

        MLDataSet trainingSet = new BasicMLDataSet(finprobset, soluSet);

        MLTrain train = new ResilientPropagation(network, trainingSet);

        System.out.println("Datasets converted");
        System.out.println("Learning started");

        int epoch = 1;
        do {
            train.iteration();
            System.out.println("Epoch#" + epoch + " Error:" + train.getError());
            epoch++;
        } while (train.getError() > 0.05);//edit the 0.01 to determine when to stop learning

        System.out.println("Learning ended");

        System.out.println("Let's play a game, you are O (starting first)");
        Scanner reader = new Scanner(System.in);

        notplayingas = Symbol.X;
        playingas = Symbol.O;
        myturn = true;
        int draws = 0;
        int AIwins = 0;
        int randWins = 0;
        int games = 0;
        while (games < 10000) {
            games++;
            System.out.println("Game #" + games);
            myturn = true;
            Field mainfield = new Field();
            int it = 0;
            while (mainfield.checkWin().equals(Symbol.EMPTY) && it < 9) {
                it++;
                if (myturn) {
                    //drawField(mainfield);
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
                    //drawField(mainfield);
                    MLData inp = new BasicMLData(fieldToDouble(mainfield));
                    MLData outp = network.compute(inp);
                    double max = 0;
                    int fina = 0;
                    int finb = 0;
                    for (int a = 0; a < 3; a++) {
                        for (int b = 0; b < 3; b++) {
                            if (outp.getData()[3 * a + b] > max && mainfield.getSymbol(a, b).equals(Symbol.EMPTY)) {
                                max = outp.getData()[3 * a + b];
                                fina = a;
                                finb = b;
                            }
                        }
                    }
                    //System.out.println("Output:" + printdouble(outp.getData()));
                    //System.out.println("Option chosen:" + (3 * fina + finb));
                    mainfield.placeSymbol(notplayingas, fina, finb);
                    myturn = !myturn;
                }
            }
            drawField(mainfield);
            if (mainfield.checkWin().equals(Symbol.X)) {
                AIwins++;
            } else if (mainfield.checkWin().equals(Symbol.O)) {
                randWins++;
            } else {
                draws++;
            }
            //END
        }
        System.out.println("AI - wins:" + AIwins);
        System.out.println("Random - wins:" + randWins);
        System.out.println("Draws:" + draws);
    }

    private static List<Field> getSet(String fileN) throws IOException {
        List<Field> temp = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(fileN));

        try {
            String line = br.readLine();

            while (line != null) {
                temp.add(strToF(line));
                line = br.readLine();
            }
        } finally {
            br.close();
        }
        return temp;
    }

    private static double[][] getSoluSet(giveAdvice gia, List<Field> probs) {
        double[][] toOut = new double[probs.size()][9];
        int ind = 0;
        for (Field mf : probs) {
            Field advt = gia.giveA(mf);
            if (advt == null) {
                double[] g = new double[9];
                toOut[ind] = g;
                ind++;
                continue;
            }
            boolean don = false;
            double[] fin = new double[9];
            for (int a = 0; a < 3 && !don; a++) {
                for (int b = 0; b < 3 && !don; b++) {
                    Field toadv = mf.deepClone();
                    toadv.placeSymbol(Symbol.X, a, b);
                    if (toadv.compareTo(advt)) {
                        don = true;
                        fin[3 * a + b] = 1;
                    }
                }
            }
            toOut[ind] = fin;
            ind++;
        }
        return toOut;
    }

    private static Field strToF(String str) {
        Field fie = new Field();
        for (int a = 0; a < str.length(); a++) {
            switch (str.charAt(a)) {
                case 'E':
                    fie.placeSymbol(Symbol.EMPTY, a / 3, a % 3);
                    break;
                case 'O':
                    fie.placeSymbol(Symbol.O, a / 3, a % 3);
                    break;
                case 'X':
                    fie.placeSymbol(Symbol.X, a / 3, a % 3);
                    break;
                default:
                    System.out.println("Error while converting String to a Field");
            }
        }
        return fie;
    }

    private static double[][] conv(List<Field> fiel) { //Edit this one to change the input to the system
        double[][] finfin = new double[fiel.size()][9];
        int ind = 0;
        for (Field f : fiel) {
            Symbol[][] g = f.getField();
            double[] ghg = new double[9];
            for (int a = 0; a < 3; a++) {
                for (int b = 0; b < 3; b++) {
                    if (g[a][b].equals(Symbol.EMPTY)) {
                        ghg[a * 3 + b] = -1;//here
                    } else if (g[a][b].equals(Symbol.X)) {
                        ghg[a * 3 + b] = 0;//here
                    } else if (g[a][b].equals(Symbol.O)) {
                        ghg[a * 3 + b] = 1;//and here
                    }
                }
            }
            finfin[ind] = ghg;
            ind++;
        }
        return finfin;
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

    private static double[] fieldToDouble(Field fie) {
        double[] temp = new double[9];
        Symbol[][] g = fie.getField();
        for (int a = 0; a < 3; a++) {
            for (int b = 0; b < 3; b++) {
                if (g[a][b].equals(Symbol.EMPTY)) {
                    temp[a * 3 + b] = -1;
                } else if (g[a][b].equals(Symbol.X)) {
                    temp[a * 3 + b] = 0;
                } else if (g[a][b].equals(Symbol.O)) {
                    temp[a * 3 + b] = 1;
                }
            }
        }
        return temp;
    }

    private static String printdouble(double[] toPrint) {
        String to = "";
        for (int a = 0; a < toPrint.length; a++) {
            to += toPrint[a] + " ; ";
        }
        return to;
    }
}
