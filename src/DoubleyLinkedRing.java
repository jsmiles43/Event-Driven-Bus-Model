import java.util.Arrays;

import flanagan.math.*; //I have imported the flanagan java library from http://www.ee.ucl.ac.uk/~mflanaga/java/
//to use its exponentially distributed random number generation
public class DoubleyLinkedRing {
  double clock;	//the main clock for keeping time
  private Event root; 
  public int size;
  int[] queue;     //bus stop queues
  int[] additiveQueueSizePerPeopleEvent;   //total number of people at each stop    
  int[] peopleEventCount;     //number of peoples events per stop
  int[] max; 
  int[] min;
  PsRandom rand;         //for exponential random generator
  int numberOfBusStops = 0, numberOfBuses = 0;
  double meanInterArrivalRate = 0, boardingTime = 0, simTime = 0, timeBetweenBusStops = 0;
  
  //The constructor for a doubly linked ring
  DoubleyLinkedRing(int _busStops, int _numberOfBuses, double _meanInterArrivalRate, double _boardingTime, double _simTime, double _timeBetweenBusStops) {
	  clock = 0;
	  numberOfBusStops = _busStops; 
	  numberOfBuses = _numberOfBuses;
	  meanInterArrivalRate = _meanInterArrivalRate;
	  //System.out.println(boardingTime);
	  boardingTime = _boardingTime;
	  simTime = _simTime;
	  timeBetweenBusStops = _timeBetweenBusStops;
	  queue = new int[_busStops];
	  max = new int[_busStops];
	  min = new int[_busStops];
	  Arrays.fill(min, 0);
	  additiveQueueSizePerPeopleEvent = new int[_busStops];
	  peopleEventCount = new int[_busStops];
	  root = null;
	  rand = new PsRandom();
  }

  public int Size() {
	  return size;
  }
  
  public void addEvent(Event e){     //add an event to the linked ring in order of time
	if (root == null) {				//earliest events are arranged clockwise with the root at 12:00
		root = e; 					//latest event will be to the left of the root in other words
		root.prev = root;
		root.next = root;
	}
    else {
    	Event current = root;
    	while (current.next.time <= e.time && current.next != root) {
    		current = current.next;
    	}
    	e.prev = current;
    	e.next = current.next;
    	current.next = e; 
    	e.next.prev = e;
    	
    }
	size++;
  }

  public Event deleteEvent(){ 		//delete and process event while updating clock
    occurEvent(root.next);           //generate more events after occurring event in linked list 
	Event temp = root.next;
	root.next = temp.next;
	temp.next.prev = root;
	temp.prev = null;
	temp.next = null;
	size--;
	return temp;
  }
  
  public void occurEvent(Event occurence) {			//processes an event by changing queue sizes and generating
	  this.clock = occurence.time;					//new events with appropriate event time based on BusSimluationData.txt parameters
	  switch(occurence.type) {
	  case PERSON:
		  queue[occurence.stopNumber - 1]++;
		  double x = rand.nextExponential(0, meanInterArrivalRate);     //exponential random variable
		  generateEvent(clock + meanInterArrivalRate, Event.Type.PERSON, occurence.stopNumber, 0);
		  break;
	  case ARRIVAL:
		  if (queue[occurence.stopNumber - 1] == 0) 
			  generateEvent(clock + timeBetweenBusStops, Event.Type.ARRIVAL, (occurence.stopNumber % numberOfBusStops) + 1, occurence.busNumber); //generate arrival event
		  else 
			  generateEvent(clock, Event.Type.BOARDER, occurence.stopNumber, occurence.busNumber); //generate boarder event
		  break;
	  case BOARDER:
		  queue[occurence.stopNumber - 1]--;
		  if (queue[occurence.stopNumber - 1] == 0)
			  generateEvent(clock + timeBetweenBusStops, Event.Type.ARRIVAL, (occurence.stopNumber % numberOfBusStops) + 1, occurence.busNumber);
		  else 
			  generateEvent(clock + boardingTime, Event.Type.BOARDER, occurence.stopNumber, occurence.busNumber);
		  break;
	  default:
		  break;
	  } 
  }
  
  public void generateEvent(double eventTime, Event.Type type, int _stopNumber, int _busNumber) {    //generate event function
	  Event event = new Event(eventTime, type, _stopNumber, _busNumber);
	  addEvent(event);
  }
  
}