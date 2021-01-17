
 
package elevatorthread;

/**
 *
 * @author cls
 */
import java.util.concurrent.*;
import java.util.*; 
import java.util.concurrent.atomic.*; 
import java.io.*; 
import java.util.logging.Level;
import java.util.logging.Logger;




   public class Elevator extends Thread{
   private int maxFloor;
   private int minFloor;
   private int faultTime;
   private int elevatorNumber; 
   private ElevatorController elevatorController;
   private Elevator backUpElevator; 
   public boolean createFault; 
   public boolean faulty;
   Random random = new Random(); 
   private Passengers p;
   public int totalRequest;
   private boolean suspended;

   public Elevator(ElevatorController elevatorController){
      this.elevatorController = elevatorController; 
      this.maxFloor = 5;
      this.minFloor = 0; 
      this.faulty =  false;
   }

   public Elevator(ElevatorController elevatorController, boolean createFault){
      this.elevatorController = elevatorController; 
      this.maxFloor = 5;
      this.minFloor = 0; 
      this.suspended=false;
      this.faultTime = random.nextInt((10-5)+1)+5;
      this.createFault = createFault; 
      this.faulty = false;
     
   } 

 
   
   
 
   
   
   public void run(){
   try{
	 
	   writeOutput(); 
	   
	   
			while(!(faulty)){
				{
		    
				// control the elevators actions
                                elevatorController.acceptRequest();
				elevatorController.enterElevator(); 	
				elevatorController.exitElevator();
                                elevatorController.changeFloor(minFloor,maxFloor);
                                
                                
                                maxRequest();
                                maxRequestExit();
                                
                  
                                Thread.sleep(200);
                                
                                
                                elevatorController.acceptRequestExit();
                                elevatorController.enterElevatorDest();
                                elevatorController.exitElevatorDest();
                                elevatorController.changeFloor0(minFloor, maxFloor);
                           
                                /*maxRequestExt();
                                maxRequestExitFloor();*/
                                   
                                Thread.sleep(200);
    
                          
			}
                     
		  }
    
	  }catch(Exception e2){
				
	e2.printStackTrace(); 
        }
	 
		  
	

   }


   	public synchronized void writeOutput(){
		 
		String start = "                                                ";					
		String text = " 						The elevator has started					"; 		
		System.out.println("The elevator has started:"+elevatorController.elevatorNumber);
                System.out.println("\nActive");
                String headElevator = String.format("%s\n%s\n%s\n",start, text,start);

    	  
    	  try{
			  
			
			 File file =new File("output.txt");
    	     if(!file.exists()){
    	 	     file.createNewFile();
    	    }
			  FileWriter fileWritter = new FileWriter(file.getName(),true);
			  BufferedWriter bw = new BufferedWriter(fileWritter);
              bw.write(headElevator);
              bw.close();
    	  
    	  }
    	 catch(IOException e){
    	   System.out.println("Exception occurred:");
    	   e.printStackTrace();
      }
		
	}

   public synchronized void maxRequest() throws InterruptedException  {
	
	   if(elevatorController.request.size()>20&&elevatorController.request.size()>0 && createFault == true&&Thread.activeCount()<=5){
				
				ElevatorController Controller2 = new ElevatorController();
                                
				elevatorController.transferPeople(elevatorController, Controller2); 
			        backUpElevator = new Elevator(Controller2,true);
				backUpElevator.start();
                                
                                try{
                                    
                                    Passengers[] people=new Passengers[10];
                                    ExecutorService executor = Executors.newFixedThreadPool(10);
                                    for (int i=0; i<10; i++){
                                        people[i] = new Passengers(Controller2);
                                        //people[i].start();
        
	                              }
   
	                            for(int i =0; i<10; i++){
                                        
		                        executor.submit(people[i]);
	                           }

	                           executor.shutdown();
                                   executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
                                   Thread.sleep(500);
                                    
                                }catch(InterruptedException e1){
                                }
                                createFault=false;
                             
                                //Controller2.printBuildState();
                                //resumeElevator(backUpElevator);
                                
                               // backUpElevator.resumeElevator();
			       //stopCurrentThread(); 
				
		}
           

	  
	   
   }
      public synchronized void maxRequestExit() throws InterruptedException  {
	   
	
	   if(elevatorController.request.size()<20&& elevatorController.request.size()>0&& createFault == true&&elevatorController.elevatorNumber!=1){
				// create a backup elevator 
			
                        stopCurrentThread();
                        System.out.println("Elevator:"+elevatorController.elevatorNumber+"Pasive");
                        //suspended(backUpElevator);
                        //backUpElevator.interrupt();
				
		}

	  
	   
   }
      

   public synchronized void stopCurrentThread(){
	    this.faulty=true; 
   }

   public synchronized void sleepEmptyExit() throws InterruptedException{
            
            synchronized(this){
				
				while(elevatorController.request.isEmpty()){
					wait(); 
				}
				
				notifyAll(); 
			}

   }

}