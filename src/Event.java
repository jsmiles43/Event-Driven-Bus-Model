public class Event {       //the event class with types person, arrival, boarder, and dummy for the initial event 
	public double time;	//with clock = 0
	
	public enum Type {
		PERSON,ARRIVAL,BOARDER,DUMMY
	}
	static int eventNumber = 0;  
	public Type type;
	public int stopNumber;    //the stopNumber 
	public int busNumber;     //the busNumber set to 0 for person
	public Event prev;		//previous event in ring
	public Event next;		//next event in ring
	
	
	public Event(double _time, Type _type, int _stopNumber, int _busNumber) {
		this.time = _time;
		this.type = _type;
		this.stopNumber= _stopNumber;
		this.busNumber= _busNumber;
		eventNumber++;
	};
}
