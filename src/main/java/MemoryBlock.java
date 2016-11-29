
public class MemoryBlock {
	private int base;
	private int limit;
	
	public MemoryBlock(int base, int limit) {
		this.base = base;
		this.limit = limit;
	}

	public int getBase() {
		return base;
	}
	
	public void setBase(int base) {
		this.base = base;
	}
	
	public int getLimit() {
		return limit;
	}
	
	public void setLimit(int limit) {
		this.limit = limit;
	}
	
	@Override
	public String toString() {
		return "\tmemBase: " + base
			+ "\tmemLimit: " + limit;
	}
}
