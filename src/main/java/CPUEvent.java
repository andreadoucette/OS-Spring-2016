import java.util.Random;

public enum CPUEvent {
	TERMINATE(5),
	INTERRUPT(10),
	PAGE_FAULT(15),
	BLOCK_IO(20),
	OTHER(50);
	
	private static Random random = new Random();
	
	private int probability = 0;
	private CPUEvent(int probability) {
		this.probability = probability;
	}
	
	public static CPUEvent getCPUEvent() {
		int randomNum = random.nextInt(100);
		for (CPUEvent event : CPUEvent.values()) {
			if (randomNum < event.probability) {
				return event;
			} else {
				randomNum -= event.probability;
			}
		}
		return null;
	}
}
