
package elevatorthread;
/**https://github.com/pratikbongale**/
/**
 *
 * @author cls
 */


import elevatorthread.ElevatorController;
import java.util.*;
import java.io.*; 
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public final class Passengers extends Thread{
    private static final AtomicInteger idGeneratorElevator = new AtomicInteger(1);   
    private int arrivalTime;
    public int id; 
    public int arrivalFloor;
    public int destinationFloor;
  
    private int passengerNum;
    private int requestNum;
    private int destExit;
    public int exitNumPassenger;
    private int arrivalFloorExit;
    private int capacity;
    private ArrayList<Integer>requestFloor=new ArrayList<>();
   
    
   
    Random random = new Random();
    private ElevatorController elevatorController;
   
    private ArrayList<Long>p=new ArrayList();
    //private ElevatorControllerGUI elevatorControllerGUI; 


    // constructor for the elevator without GUI 
    public Passengers(ElevatorController elevatorController){
         this.id =  idGeneratorElevator.getAndIncrement();
         this.passengerNum=setPassengerNum();
         this.arrivalTime = setArrivalTime();
         this.capacity=0;
         this.exitNumPassenger=setExitNumPassenger();
         
         this.arrivalFloor = 0;
         this.destExit=setDestExit();
         this.destinationFloor = setDestinationFloor();
         this.arrivalFloorExit=setArrivalFloorExit();
       
       
         this.random = new Random();
         this.elevatorController = elevatorController;
         String personalDetails = String.format("\nId:%d,ArrivalFloor:%d,Destination:%d",this.id,this.arrivalFloor,this.destinationFloor);
         System.out.println(personalDetails);
         
       
         
    }
    

    public int getPassengerNum() {
        return passengerNum;
    }

    public synchronized int setPassengerNum() {
        
        return random.nextInt(10-1)+1;
    }
    public int getExitNumPassenger() {
        return exitNumPassenger;
    }

    public synchronized int setExitNumPassenger() {
        
        return random.nextInt(5-1)+1;
    }


    public synchronized int setArrivalFloor(){
		//  random arrival floor
        return  random.nextInt((5-1)+1)+0;
    }
       public synchronized int setArrivalFloorExit(){
		//  random arrival floor
        return  random.nextInt((5-1)+1)+1;
    }
    
    public synchronized int setDestinationFloor(){
		// ensure arrival floor != destination floor
		int randomFloor = random.nextInt((5-1)+1)+0;
		while(this.arrivalFloor == randomFloor){
			randomFloor = random.nextInt((5-1)+1)+0;
		}
		
		return randomFloor; 
	}

	
	public synchronized int setArrivalTime() {
		
            return random.nextInt((100-1)+1)+1;
               
                

	}
      
    

  
    public synchronized long getId(){
		return this.id;
               
	}
  
    
    

    public synchronized int getDestinationFloor(){
		return  this.destinationFloor;
	}

	
	 public synchronized int getArrivalFloor(){
		return  this.arrivalFloor;
	}
          public synchronized int getArrivalFloorExit(){
		return  this.arrivalFloorExit;
	}
	
	    public synchronized int getArrivalTime(){
		return  this.arrivalTime;
	}

    public synchronized String toString(){
		// passengers details 
               
                
                String personalDetails = String.format("\nId:%d,ArrivalFloor:%d,Destination:%d",this.id,this.arrivalFloor,this.destinationFloor);
                return personalDetails;
               
       
}
   
     public synchronized String toStringDest(){
		// passengers details 
               
                
                String personalDetails = String.format("\nId:%d,ArrivalFloor:%d,Destination:%d",this.id,this.arrivalFloorExit,this.destExit);
                return personalDetails;
}
	

	
   // write to output.dat
   public synchronized void writeOutput(){
		String outputStr = String.format("Person (Thread ID) %s makes request at time %s starting at floor %s with the destination floor %s.\n", this.id, this.arrivalTime, this.arrivalFloor, this.destinationFloor); 

		

    	  
    	  try{
			  
			 File file =new File("request.txt");
    	     if(!file.exists()){
    	 	     file.createNewFile();
    	    }
			  FileWriter fileWritter = new FileWriter(file.getName(),true);
			  BufferedWriter bw = new BufferedWriter(fileWritter);
              bw.write(outputStr);
              bw.close();
    	  
    	  }
    	 catch(IOException e){
    	   System.out.println("Exception occurred:");
    	   e.printStackTrace();
      }
		
	}
    

    public void run(){
        try{
			writeOutput();
			
      
            
            elevatorController.makeRequest(this);
            
            elevatorController.makeRequest2(this);
            
        }
        catch(Exception e1){
            e1.printStackTrace();
			
		

        }

    }

    /**
     * @return the destExit
     */
    public synchronized int setDestExit() {
        return destExit=0;
    }
   public int getDestExit(){
       return destExit;
       
   }
    /**
     * @param destExit the destExit to set
     */
    /*public void setDestExit() {
        this.destExit = 0;
    }*/

    /**
     * @return the requestNum
     */
 

}



