/*  
    Title:      Main.java
	Name:       Dylan Kapustka (Dlk190000)
    Instructor: Professor Ozbirn
    Course:     CS 4348.001 - S21
    Date:       03/09/2021

    Description: This class creates a new CPU object and tells it to start processing.
*/

import java.util.*;
import java.io.*;

public class Main {
	public static void main(String[] args) { 
		// Check for two arguments expected
        if (args.length != 2) {
            System.out.println("Error Incorrect Arguments:" + Arrays.toString(args));
            System.exit(0);
        }
        
        try {
			// set up the memory
			Process memory = Runtime.getRuntime().exec("java Memory " + args[0]);
			InputStream is = memory.getInputStream();
			OutputStream os = memory.getOutputStream();
			int timer = Integer.parseInt(args[1]); //Set timer to interrupt val
            String file = args[0]; // Store file name for ease of access later

			//Create our CPU
			CPU cpu = new CPU(is, os, timer, file);
			cpu.startCPU();

		} catch (Exception e) { 
			e.printStackTrace();
		}
	}
}