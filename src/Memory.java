package src;

public class Memory {


    private MemoryWord[] Memory = new MemoryWord[40];
    // private boolean[] taken = new boolean[40];

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

    public boolean[] getTaken() {
        return taken;
    }

    public void setTaken(boolean[] taken) {
        this.taken = taken;
    }

    public int hasSpace() //returns -1 if no empty spaces
    {
        if(p1==false )//&& taken[26]==false) //first slot of process is empty + maybe 26 wont be used
            return 12;
        else if(p2==false)// && taken[39]==false) //second slot of process is empty + maybe 39 wont be used
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
    public void insertIntoMemory(int position, String  program) //position read from pcv attribute?
    { //TODO handle from outside the p1 or p2, as well as switching out

        String[] programSplit= program.split();
        if (position==12)
        {
            p1=true; //taken
        }
        else
        {
            p2=true;
        }
        for(int i=position; i<program.length();i++)
        {

            Memory[i]= new MemoryWord("",programSplit);

        }

    }

    public void removeFromMemory(int position, int programID) //make boolean false
    {

    }
}
