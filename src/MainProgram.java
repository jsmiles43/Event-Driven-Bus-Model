import java.util.Scanner;    
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class MainProgram {
	
	public static void main(String args[]) throws IOException {
		//read parameters from BusSimulationData.txt*************************
		
		int numberOfBusStops = 0, numberOfBuses = 0;
		double meanInterArrivalRate = 0, boardingTime = 0, simTime = 0, timeBetweenBusStops = 0;
		int stopsToNextBus = 0;
		try {  //reading bus simulation data from file***********************************
			File inputFile = new File("BusSimulationData.txt");
			Scanner input = new Scanner(inputFile);
			input.nextLine();
			while (input.hasNext()) {
				 numberOfBusStops = input.nextInt();
				 numberOfBuses = input.nextInt();
				 timeBetweenBusStops = input.nextInt();
				 meanInterArrivalRate = 1/input.nextDouble();
				 boardingTime = input.nextDouble()/60;
				 simTime = input.nextDouble() * 60;
				 stopsToNextBus = numberOfBusStops/numberOfBuses;
			}
			input.close();
		}
		catch(FileNotFoundException e) {
			throw e;
		}
		
		//************************************************************************
		//generate doubly linked ring for simulation
		DoubleyLinkedRing ring = new DoubleyLinkedRing(numberOfBusStops, numberOfBuses, meanInterArrivalRate, boardingTime, simTime, timeBetweenBusStops);
		
		//adding initial events****************************************************
		ring.addEvent(new Event(0, Event.Type.DUMMY, 0, 0));
		for (int stop = 1; stop <= numberOfBusStops; stop++)
			ring.addEvent(new Event(0, Event.Type.PERSON, stop, 0));
		for (int i = 1, j = 1; i <= numberOfBuses; i++, j=j+stopsToNextBus) {
			ring.addEvent(new Event(0, Event.Type.ARRIVAL, j, i));
		}
		//*********SIMULATION****************************************************
		
		while (ring.clock < simTime) {         //The SIMULATION loop
		
		
		Event e = ring.deleteEvent();
			if (e.type == Event.Type.ARRIVAL) {
				//System.out.println(ring.queue[0]);
				System.out.println(ring.clock);
				//System.out.println();
			}
			
			/*
			if (ring.clock > 420 && ring.clock < 480) {  //collect statistic data for appropriate time ranges (hour by hour)
				 if (e.type == Event.Type.PERSON) {      //counting person events and queue sizes at each person event to calculate average queue size/hour for each stop
					if (ring.queue[e.stopNumber - 1] > ring.max[e.stopNumber-1]) {ring.max[e.stopNumber - 1] = ring.queue[e.stopNumber - 1];}
					if (ring.queue[e.stopNumber - 1] < ring.min[e.stopNumber-1]) {ring.min[e.stopNumber - 1] = ring.queue[e.stopNumber - 1];}
					ring.additiveQueueSizePerPeopleEvent[e.stopNumber - 1] += ring.queue[e.stopNumber-1];
					ring.peopleEventCount[e.stopNumber - 1] += 1;
				
				}
				
			}
			*/
		
		}
		//queue statistic data*******************************************************
		String output = "\nQueue Averages, Max, and Min for eighth hour" + '\n';
		for (int i = 0; i < numberOfBusStops; i++) {
			//output += "Stop " + (i+1) + ": " + ring.additiveQueueSizePerPeopleEvent[i]/ring.peopleEventCount[i];
			output += " Max: " + ring.max[i];
			output += " Min: " + ring.min[i] + '\n';
		}
		
		
		
		//writing to a file with the queue stat information, I ran simulation several times taking stats at each hour and recording it in Simulationfile.txt
		try {
			File writeFile = new File("/users/josephmiles/SimulationData.txt");
			
			if (!writeFile.exists())
				writeFile.createNewFile();
			FileWriter writer = new FileWriter(writeFile.getAbsoluteFile(), true);
			
			writer.write(output);
			writer.close();
			
		}
		catch(IOException e) {
			throw e;
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
	}

	
	
	
	
	
	
	
	
	
}
