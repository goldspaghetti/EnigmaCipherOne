import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static java.util.Map.entry;

public class EnigmaCipher {
    static int[][] plugboardConfig = {{0, 1}, {2, 3}};
    //static int[][] plugboardConfig = {{}};
    static Plugboard plugboard = new Plugboard(plugboardConfig);
    //ROTORS ARE RIGHT TO LEFT
    static Rotor[] rotors = {new Rotor(0, 0), new Rotor(1, 0), new Rotor(2, 0)};
    //static Rotor[] reverseRotors = {new Rotor(0, 0, true), new Rotor(1, 0, true), new Rotor(2, 0, true)};
    static Reflector reflector = new Reflector(0);
    public static void main(String[] args){
        /*String testStr = "FVPJIAOYEDRZXWGCTKUQSBNMHL";
        for (char currChar : testStr.toCharArray()){
            System.out.print((int)currChar - 65);
            System.out.print(",");
        }*/

        //get inputs
        String inputStr = "JFCXBP";
        encode(inputStr);
        //System.out.println(rotors[0].keyValue);
        //System.out.println(rotors[1].keyValue);
        //System.out.println(rotors[2].keyValue);

        for (Rotor currRotor : rotors){
            currRotor.keyValue = 0;
        }
    }
    public static int[] encode(String input){
        int[] output = new int[input.length()];
        for (int i = 0; i < input.length(); i++){
            char currChar = input.charAt(i);
            int charValue = (int)currChar - 65;
            charValue = plugboard.translateChar(charValue);
            //System.out.println("!");
            Rotor.rotateRotors(rotors, 0);
            for (Rotor rotor : rotors){
                charValue = rotor.getCorrespondingChar(charValue, false);
                //System.out.println((char)(charValue+65));
            }
            charValue = reflector.getCorrespondingChar(charValue);
            //System.out.println((char)(charValue+65));
            for (int j = rotors.length -1; j >= 0; j--){
                Rotor rotor= rotors[j];
                charValue = rotor.getCorrespondingChar(charValue, true);
                //System.out.println((char)(charValue+65));
            }
            //System.out.println(charValue);
            charValue = plugboard.translateChar(charValue);
            //System.out.println(charValue);
            System.out.print((char)(charValue+65));
            output[i] = charValue;
        }
        return output;
    }
}
class Plugboard {
    //HashMap plugboardKey = new HashMap<Integer, Integer>(26);
    int[] plugboardConfig;
    Plugboard(int[][] plugboardConfig){
        this.plugboardConfig = new int[26];
        for (int i = 0; i < this.plugboardConfig.length; i++){
            this.plugboardConfig[i] = i;
        }
        for (int[] ints : plugboardConfig) {
            this.plugboardConfig[ints[0]] = ints[1];
            this.plugboardConfig[ints[1]] = ints[0];
        }
    }
    public int translateChar(int inputChar){
        return this.plugboardConfig[inputChar];
    }
}
class Rotor{
    //Probably use wheels for this?
    static int[] config1 = {4,10,12,5,11,6,3,16,21,25,13,19,14,22,24,7,23,20,18,15,0,8,1,17,2,9};
    static int[] config2 = {0,9,3,10,18,8,17,20,23,1,11,7,22,19,12,2,16,6,25,13,15,24,5,21,14,4};
    static int[] config3 = {1,3,5,7,9,11,2,15,17,19,23,21,25,13,24,4,8,22,6,0,10,12,20,18,16,14};
    //HashMap rotorKey = new HashMap<Integer,Integer>(26);
    int[] rotorConfig;
    int[] rotorReverseConfig;
    int keyValue = 0;
    int turnOverNum = 17;
    static int config1TurnOverNum = 17;
    static int config2TurnOverNum = 5;
    static int config3TurnOverNum = 1;
    //static int config1 turnOverNum =
    Rotor(int wheelNum, int keyValue){
        //just using config1 for now
        if (wheelNum == 0){
            this.rotorConfig = config1;
            this.turnOverNum = config1TurnOverNum;
        }
        else if (wheelNum == 1){
            this.rotorConfig = config2;
            this.turnOverNum = config2TurnOverNum;
        }
        else{
            this.rotorConfig = config3;
            this.turnOverNum = config3TurnOverNum;
        }
        this.keyValue = keyValue;
        rotorReverseConfig = new int[rotorConfig.length];
        for (int i = 0; i < rotorConfig.length; i++){
            int currValue = rotorConfig[i];
            rotorReverseConfig[currValue] = i;
        }
        /*if (reverse){
            int[] newRotorConfig = new int[rotorConfig.length];
            for(int i = 0; i < rotorConfig.length; i++){
                int currValue = rotorConfig[i];
                newRotorConfig[currValue] = i;
            }
        }
        this.reverse = reverse;*/
    }
    public int getCorrespondingChar(int key, boolean reverse){
        if (key < 0 || key > 25){
            //System.out.println("out of range");
            System.out.println(key);
            return -1;
        }
        if (key+keyValue > 25){
            //System.out.println("going down");
            key -= 26;
        }
        int currValue;
        if (reverse){
            currValue= rotorReverseConfig[key+keyValue]-keyValue;
        }
        else{
            currValue= rotorConfig[key+keyValue]-keyValue;
        }
        if (currValue <0){
            currValue +=26;
        }
        return currValue;
    }
    public boolean turnRotor(){
        this.keyValue++;

        if (keyValue > 25){
            keyValue = 0;
        }
        return keyValue == turnOverNum;
    }
    public static void rotateRotors(Rotor[] rotors, int currRotorNum){
        //System.out.println("rotating rotors");
        Boolean rotateNext = true;
        while (rotateNext){
            rotateNext = rotors[currRotorNum].turnRotor();
            if (currRotorNum == rotors.length-1 && rotateNext){
                currRotorNum--;
            }
            else if (rotateNext){
                currRotorNum++;
            }
        }
    }
}
//maybe? just combination + wheel type though
class Wheel{
    //combination
    //wheel type, turnOverNum
    int[] combination;
    int turnOverKey;
    Wheel(){

    }
}
class Reflector{
    static int[] reflectorB = {24,17,20,7,16,18,11,3,15,23,13,6,14,10,12,8,4,1,5,25,2,22,21,9,0,19};
    static int[] reflectorC = {5,21,15,9,8,0,14,24,4,3,17,25,23,22,6,2,19,10,20,16,18,1,13,12,7,11,};
    //MAYBE IMPlEMENT ReFlECTOR D IN SOME WAY
    int[] reflectorValues;
    Reflector(int selectedReflector){
        if (selectedReflector == 0){
            this.reflectorValues = reflectorB;
        }
        else{
            this.reflectorValues = reflectorC;
        }
    }
    public int getCorrespondingChar(int currChar){
        return reflectorValues[currChar];
    }
}
