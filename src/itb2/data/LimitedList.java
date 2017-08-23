package itb2.data;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Inspired by <a href="https://stackoverflow.com/a/14322473">RenaudBlue Stackoverflow</a>
 * 
 * @author KoenigsKind
 *
 * @param <E>
 */
public class LimitedList<E> extends LinkedList<E> {
	private static final long serialVersionUID = 2963244990260423110L;
	private final int limit;
	
	public LimitedList(int limit) {
		this.limit = limit;
	}
	
	@Override
	public boolean add(E e) {
		boolean added = super.add(e);
		if(added)
			reduce();
		return added;
	}
	
	@Override
	public void add(int index, E element) {
		super.add(index, element);
		reduce();
	}
	
	@Override
	public boolean addAll(Collection<? extends E> c) {
		boolean changed = super.addAll(c);
		if(changed)
			reduce();
		return changed;
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		boolean changed = super.addAll(index, c);
		if(changed)
			reduce();
		return changed;
	}
	
	@Override
	public void addFirst(E e) {
		if(size() == limit)
			super.removeLast();
		super.addFirst(e);
	}
	
	@Override
	public void addLast(E e) {
		if(size() == limit)
			super.removeFirst();
		super.addLast(e);
	}
	
	private void reduce() {
		while(size() > limit)
			remove();
	}
	
	
}
