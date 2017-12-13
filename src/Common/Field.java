package Common;

public class Field {
    private Symbol[][] field = new Symbol[3][3];
    
    public Field(){
        for(int a = 0; a < 3;a++){
            for(int b = 0; b < 3; b++){
                field[a][b]=Symbol.EMPTY;
            }
        }
    }
    
    public Field(Symbol[][] s){
        this.field=s;
    }
    
    public void placeSymbol(Symbol s, int a, int b){
        field[a][b]=s;
    }
    
    public Symbol getSymbol(int a, int b){
        return field[a][b];
    }
    
    public Symbol[][] getField(){
        return field;
    }
    
    public boolean compareTo(Field that) {
        if(this.singleCompareTo(that)) return true;
        Field temp = this.deepClone();
        for(int a = 0; a < 3; a++){
            temp=turn90(temp);
            if(temp.singleCompareTo(that)) return true;
        }
        temp=temp.mirror();
        if(this.singleCompareTo(that)) return true;
        for(int a = 0; a < 3; a++){
            temp=turn90(temp);
            if(temp.singleCompareTo(that)) return true;
        }
        return false;
    }
    
    public boolean singleCompareTo(Field that){
        for(int a = 0; a < 3;a++){
            for(int b = 0; b < 3; b++){
                if(!this.field[a][b].equals(that.field[a][b])) return false;
            }
        }
        return true;
    }
    
    public Field turn90(Field toTurn){
        Field newField = new Field();
        for(int a = 0; a < 3; a++){
            for(int b = 0; b < 3; b++){
                newField.field[a][b]=toTurn.field[b][2-a];
            }
        }
        return newField;
    }
    
    public Field mirror(){
        Field newField = new Field();
        Field tomirr = this.deepClone();
        for(int a = 0; a < 3; a++){
            for(int b = 0; b < 3; b++){
                newField.field[b][a] = tomirr.field[a][b];
            }
        }
        return newField;
    }
    
    public Symbol checkWin(){
        for(Symbol toWin : Symbol.values()){
            if(toWin.equals(Symbol.EMPTY)) break;
            for(int a = 0; a < 3; a++){
                if(this.field[a][0].equals(toWin) && this.field[a][1].equals(toWin) && this.field[a][2].equals(toWin)) return toWin;
            }
            for(int a = 0; a < 3; a++){
                if(this.field[0][a].equals(toWin) && this.field[1][a].equals(toWin) && this.field[2][a].equals(toWin)) return toWin;
            }
            if(this.field[0][0].equals(toWin) && this.field[1][1].equals(toWin) && this.field[2][2].equals(toWin)) return toWin;
            if(this.field[2][0].equals(toWin) && this.field[1][1].equals(toWin) && this.field[0][2].equals(toWin)) return toWin;
        }
        return Symbol.EMPTY;
    }
    
    public Field deepClone(){
        Field clone = new Field();
        for(int a = 0; a < 3; a++){
            for(int b = 0; b < 3; b++){
                if(this.field[a][b].equals(Symbol.EMPTY)) clone.field[a][b]=Symbol.EMPTY;
                if(this.field[a][b].equals(Symbol.X)) clone.field[a][b]=Symbol.X;
                if(this.field[a][b].equals(Symbol.O)) clone.field[a][b]=Symbol.O;
            }
        }
        return clone;
    }
}
