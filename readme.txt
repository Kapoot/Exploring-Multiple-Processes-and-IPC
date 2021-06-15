Name:   Dylan Kapustka
NetID:  dlk190000

Project #1   (Contains the Following Java Files):
Main.java    This class creates a new CPU object and tells it to start processing.
CPU.java     This class simulates a CPU and processes instructions.
Memory.java  This class simulates Memory and can read or write based on CPU instructions. 

Design Info:
IDE Used: VS Code (V.1.54)
Java Version (15.0.2)

CMD Commandline (Execution):
javac *.java
java Main.java sample1.txt 30
java Main.java sample2.txt 30
java Main.java sample3.txt 30
java Main.java sample4.txt 30
java Main.java sample5.txt 30

Expected Output:
----------------------
sample1.txt:
ABCDEFGHIJKLMNOPQRSTUVWXYZ12345678910

sample2.txt:
    ------
 /         \
/   -*  -*  \
|           |
\   \____/  /
 \         /
    ------

sample3.txt:
A
0
A
0
A
2
A
4
A
5
A
7
A
9
A
10
A
12
A
14

sample4.txt:
1000
999
1000
1998
1997
1998
Memory violation: accessing system address 1000 in user mode 

sample5.txt:
<(^u^)>
20+22=42
