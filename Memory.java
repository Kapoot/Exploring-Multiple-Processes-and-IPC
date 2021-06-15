/*  
    Title:      Memory.java
    Name:       Dylan Kapustka (Dlk190000)
    Instructor: Professor Ozbirn
    Course:     CS 4348.001 - S21
    Date:       03/09/2021

    Description: This class simulates Memory and can read or write based on CPU instructions. 
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Memory
{   
    final static int [] memory = new int[2000]; //Implements memory
    
    public static void main(String args[])
    {
        try
        {
            //Scan our file
            Scanner reader = new Scanner(System.in);
            File file = null;

            if(reader.hasNextLine())
            {
                file = new File(reader.nextLine());
            }
            
            
        //Try to read the file
        //Initialize memory
        try 
        {
            Scanner scanner = new Scanner(file);
            String tmp;
            int instruction;
            int init = 0;

            //Read file and load instruction
            while(scanner.hasNext())
            {
                if(scanner.hasNextInt())
                {
                    instruction = scanner.nextInt();
                    memory[init++] = instruction;
                }
                else
                {
                    tmp = scanner.next();

                    //if token starts with ".", move counter
                    if(tmp.charAt(0) == '.')
                    {
                        init = Integer.parseInt(tmp.substring(1));
                    }
                    
                    //if token is comment or anything else, go to next line
                    else
                    {
                        scanner.nextLine();
                    }
                }
            }
            scanner.close();
        } 
        catch (FileNotFoundException ex) 
        {
            ex.printStackTrace();
        }

            String line; //holds scanner lines
            
            //This loop keeps reading instructions and performs read or write functions
            while(true)
            {
                if(reader.hasNext())
                {
                    line = reader.nextLine();
                    String[] tokens = line.split(":"); //split the line to get the necessary tokens
                    String instruction = tokens[0]; //tells if READ or WRITE

                    //If first token is 'READ', CPU is requesting to read
                    if(instruction.equals("READ"))    
                    {
                        //Send to CPU
                        System.out.println(memory[Integer.parseInt(tokens[1])]);
                    }
                    
                    //Else, CPU is requesting to write
                    else if(instruction.equals("WRITE"))
                    {
                        //Write to memory
                        int address = Integer.parseInt(tokens[1]);
                        int data = Integer.parseInt(tokens[2]);
                        memory[address] = data;
                    }

                    else 
                        break;
                }
                else
                    break;
            }

            reader.close();
            
        } catch(NumberFormatException e)
        {
            e.printStackTrace();
        }
    }
}