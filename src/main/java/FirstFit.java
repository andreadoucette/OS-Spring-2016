import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class FirstFit implements MemoryManager {
	private boolean defragmented = true;
	
	// The open memory is a SortedSet, so we never need to sort it
	private SortedSet<MemoryBlock> open = new TreeSet<MemoryBlock>(
			(block1, block2) -> block1.getBase() - block2.getBase());
	private SortedSet<MemoryBlock> used = new TreeSet<MemoryBlock>(
			(block1, block2) -> block1.getBase() - block2.getBase());
	
	//Open memory
	private MemoryBlock findAndSplitFirstBlock(int size) {
		System.out.format("FirstFit: Looking for %s memory%n", size);
		
		Iterator<MemoryBlock> iterator = open.iterator();
		while (iterator.hasNext()) {
			MemoryBlock memoryBlock = iterator.next();
			int limit = memoryBlock.getLimit();
			
			if (limit >= size) {
				int base = memoryBlock.getBase();
				System.out.format("Found: %s in location %s%n", limit, base);
				if (limit > size) {
					// Split
					memoryBlock.setBase(base + size);
					memoryBlock.setLimit(limit - size);
					
					// Make a new block to return
					MemoryBlock newBlock = new MemoryBlock(base, size);
					System.out.format("Open: %s%nUsed: %s%n", memoryBlock, newBlock);
					return newBlock;
				} else {
					iterator.remove();
					System.out.format("Used: %s%n", memoryBlock);
					return memoryBlock;
				}
			}
		}
		
		System.out.println("Found no memory");
		return null;
	}
	
	private void defragment() {
		System.out.println("Defragmenting...");
		MemoryBlock previous = null;
		final Iterator<MemoryBlock> iterator = open.iterator();
		while (iterator.hasNext()) {
			MemoryBlock current = iterator.next();
			if (previous != null && previous.getBase() + previous.getLimit() == current.getBase()) {
				// Merge the blocks
				iterator.remove();
				previous.setLimit(previous.getLimit() + current.getLimit());
			} else {
				// If we merged the blocks, the PREVIOUS block stays in the list, so we don't need to update
				previous = current;
			}
		}
		defragmented = true;
	}
	
	public FirstFit(int limit) {
		open.add(new MemoryBlock(0, limit));
	}

	@Override
	public MemoryBlock malloc(int size) {
		MemoryBlock foundBlock = findAndSplitFirstBlock(size);
		
		if (foundBlock == null) {
			if (defragmented) {
				System.out.println("Memory already defragmented.");
			} else {
				defragment();
				foundBlock = findAndSplitFirstBlock(size);
			}
		}
		
		if (foundBlock != null) {
			used.add(foundBlock);
		}
		
		return foundBlock;
	}

	@Override
	public void free(MemoryBlock block) {
		used.remove(block);
		open.add(block);
		defragmented = false;
	}
 
	@Override
	public String report() {
		return "*****\t\t\tOpen Memory\t\t\t*****\n"
				+ open.stream().map(MemoryBlock::toString).collect(Collectors.joining("\n"))
				+ "\n\n*****\t\t\tUsed Memory\t\t\t*****\n"
				+ used.stream().map(MemoryBlock::toString).collect(Collectors.joining("\n"));
	}
}
