package net.lanesurface.encryption;

import static java.lang.Math.*;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Encrypter {
    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
                                IGNORE_CHARS = "'!&():;/\"?.,-`’“”\\";
    private static final int NUM_CHARS = CHARS.length();
    
    private Map<Character, Character> charTable;
    
    Encrypter(String code) {
        int n = Integer.parseInt(code.substring(0, 2)),
            q = Integer.parseInt(code.substring(2));
        
        charTable = new HashMap<>();
        
        for (int i = 0; i < NUM_CHARS; i++)
            charTable.put(CHARS.charAt(i),
                          CHARS.charAt((i + abs(n - q)) % NUM_CHARS));
    }
    
    public void encryptFile(File file) 
            throws java.io.IOException {
        Scanner scanner = new Scanner(file);
        
        PrintWriter writer = new PrintWriter(file.getName() + ".enc", 
            "UTF-8");
        
        while (scanner.hasNextLine()) {
            String[] words = scanner.nextLine().split("[ ]+");
            
            for (String word : words)
                writer.print(encrypt(word) + " ");
            writer.println();
        }
        
        writer.close();
        scanner.close();
    }
    
    public String encrypt(String s) {
        StringBuilder builder = new StringBuilder();
        s = s.toUpperCase();
        
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            
            if (IGNORE_CHARS.contains(""+c)) builder.append(c);
            else builder.append(charTable.get(s.charAt(i)));
        }
        
        return builder.toString();
    }
    
    public static String generateCypher() {
        int n = getRandomIndex(),
            q = getRandomIndex();
        
        return stringify(n) + stringify(q);
    }
    
    private static int getRandomIndex() {
        return (int)(random()*NUM_CHARS+1);
    }
    
    private static String stringify(int i) {
        return i >= 10 ? ""+i : "0"+i;
    }
    
    private static final String USAGE = 
        "Enter the path to a file you would like to encrypt.";
    
    public static void main(String[] args)
            throws java.io.IOException {
        if (args.length < 1) {
            System.out.println(USAGE);
            return;
        }
        
        String cypher = generateCypher();
        Encrypter e = new Encrypter(cypher);
        
        System.out.println("CYPHER: <" + cypher + ">\n" + 
            "Keep this code in a safe place; it can be used to " +
            "decrypt your files.");
        
        e.encryptFile(new File(args[0]));
    }
}
