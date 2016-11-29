import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class ProcessManagement {
	private static final int NUM_PROCESSES = 15;
	private static final int MEMORY_SIZE = 256;
	
	private static final Random random = new Random();
	
	private static PCB running = null;
	
	private static MemoryManager memoryManager;
	
	private static void terminate () {
		memoryManager.free(running.getMemory());
		running = null;
	}

	public static void main(String[] args) {
		// Pick a memory manager and then...
		System.out.println("Press 1 for first fit.");
		System.out.println("Press 2 for best fit.");
		System.out.println("Press 3 for worst fit.");
		System.out.print("Which fit would you like to use? ");
		Scanner scanner = new Scanner(System.in);
		switch (scanner.nextInt()) {
		case 1:
			memoryManager = new FirstFit(MEMORY_SIZE);
			break;
			
		case 2:
			memoryManager = new BestFit(MEMORY_SIZE);
			break;
			
		case 3:
			memoryManager = new WorstFit(MEMORY_SIZE);
			break;

		default:
			System.out.println("What are you doing?");
			scanner.close();
			return;
		}
		
		scanner.close();

		List<PCB> ready = new LinkedList<PCB>();
		for (int i = 0; i < NUM_PROCESSES; i++) {
			ready.add(new PCB());
		}
		
		System.out.println("\n*****\t\t\tReady Queue\t\t\t*****");
		ready.forEach(System.out::println);
		
		//While loop that goes through the Ready List
		while (!ready.isEmpty()) {
			
			//Set running to the first node which is removed from Ready List
			running = ready.remove(0);
			
			if (running.getMemory() == null) {
				int size = running.getMemNeeded();
				System.out.format("%nID: %s needs: %s%n", running.getId(), size);
				MemoryBlock memory = memoryManager.malloc(size);
				if (memory == null) {
					ready.add(running);
					continue;
				} else {
					running.setMemory(memory);
				}
			}

			int runtime = random.nextInt(20) + 1;
			running.setState(PCB.State.RUNNING);
			running.addCpuUsed(runtime);
			ready.forEach(pcb -> pcb.addTimeWaiting(runtime));
			
			System.out.println("\n*****\t\t\tRunning Process\t\t\t*****");
			System.out.println(running);
			
			System.out.println("\n*****\t\t\tMemory Report\t\t\t*****");
			System.out.println(memoryManager.report());
			
			if (running.getCpuUsed() > running.getCpuMax()) {
				System.out.println("Out of CPU time - killed");
				terminate();
				continue;
			}
			
			CPUEvent event = CPUEvent.getCPUEvent();
			System.out.println("\nContext Switch: " + event);
			
			switch (event) {
			case INTERRUPT:
			case PAGE_FAULT:
			case BLOCK_IO:
			case OTHER:
				ready.add(running);
				running.setState(PCB.State.READY);
				break;
			case TERMINATE:
				terminate();
			}
			
			System.out.println("Ready Queue");
			ready.forEach(System.out::println);
		}
	}

}
