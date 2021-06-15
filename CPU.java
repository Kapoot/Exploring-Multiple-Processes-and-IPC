/*  
    Title:      CPU.java
    Name:       Dylan Kapustka (Dlk190000)
    Instructor: Professor Ozbirn
    Course:     CS 4348.001 - S21
    Date:       03/09/2021

    Description: This class simulates a CPU and processes instructions accordingly
*/

import java.io.*;
import java.util.Random;
import java.util.Scanner;

public class CPU {
    // declare registers
    static int PC = 0; // Program Counter
    static int SP = 1000; // Stack Pointer
    static int IR = 0; // Instruction Register
    static int AC = 0; // Accumulator
    static int X = 0;
    static int Y = 0;

    // restrictions
    static int USER_PROGRAM = 1000;
    static int SYSTEM_PROGRAM = 2000;
    static int USER_STACK_TOP = 1000;
    static int SYSTEM_STACK_TOP = 2000;

    // interrupt timer
    static int timer;

    static boolean userMode = true; //checks for kernel mode
    static boolean processingInterrupt = false; //Avoid nested interrupt
    static int instruction_num = 0; //Check num of instructions

    static Scanner reader;
    static PrintWriter pw;
    static String file;
    static InputStream is;
    static OutputStream os;

    //Constructor
    public CPU(InputStream is, OutputStream os, int timer, String file) {
        CPU.is = is;
        CPU.os = os;
        CPU.reader = new Scanner(is);
        CPU.pw = new PrintWriter(os);
        CPU.timer = timer;
        CPU.file = file;
    }

    public void startCPU() {

        try {
            // Send file name to memory
            pw.printf(file + "\n");
            pw.flush();

            //Loop keeps open communication between CPU and Memory
            while (true) {

                //check for timer interrupt and process it
                if ((instruction_num % timer) == 0 && !processingInterrupt && instruction_num > 0) {
                    processingInterrupt = true;

                    int operand;
                    userMode = false; //In Kernel mode

                    operand = SP;
                    SP = SYSTEM_STACK_TOP;
                    //Push value
                    SP--;
                    pw.printf("WRITE:" + SP + ":" + operand + "\n");
                    pw.flush();
                    operand = PC;
                    PC = 1000;
                    //Push value
                    SP--;
                    pw.printf("WRITE:" + SP + ":" + operand + "\n");
                    pw.flush();
                }

                int value = readFromMemory(pw, is, reader, os, PC);

                //If value is appropriate, process the instruction
                if (value != -1) {
                    processInstruction(value, pw, is, reader, os);
                } else
                    break;
            }

        } catch (Exception t) {
            t.printStackTrace();
        }

    }

    //Read data in memory
    private static int readFromMemory(PrintWriter pw, InputStream is, Scanner reader, OutputStream os, int address) {

        //If in usermode and address exceeds to kernel mode, throw an error
        if (userMode && (address >= 1000)) {
            System.out.println("Memory Violation: Accessing system address 1000 in user mode.");
            System.exit(0);
        }

        //"READ" tells memory to read from address
        pw.printf("READ:" + address + "\n");
        pw.flush();

        String res = reader.next();
        return Integer.parseInt(res);

    }

    // Processes instructions
    private static void processInstruction(int value, PrintWriter pw, InputStream is, Scanner reader, OutputStream os) {
        IR = value; //store instruction
        int operand;

        if (!processingInterrupt) {
            instruction_num++;
        }
    

        switch (IR) {
        case 1: //Load the value into the AC
            PC++;
            operand = readFromMemory(pw, is, reader, os, PC);
            AC = operand;
            PC++;
            break;

        case 2: //Load the value at the address into the AC
            PC++;
            operand = readFromMemory(pw, is, reader, os, PC);
            AC = readFromMemory(pw, is, reader, os, operand);
            PC++;
            break;

        case 3: //Load the value from the address found in the address into the AC
            PC++;
            operand = readFromMemory(pw, is, reader, os, PC);
            operand = readFromMemory(pw, is, reader, os, operand);
            AC = readFromMemory(pw, is, reader, os, operand);
            PC++;
            break;

        case 4: //Load the value at (address+X) into the AC
            PC++;
            operand = readFromMemory(pw, is, reader, os, PC);
            AC = readFromMemory(pw, is, reader, os, operand + X);
            PC++;
            break;

        case 5: //Load the value at (address+Y) into the AC
            PC++;
            operand = readFromMemory(pw, is, reader, os, PC);
            AC = readFromMemory(pw, is, reader, os, operand + Y);
            PC++;
            break;

        case 6: //Load from (Sp+X) into the AC
            AC = readFromMemory(pw, is, reader, os, SP + X);
            PC++;
            break;

        case 7: //Store the value in the AC into the address
            PC++;
            operand = readFromMemory(pw, is, reader, os, PC);
            pw.printf("WRITE:" + operand + ":" + AC + "\n");
            pw.flush();
            PC++;
            break;

        case 8: //Gets a random int from 1 to 100 into the AC
            Random r = new Random();
            int i = r.nextInt(100) + 1;
            AC = i;
            PC++;
            break;

        case 9: //If port=1, writes AC as an int to the screen
                //If port=2, writes AC as a char to the screen
            PC++;
            operand = readFromMemory(pw, is, reader, os, PC);
            if (operand == 1) {
                System.out.print(AC);
                PC++;
                break;

            } else if (operand == 2) {
                System.out.print((char) AC);
                PC++;
                break;
            } else {
                System.out.println("Error: Port = " + operand);
                PC++;
                System.exit(0);
                break;
            }

        case 10: //Add the value in X to the AC
            AC = AC + X;
            PC++;
            break;

        case 11: //Add the value in Y to the AC
            AC = AC + Y;
            PC++;
            break;

        case 12: //Subtract the value in X from the AC
            AC = AC - X;
            PC++;
            break;
        case 13: //Subtract the value in Y from the AC
            AC = AC - Y;
            PC++;
            break;

        case 14: //Copy the value in the AC to X
            X = AC;
            PC++;
            break;

        case 15: //Copy the value in X to the AC
            AC = X;
            PC++;
            break;

        case 16: //Copy the value in the AC to Y
            Y = AC;
            PC++;
            break;

        case 17: //Copy the value in Y to the AC
            AC = Y;
            PC++;
            break;

        case 18: //Copy the value in AC to the SP
            SP = AC;
            PC++;
            break;

        case 19: //Copy the value in SP to the AC
            AC = SP;
            PC++;
            break;

        case 20: //Jump to the address
            PC++;
            operand = readFromMemory(pw, is, reader, os, PC);
            PC = operand;
            break;

        case 21: //Jump to the address only if the value in the AC is zero
            PC++;
            operand = readFromMemory(pw, is, reader, os, PC);
            if (AC == 0) {
                PC = operand;
                break;
            }
            PC++;
            break;

        case 22: //Jump to the address only if the value in the AC is not zero
            PC++;
            operand = readFromMemory(pw, is, reader, os, PC);
            if (AC != 0) {
                PC = operand;
                break;
            }
            PC++;
            break;

        case 23: //Push return address onto stack, jump to the address
            PC++;
            operand = readFromMemory(pw, is, reader, os, PC);
            //Push value
            SP--;
            pw.printf("WRITE:" + SP + ":" + (PC + 1) + "\n");
            pw.flush();

            USER_STACK_TOP = SP;
            PC = operand;
            break;

        case 24: // Pop return address from the stack, jump to the address
            //Pop value
            int tmp = readFromMemory(pw, is, reader, os, SP);
            pw.printf("WRITE:" + SP + ":" + 0 + "\n");
            pw.flush();
            SP++;
            operand = tmp;

            PC = operand;
            break;

        case 25: //Increment the value in X
            X++;
            PC++;
            break;

        case 26: //Decrement the value in X
            X--;
            PC++;
            break;

        case 27: // Push AC onto stack
            //Push value
            SP--;
            pw.printf("WRITE:" + SP + ":" + AC + "\n");
            pw.flush();
            PC++;
            break;

        case 28: // Pop from stack into AC
            //Pop value
            tmp = readFromMemory(pw, is, reader, os, SP);
            pw.printf("WRITE:" + SP + ":" + 0 + "\n");
            pw.flush();
            SP++;
            AC = tmp;
            PC++;


            break;

        case 29: //Perform system call

            processingInterrupt = true;
            userMode = false; //In Kernel mode

            operand = SP;
            SP = 2000;
            //Push value
            SP--;
            pw.printf("WRITE:" + SP + ":" + operand + "\n");
            pw.flush();

            operand = PC + 1;
            PC = 1500;
            //Push value
            SP--;
            pw.printf("WRITE:" + SP + ":" + operand + "\n");
            pw.flush();

            break;

        case 30: //Return from system call
            //Pop value
            tmp = readFromMemory(pw, is, reader, os, SP);
            pw.printf("WRITE:" + SP + ":" + 0 + "\n");
            pw.flush();
            SP++;
            PC = tmp;
            //Pop value
            tmp = readFromMemory(pw, is, reader, os, SP);
            pw.printf("WRITE:" + SP + ":" + 0 + "\n");
            pw.flush();
            SP++;
            SP = tmp;

            userMode = true;
            instruction_num++;
            processingInterrupt = false;
            break;

        case 50: //End Execution
            System.exit(0);
            break;

        default:
            System.out.println("Instruction not found - DEFAULTED, EXITING");
            System.exit(0);
            break;

        }
    }

}
