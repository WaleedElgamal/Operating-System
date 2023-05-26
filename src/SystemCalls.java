package src;

import java.io.*;
import java.util.Scanner;

public class SystemCalls {

    public static void print(String message) {
        System.out.println(message);
    }

    public static String readFile(String filename) {

        String res = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
            String line = br.readLine();
            while (line != null) {
                res += line + "\n";
                line = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            System.out.println("File not found");
        }
        return res;
    }

    public static void writeFile(String filename, String content){
        try {
            File file = new File(filename);
            file.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(content);
            bw.close();
        } catch (IOException e) {
            System.out.println("File not found");
        }
    }

    public static String getInput(String prompt) {
        System.out.println(prompt);
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

    public static MemoryWord readMem(int address) {
        return OperatingSystem.getMemory()[address];
    }

    public static void writeMem(MemoryWord mem, int address) {
        //when we write into memory a new variable using assign, create a new word
        OperatingSystem.getMemory()[address].setVariableName(mem.getVariableName());
        OperatingSystem.getMemory()[address].setValue(mem.getValue());
        OperatingSystem.getMemory()[address].setInstruction(mem.getInstruction());
    }
}
