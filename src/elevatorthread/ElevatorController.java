
package elevatorthread;

/**https://github.com/pharshal/MultithreadedElevator/blob/master/src/main/java/ElevatorController.java**/
/**
 *
 * @author cls
 */

import java.util.concurrent.*;
import  java.util.concurrent.atomic.*; 
import java.util.*; 
import java.io.*; 
import elevatorthread.ElevatorMain;
import elevatorthread.Passengers;
import static java.lang.Math.random;


public class ElevatorController {
    public boolean goingUp;
    private ElevatorMain m;
    public boolean goingDown;
    public boolean goingExitUp;
    public boolean goingExitDown;
    public int currentFloor; 
    public int currentTime; 
    public int currentNumPeople; 
    private static final AtomicInteger elevatorNumberGenerator = new AtomicInteger(1);  
    public int elevatorNumber;
    public ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Passengers>> waiting; 
    public ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Passengers>> request; 
    public ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Passengers>> inElevator;
    public ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Passengers>> waitingExit; 
    public ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Passengers>> requestExit; 
    public ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Passengers>> inElevatorExit;
    private int maxNumber;
    private int queque;
    private int totalRequest;
    private int totalLoadedPassengers;
    private int totalUnloadedPassengers;
    private int[] unloadedPassengers;
    private int maxExit;
    private int newFloor;
  
    private int exit;
    private int exitNumPeople;
    private Passengers person;
   
   

    Random random=new Random();
    
    
    
    public ElevatorController(){
          
		this.goingUp = true;
                this.goingExitDown=true;
                this.goingExitUp=false;
                this.goingDown = false; 
		this.newFloor=random.nextInt((5-1)+1)+1;
                this.exitNumPeople=0;
                totalLoadedPassengers = 0;
                totalUnloadedPassengers = 0;
                unloadedPassengers = new int[5];
		this.request = new ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Passengers>>();
		this.waiting = new ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Passengers>>();
		this.inElevator = new ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Passengers>>();
                this.requestExit = new ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Passengers>>();
		this.waitingExit = new ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Passengers>>();
		this.inElevatorExit = new ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Passengers>>();
		this.currentFloor = 0; 
                this.currentTime = 0; 
                this.currentNumPeople=0;
                this.maxNumber=10;
                this.maxExit=5;
                this.totalRequest=0;
                
	
		this.elevatorNumber = elevatorNumberGenerator.getAndIncrement(); 

	}


	public synchronized void addPerson(Passengers person, int floor, ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Passengers>> hashmap){
		ConcurrentLinkedQueue<Passengers> tmp = new ConcurrentLinkedQueue<Passengers>(); 
		if (hashmap.containsKey(floor)){
			tmp = hashmap.get(floor);
			tmp.add(person);
			hashmap.put(floor, tmp); 
		}
		else{
			tmp.add(person); 
			hashmap.put(floor, tmp); 
			
		}
		
	}
       
        public synchronized void addPersonExit(Passengers person, int floor, ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Passengers>> hashmap){
		ConcurrentLinkedQueue<Passengers> tmp = new ConcurrentLinkedQueue<Passengers>(); 
		if (hashmap.containsKey(floor)){
			tmp = hashmap.get(floor);
			tmp.add(person);
			hashmap.put(floor, tmp); 
		}
		else{
			tmp.add(person); 
			hashmap.put(floor, tmp); 
			
		}
		
	}
        
	
	public synchronized void addPersonFirst(Passengers person, int floor, ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Passengers>> hashmap){
		ConcurrentLinkedQueue<Passengers> oldQueue = new ConcurrentLinkedQueue<Passengers>(); 
	    ConcurrentLinkedQueue<Passengers> newQueue = new ConcurrentLinkedQueue<Passengers>(); 
		if (hashmap.containsKey(floor)){
			oldQueue = hashmap.get(floor); 
			newQueue.add(person); 
			for (Passengers p : oldQueue){
				newQueue.add(p);  
			}
			hashmap.put(floor, newQueue); 
		}
		else{
			oldQueue.add(person); 
			hashmap.put(floor, oldQueue); 
			
		}
		
	}	

	
   
	public synchronized void addPersonFirstExit(Passengers person, int floor, ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Passengers>> hashmap){
		ConcurrentLinkedQueue<Passengers> oldQueue = new ConcurrentLinkedQueue<Passengers>(); 
	    ConcurrentLinkedQueue<Passengers> newQueue = new ConcurrentLinkedQueue<Passengers>(); 
		if (hashmap.containsKey(floor)){
			oldQueue = hashmap.get(floor); 
			newQueue.add(person); 
			for (Passengers p : oldQueue){
				newQueue.add(p);  
			}
			hashmap.put(floor, newQueue); 
		}
		else{
			oldQueue.add(person); 
			hashmap.put(floor, oldQueue); 
			
		}
		
	}
	
        
	
	public synchronized Passengers removePass(int floor, ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Passengers>> hashmap) {
		ConcurrentLinkedQueue<Passengers> tmp = new ConcurrentLinkedQueue<Passengers>(); 
		if (hashmap.containsKey(floor)){
			tmp = hashmap.get(floor);
			Passengers personRemoved = tmp.poll();
			hashmap.put(floor, tmp); 
			if (hashmap.get(floor).isEmpty()){
				hashmap.remove(floor); 
			}
			
			return personRemoved; 

		}
		return null; 


		
	}
        
	public synchronized Passengers removePassExit(int floor, ConcurrentHashMap<Integer,ConcurrentLinkedQueue<Passengers>> hashmap) {
		ConcurrentLinkedQueue<Passengers> tmp = new ConcurrentLinkedQueue<Passengers>(); 
		if (hashmap.containsKey(floor)){
			tmp = hashmap.get(floor);
			Passengers personRemoved = tmp.poll();
			hashmap.put(floor, tmp); 
			if (hashmap.get(floor).isEmpty()){
				hashmap.remove(floor); 
			}
			
			return personRemoved; 

		}
		return null; 


		
	}
	

    
    
    
    public  synchronized void acceptRequest(){
	   Passengers personWaiting = removePass(currentTime, request); 
	   while(personWaiting != null){
		   addPerson(personWaiting,personWaiting.getArrivalFloor(), waiting); 
		   removePass(personWaiting.getArrivalFloor(), request);
		   personWaiting  =   removePass(currentTime, request); 
	   }
  
	}
   
    public  synchronized void acceptRequestExit(){
	   Passengers personWaiting = removePass(currentTime, requestExit); 
	   while(personWaiting != null){
		   addPerson(personWaiting,personWaiting.getArrivalFloorExit(), waitingExit); 
		   removePass(personWaiting.getArrivalFloorExit(),requestExit );
		   personWaiting  =   removePass(currentTime, requestExit); 
	   }
    }
       
   	
	public synchronized void makeRequest(Passengers person){
		addPerson(person,person.getArrivalTime(), request); 

		
	}
        public synchronized void makeRequest2(Passengers person){
		addPersonExit(person,person.getArrivalTime(), requestExit); 

		
	}

    
    public synchronized void changeFloor(int minFloor,int maxFloor) throws InterruptedException{
    

   
     if(!(inElevator.isEmpty())){
       int max = Collections.max(inElevator.keySet());
       if(max < currentFloor){
		   this.goingUp = false;
		   this.goingDown = true;                   
	   }
	   if(max>currentFloor){
		   this.goingUp = true;
		   this.goingDown = false;  
	   }
       
       }
       
       else{

	      if(!(waiting.isEmpty()) && goingUp == true){
			 int max = Collections.max(waiting.keySet());
			 if (max < currentFloor){
				this.goingUp = false;
				this.goingDown = true; 
			 }
		  }

	   }

  
      if(!(waiting.isEmpty()) || !(inElevator.isEmpty()) ){
		  
		 String elevatorFloorString =  String.format("Elevator %d is on floor %d\n",this.elevatorNumber,this.currentFloor);
		 
		
		 writeOutput(elevatorFloorString); 
		 
		
	    System.out.printf(elevatorFloorString);
	    
	    
		if (goingUp == true){
			   
			   if(this.currentFloor == maxFloor){
				    currentFloor--;
                                    
				    goingUp = false; 
				    goingDown = true; 			    
			   }
		       else{
				   currentFloor++;
                                   
				}
	       }
			
		   else{
			   
			   if(currentFloor == minFloor){
			            goingUp = true; 
				    goingDown = false; 	
				    currentFloor++;
                                    
		   
				 }
				else{
			         currentFloor--;
                                 
                 }
			 } 
			 
		}
    
        
         

    
                    //while(this.request.size()==0){
                       if (waiting.isEmpty() && inElevator.isEmpty()){
                           System.out.println("\n\nElevator:"+this.elevatorNumber+"\nmode:"+"Idle"+"\ndestination:"+this.currentFloor+"\ncapacity:"+this.maxNumber+"\ncount_inside:0"+"\ninside:"+"[]");
		        //String sleepingOutput = String.format("Elevator %d is Idle on floor %d\n",this.elevatorNumber, this.currentFloor);
		        /*writeOutput(sleepingOutput); 
                        
				System.out.printf(sleepingOutput);*/
                    
                     }
                    //this.writeStates();
                      
                    //}
                    this.currentTime++;

    }
    public synchronized void changeFloor0(int minFloor,int maxFloor) throws InterruptedException{
   
     if(!(inElevatorExit.isEmpty())){
       int max = Collections.max(inElevatorExit.keySet());
       if(max < newFloor){
		   this.goingExitUp = false;
		   this.goingExitDown = true;                   
	   }
	   if(max>newFloor){
		   this.goingExitUp = true;
		   this.goingExitDown = false;  
	   }
       
       }
       
       else{

	      if(!(waitingExit.isEmpty()) && goingExitUp == true){
			 int max = Collections.max(waitingExit.keySet());
			 if (max < newFloor){
				this.goingExitUp = false;
				this.goingExitDown = true; 
			 }
		  }

	   }

      if(!(waitingExit.isEmpty()) || !(inElevatorExit.isEmpty()) ){
		  
		 /*String elevatorFloorString =  String.format("Elevator %d is on floor %d\n",this.elevatorNumber,this.currentFloor);
		 
		
		 writeOutput(elevatorFloorString); 
		 	
	    System.out.printf(elevatorFloorString);*/	 
	    
	 
		if (goingExitUp == true){
			   
			   if(this.newFloor == maxFloor){
				    newFloor--;
                                    
				    goingExitUp = false; 
				    goingExitDown = true; 			    
			   }
		       else{
				   newFloor++;
                                   
				}
	       }
			
		   else{
			   
			   if(newFloor == minFloor){
			            goingExitUp = true; 
				    goingExitDown = false; 	
				    newFloor++;
                                    
		   
				 }
				else{
			         newFloor--;
                                 
                 }
			 } 
			 
		}
    
 
    
                    while(this.requestExit.size()==0){
                       if (waitingExit.isEmpty() && inElevatorExit.isEmpty()){
                           System.out.println("\n\nElevator:"+this.elevatorNumber+"\nmode:"+"Idle"+"\nFloor:"+this.newFloor+"\ncapacity:"+this.maxNumber+"\ncount_inside:0"+"\ninside:"+"[]");
		        /*String sleepingOutput = String.format("Elevator %d is Idle on floor %d\n",this.elevatorNumber, this.newFloor);
		        writeOutput(sleepingOutput); 
                        
				System.out.printf(sleepingOutput); */
                     
                              
                               
                               
       
                       }
                       //break;
                      
                    }
                    this.currentTime++;
      
    }
 
public synchronized void enterElevatorDest() throws InterruptedException{
	  
    
		
	   boolean headingPrinted = false; 

           
           Passengers personEntering = removePassExit(newFloor, waitingExit);
       
           
	   
       while(personEntering!=null){
		        
		            Random random=new Random();
				// print heading 
		   	    if (!(headingPrinted)){
					String enteringElevator = String.format("Elevator %d Stopping on floor %d for people\n**************************\n",this.elevatorNumber,this.newFloor);
					writeOutput(enteringElevator);
					System.out.print(enteringElevator); 
					headingPrinted = true; 
				}
		
                                if(this.inElevatorExit.size()>maxNumber){
                                    
                                    addPersonFirstExit(personEntering, newFloor, waitingExit); 
				    String numberError = String.format("**********************************************************\nElevator:%d is full",this.elevatorNumber); 
					
                                     writeOutput(numberError);
			           System.out.print(numberError);
                                   break;
                                    
                                }
                               
                                
                                addPersonExit(personEntering,personEntering.getDestExit(),inElevatorExit);
                         
                                System.out.println("Floor:"+newFloor);
                               
                                System.out.println("\n\nTime"+this.currentTime+"\nElevator:"+this.elevatorNumber+"\nMode:working"+"\ndirection:down"+"\nFloor:"+newFloor+"\nDestination"+personEntering.getDestExit()+"\nMax:"+maxNumber+"\nInside:"+this.inElevatorExit.size());
                            
                                System.out.print(personEntering.toStringDest());
                         
				writeOutput(personEntering.toStringDest()); 
			
		        personEntering = removePassExit(newFloor, waitingExit); 
                        

	   
          }
          


		
	}

   public synchronized void exitElevatorDest() throws InterruptedException{
    
     Passengers  personLeaving = removePassExit(newFloor, this.inElevatorExit);
    
       
      boolean headingPrinted = false; 
      
      while(personLeaving!=null){
          
		
                 
		 if(!(headingPrinted)){
                        
			String exitingElevator = String.format("*******************************\nLetting %s people out on  floor %d from Elevator %d\n",personLeaving.toString(),personLeaving.getDestExit(),this.elevatorNumber,newFloor); 
			Thread.sleep(1000);
                        writeOutput(exitingElevator); 
			System.out.print(exitingElevator); 
                        
			headingPrinted = true; 
                        
	     }
                 if(this.inElevatorExit.size()>maxExit){
                                   
                                    addPersonFirstExit(personLeaving, newFloor, waitingExit); 
				    String numberError = String.format("\nElevator:%d is max exit person\n",this.elevatorNumber,this.maxExit); 
					
                                     writeOutput(numberError);
			           System.out.print(numberError);
                                   break;
                                    
                                }
           
              
              System.out.println("\n\nTime"+this.currentTime+"\nElevator:"+this.elevatorNumber+"\nMode:working"+"\nFloor:"+newFloor+"\nDestination:"+personLeaving.getDestExit()+"\nMax:"+maxNumber+"\nInside:"+inElevatorExit.size());                  
	
	    personLeaving= removePassExit(newFloor,inElevatorExit); 
	  
     	
       }
    
}
  

    public synchronized void enterElevator() throws InterruptedException{
		
		

           
           
	   boolean headingPrinted = false; 

           
           Passengers personEntering = removePass(currentFloor, waiting);
           
       
       while(personEntering!=null){
		        
		            Random random=new Random();
			  
		   	    if (!(headingPrinted)){
					String enteringElevator = String.format("Elevator %d Stopping on floor %d for people\n**************************\n",this.elevatorNumber,this.currentFloor);
                                        Thread.sleep(500);
					writeOutput(enteringElevator);
					System.out.print(enteringElevator); 
					headingPrinted = true; 
				}
			
                            
                                if(this.inElevator.size()>maxNumber){
                                   
                                    addPersonFirst(personEntering, currentFloor, waiting); 
				    String numberError = String.format("\nElevator:%d is full\n",this.elevatorNumber); 
					
                                     writeOutput(numberError);
			           System.out.print(numberError);
                                   break;
                                    
                                }
                       
                                addPerson(personEntering,personEntering.getDestinationFloor(),inElevator);
                               
                                
                               
                                this.queque=this.request.size();
                                System.out.println("floor:"+this.currentFloor);
                                //System.out.println("Queque:"+queque);
                                System.out.println("\n\nTime:"+this.currentTime+"\nElevator:"+this.elevatorNumber+"\nMode:working"+"\ndirection:up"+"\nFloor:"+currentFloor+"\nDestination:"+personEntering.getDestinationFloor()+"\nCount_capacity:"+maxNumber+"\nInside:"+this.inElevator.size());
            
                               
                                
                                System.out.print(personEntering);
                             
				writeOutput(personEntering.toString()); 
			
                        
		        personEntering = removePass(currentFloor, waiting); 
                        

	   
          }
          


		
	}

   public synchronized void exitElevator() throws InterruptedException{
     
     Passengers  personLeaving = removePass(currentFloor, this.inElevator);
     
      boolean headingPrinted = false; 
      
      while(personLeaving!=null){
          
		
                 
		 if(!(headingPrinted)){
			String exitingElevator = String.format("\nLetting %s people out on  floor %d from Elevator %d\n",personLeaving.toString(),currentFloor,this.elevatorNumber); 
			
                        writeOutput(exitingElevator); 
			System.out.print(exitingElevator); 
			headingPrinted = true; 
	     }
                 if(this.inElevator.size()>maxExit){
                                   
                                    addPersonFirst(personLeaving, currentFloor, waiting); 
				    String numberError = String.format("\nElevator:%d is max exit person\n",this.elevatorNumber,this.maxExit); 
					
                                     writeOutput(numberError);
			           System.out.print(numberError);
                                   break;
                                    
                 }
      
               System.out.println("\n\nTime:"+this.currentTime+"\nElevator:"+this.elevatorNumber+"\nMode:working"+"\nFloor:"+currentFloor+"\nCount_capacity:"+maxNumber+"\nInside"+this.inElevator.size());                         
	     
       
	    personLeaving= removePass(currentFloor,inElevator); 
	  
     	
       }
    
}

   public synchronized void transferPeople(ElevatorController maxRequest, ElevatorController backUp) {

	   String faultWarning =String.format("\n\n**********MAX REQUEST PEOPLE BEING TRANSFERRED TO  ELEVATOR***********\n**********Starting backup elevator from floor 0************************\n"); 
	   System.out.println(faultWarning);
	   writeOutput(faultWarning);   
	
	   
           backUp.request=maxRequest.request;
	   backUp.waiting = maxRequest.waiting;
	   backUp.currentTime = maxRequest.currentTime; 
	   
	   
	   for ( Map.Entry<Integer,ConcurrentLinkedQueue<Passengers>> entry : inElevator.entrySet()) {
		  for (Passengers person : entry.getValue()){
			  addPersonFirst(person, currentFloor,backUp.waiting);
                          //System.out.println("\nelevator"+elevatorNumber+" active");
			  /*String backupWarning = String.format("%d person request %d den buyuk yeni \n", this.currentNumPeople, this.currentFloor);
			  writeOutput(backupWarning); 
			  System.out.print(backupWarning);*/
		  }
	   }

	   
   }
  
 private void incPassengers(int num) {
        currentNumPeople += num;
        totalLoadedPassengers += num;
    }
    
    // Update the number of passengers and total unloaded passengers
    private void decPassengers(int num) {
        currentNumPeople -= num;
        totalUnloadedPassengers += num;
    }
    
    private void updateUnloadedPassengers(int floor, int num) {
        unloadedPassengers[floor] += num;
    }
   

    public void setTotalLoadedPassengers(int totalLoadedPassengers) {
        this.totalLoadedPassengers = totalLoadedPassengers;
    }


    public void setTotalUnloadedPassengers(int totalUnloadedPassengers) {
        this.totalUnloadedPassengers = totalUnloadedPassengers;
    }

  
 
   
  
	public synchronized int getTime(){
		return this.currentTime;
	}
	
	
	public synchronized void writeOutput(String outputString) {
		try{
			  
			
			 File file =new File("output.txt");
    	     if(!file.exists()){
    	 	     file.createNewFile();
    	    }
			  FileWriter fileWritter = new FileWriter(file.getName(),true);
			  BufferedWriter bw = new BufferedWriter(fileWritter);
              bw.write(outputString);
              bw.close();
    	  
    	  }
    	 catch(IOException e){
    	   System.out.println("Exception occurred:");
    	   e.printStackTrace();
      }
		
	}

 
    public int getTotalRequest() {
        return totalRequest;
    }

    /**
     * @param totalRequest the totalRequest to set
     */
    public void setTotalRequest(int totalRequest) {
        this.totalRequest = totalRequest;
    }



   
    
}






