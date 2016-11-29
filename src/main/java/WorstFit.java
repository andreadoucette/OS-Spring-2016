import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
public class WorstFit implements MemoryManager {
	private boolean defragmented = true; 
	
	private SortedSet<MemoryBlock> open = new TreeSet<MemoryBlock>(
			(block1, block2)-> block1.getBase() - block2.getBase());
	private SortedSet<MemoryBlock> used = new TreeSet<MemoryBlock>(
			(block1, block2) -> block1.getBase() - block2.getBase());
	
	//Open memory look up 
	private MemoryBlock findAndSplitFirstBlock(int size) {
		System.out.format("WorstFit: Looking for %s memory%n", size);
		
		MemoryBlock largestBlock = open.stream()
			.max((block1, block2) -> block1.getLimit() - block2.getLimit())
			.map(block -> block.getLimit() >= size ? block : null)
			.orElse(null);
		
		if (largestBlock == null) {
			return null;
		}
		
		int limit = largestBlock.getLimit();
		
		int base = largestBlock.getBase();
		System.out.format("Found: %s in location %s%n", limit, base);
		if (limit > size) {
			// Split
			largestBlock.setBase(base + size);
			largestBlock.setLimit(limit - size);

			// Make a new block to return
			MemoryBlock newBlock = new MemoryBlock(base, size);
			System.out.format("Open: %s%nUsed: %s%n", largestBlock, newBlock);
			return newBlock;
		} else {
			open.remove(largestBlock);
			System.out.format("Used: %s%n", largestBlock);
			return largestBlock;
		}
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
	
	public WorstFit(int limit) {
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
