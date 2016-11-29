
//Interface that allocates and frees memory
public interface MemoryManager {
	
	MemoryBlock malloc(int size);
	void free(MemoryBlock block);
	String report();
}
