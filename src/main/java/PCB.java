import java.util.Random;

public class PCB {
	public static enum State {
		READY, RUNNING, WAITING
	}
	
	private static int nextId = 1;
	
	private static Random random = new Random ();
	
	private State state = State.READY;
	private int	id = nextId++;
	private int	cpuUsed = 0;
	private int	cpuMax = random.nextInt(50) + 1;
	private int	timeWaiting = 0;
	private int memNeeded = random.nextInt(50) + 25;
	private MemoryBlock memory;
	
	// constructor methods
	//default constructor

	@Override
	public String toString()
	{
		return "state: " + state
			+ "\tID: "		+ id
			+ "\tCPU used: "+ cpuUsed
			+ "\tCPU max: "	+ cpuMax				
			+ "\tWait: "	+ timeWaiting
			+ " \tMemory Needed: " + memNeeded
			+ (memory == null ? "\tNo memory allocated" : memory);
	}
	//	set methods

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCpuUsed() {
		return cpuUsed;
	}

	public void addCpuUsed(int cpuUsed) {
		this.cpuUsed += cpuUsed;
	}

	public int getCpuMax() {
		return cpuMax;
	}

	public void setCpuMax(int cpuMax) {
		this.cpuMax = cpuMax;
	}

	public int getTimeWaiting() {
		return timeWaiting;
	}

	public void addTimeWaiting(int timeWaiting) {
		this.timeWaiting += timeWaiting;
	}

	public int getMemNeeded() {
		return memNeeded;
	}

	public void setMemNeeded(int memNeeded) {
		this.memNeeded = memNeeded;
	}

	public MemoryBlock getMemory() {
		return memory;
	}

	public void setMemory(MemoryBlock memory) {
		this.memory = memory;
	}
}
