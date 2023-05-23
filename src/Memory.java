package src;

public class Memory {


    private MemoryWord[] Memory = new MemoryWord[40];

    boolean p1;
    boolean p2;

    public Memory() {
        p1=false;
        p2=false;
    }

    public MemoryWord[] getMemory() {
        return Memory;
    }

    public void setMemory(MemoryWord[] memory) {
        Memory = memory;
    }


    public int hasSpace() //returns -1 if no empty spaces
    {
        if(Memory[15]==null )//&& taken[26]==false) //first slot of process is empty + maybe 26 wont be used
            return 15;
        else if(Memory[27]==null)// && taken[39]==false) //second slot of process is empty + maybe 39 wont be used
            return 27;
        else
            return -1; //both slots are occupied
    }

    public int insertIntoMemoryCheck() // returns -1 if no space and should be handled inside which one to remove, else returns position
    {
        //if(hasSpace()>-1)
        return hasSpace();
        // else // need to check if there is space outside and checking which process to pre empt

    }
    public void insertIntoMemory(int position, Process p) //position read from pcv attribute?
    { //TODO handle program attribute in process for this logic to work

        String program=p.getProgram(); //gets the string file of the instructions
        String[] programSplit= program.split("\n");

        for(int i=position; i<program.length();i++)
        {

            Memory[i]= new MemoryWord("",programSplit);

        }

        switch(P.getProcessOrder) ////Fix memory bounds in PCB
        {
            case 1:
                Memory[3]=p.getMemoryStart();
                Memory[4]=p.getMemoryStart()+12;
                break;
            case 2:
                Memory[8]=p.getMemoryStart();
                Memory[9]=p.getMemoryStart()+12;
                break;
            case 3:
                Memory[13]=p.getMemoryStart();
                Memory[14]=p.getMemoryStart()+12;
                break;


        }

    public void removeFromMemory(Process p) //deletes process from memory by nullifying its segment
    {
            int position = p.getP(); //returns P1 or P2
            int begin=P.getMemoryStart(); //reutrns memory bound 1
            int end=begin+ 12; //
            for(int i=begin;i<=end;i++) //nullifies position
            {
                Memory[i]=null;
            }
            switch(P.getProcessOrder) //nullify memory bounds in PCB
            {
                case 1:
                    Memory[3]=null;
                    Memory[4]=null;
                    break;
                case 2:
                    Memory[8]=null;
                    Memory[9]=null;
                    break;
                case 3:
                    Memory[13]=null;
                    Memory[14]=null;
                    break;

            }
    }
}
