
package elevatorthread;

import elevatorthread.ElevatorController;
import elevatorthread.Elevator;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import elevatorthread.Passengers;
/**https://github.com/Chaklader/Multi-threading/tree/master/7_Concurrency_Models
https://github.com/Chaklader/Multi-threading/tree/master/Callable_And_Future/Concurrency_synchronization
https://github.com/Chaklader/Multi-threading/tree/master/Callable_And_Future/Executors_thread-pool**/
/**
 *
 * @author cls
 */
public class ElevatorMain {

    
    

    public static void main(String[] args) throws InterruptedException {
        ElevatorController manager1 = new ElevatorController();
      
        /*ElevatorController manager2 = new ElevatorController();
        ElevatorController manager3 = new ElevatorController();
        ElevatorController manager4 = new ElevatorController();
        ElevatorController manager5 = new ElevatorController();*/
        
        
  
        Elevator  elevator1 = new Elevator(manager1,true);
        /*Elevator  elevator2 = new Elevator(manager2,true);
        Elevator  elevator3 = new Elevator(manager3,true);
        Elevator  elevator4 = new Elevator(manager4,true);
        Elevator  elevator5 = new Elevator(manager5,true);*/

        Passengers[] people=new Passengers[30];
        ExecutorService executor = Executors.newFixedThreadPool(30);

	  // create 100 random people
	  
	  for (int i=0; i<30; i++){
		  people[i] = new Passengers(manager1);
                  //people[i].start();
              
                  
                 
                  

	  }
      
	  /*for (int i=20; i<40; i++){
		  people[i] = new Person(manager2);

	  }
           for (int i=40; i<60; i++){
		  people[i] = new Person(manager3);

	  }
           for (int i=60; i<80; i++){
		  people[i] = new Person(manager4);

	  }
             for (int i=80; i<100; i++){
		  people[i] = new Person(manager5);

	  }*/
	  for(int i = 0; i<30; i++){
		  executor.submit(people[i]);
	  }

	   elevator1.start();
       
         
	  executor.shutdown();
          
          executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
          //manager.printElevatorState();
          
          
    }


    
    
     
 
        
    }
 

  
    
    

