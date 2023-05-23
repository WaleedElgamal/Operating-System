package src;

import java.io.*;
import java.util.Scanner;

public class SystemCalls {


    public static void print(String message) {
        System.out.println(message);
    }

    public static void print(int message) {
        System.out.println(message);
    }

    public String readFile(String filename) throws IOException {

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

    public void writeFile(String filename, String content) throws IOException {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(filename)));
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
        return memory[address];
    }

    public static void writeMem(MemoryWord mem, int address) {
        memory[address].setVariableName(mem.getVariableName());
        memory[address].setValue(mem.getValue());

    }
}
