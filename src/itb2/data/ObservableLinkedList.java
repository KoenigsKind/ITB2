package itb2.data;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class ObservableLinkedList<E> extends LinkedList<E> {
	private static final long serialVersionUID = -9164794867510245113L;
	protected final transient Set<ListDataListener> listeners = new HashSet<>();
	
	public boolean addListener(ListDataListener listener) {
		return listeners.add(listener);
	}
	
	public boolean removeListener(ListDataListener listener) {
		return listeners.remove(listener);
	}
	
	protected void notice(int type, int index) {
		notice(type, index, index);
	}
	
	protected void notice(int type, int index0, int index1) {
		ListDataEvent event = new ListDataEvent(this, type, index0, index1);
		
		for(ListDataListener listener : listeners) {
			switch(type) {
				case ListDataEvent.CONTENTS_CHANGED:
					listener.contentsChanged(event);
					break;
				case ListDataEvent.INTERVAL_ADDED:
					listener.intervalAdded(event);
					break;
				case ListDataEvent.INTERVAL_REMOVED:
					listener.intervalRemoved(event);
					break;
			}
		}
	}
	
	@Override
	public void push(E e) {
		super.push(e);
		notice(ListDataEvent.INTERVAL_ADDED, 0);
	}
	
	@Override
	public E pop() {
		E item = super.pop();
		notice(ListDataEvent.INTERVAL_REMOVED, 0);
		
		return item;
	}
	
	@Override
	public E poll() {
		int size = size();
		E item = super.poll();
		
		if(size != size())
			notice(ListDataEvent.INTERVAL_REMOVED, 0);
		
		return item;
	}
	
	@Override
	public E pollFirst() {
		int size = size();
		E item = super.pollFirst();
		
		if(size != size())
			notice(ListDataEvent.INTERVAL_REMOVED, 0);
		
		return item;
	}
	
	@Override
	public E pollLast() {
		int size = size();
		E item = super.pollLast();
		
		if(size != size())
			notice(ListDataEvent.INTERVAL_REMOVED, size-1);
		
		return item;
	}
	
	@Override
	public boolean add(E e) {
		boolean changed = super.add(e);
		
		if(changed)
			notice(ListDataEvent.INTERVAL_ADDED, size()-1);
		
		return changed;
	}
	
	@Override
	public void add(int index, E element) {
		super.add(index, element);
		notice(ListDataEvent.INTERVAL_ADDED, index);
	}
	
	@Override
	public boolean addAll(Collection<? extends E> c) {
		int first = size();
		boolean changed = super.addAll(c);
		
		if(changed)
			notice(ListDataEvent.INTERVAL_ADDED, first, size()-1);
		
		return changed;
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		boolean changed = super.addAll(index, c);
		
		if(changed)
			notice(ListDataEvent.INTERVAL_ADDED, index, index + c.size()-1);
		
		return changed;
	}
	
	@Override
	public void addFirst(E e) {
		super.addFirst(e);
		notice(ListDataEvent.INTERVAL_ADDED, 0);
	}
	
	@Override
	public void addLast(E e) {
		super.addLast(e);
		notice(ListDataEvent.INTERVAL_ADDED, size()-1);
	}
	
	@Override
	public void clear() {
		int size = size();
		super.clear();
		
		if(size != size())
			notice(ListDataEvent.INTERVAL_REMOVED, 0, size-1);
	}
	
	@Override
	public boolean remove(Object o) {
		int index = super.indexOf(o);
		boolean changed = super.remove(o);
		
		if(changed)
			notice(ListDataEvent.INTERVAL_REMOVED, index);
		
		return changed;
	}
	
	@Override
	public boolean removeAll(Collection<?> c) {
		List<int[]> indexes = new LinkedList<>();
		int index = 0, start = -1;
		for(E e : this) {
			if(c.contains(e)) {
				if(start < 0)
					start = index;
			} else if(start > 0) {
				indexes.add(new int[] {start, index - 1});
				start = -1;
			}
			index++;
		}
		if(start > 0)
			indexes.add(new int[] {start, size() - 1});
		Collections.reverse(indexes);
		
		boolean changed = super.removeAll(c);
		
		if(changed)
			for(int[] segment : indexes)
				notice(ListDataEvent.INTERVAL_REMOVED, segment[0], segment[1]);
		
		return changed;
	}
	
	@Override
	public E remove() {
		int size = size();
		E item = super.remove();
		
		if(size != size())
			notice(ListDataEvent.INTERVAL_REMOVED, 0);
		
		return item;
	}
	
	@Override
	public E remove(int index) {
		int size = size();
		E item = super.remove(index);
		
		if(size != size())
			notice(ListDataEvent.INTERVAL_REMOVED, index);
		
		return item;
	}
	
	@Override
	public E removeFirst() {
		int size = size();
		E item = super.removeFirst();
		
		if(size != size())
			notice(ListDataEvent.INTERVAL_REMOVED, 0);
		
		return item;
	}
	
	@Override
	public boolean removeFirstOccurrence(Object o) {
		int index = super.indexOf(o);
		boolean changed = super.removeFirstOccurrence(o);
		
		if(changed)
			notice(ListDataEvent.INTERVAL_REMOVED, index);
		
		return changed;
	}
	
	@Override
	public E removeLast() {
		int size = size();
		E item = super.removeLast();
		
		if(size != size())
			notice(ListDataEvent.INTERVAL_REMOVED, size-1);
		
		return item;
	}
	
	@Override
	public boolean removeLastOccurrence(Object o) {
		int index = super.lastIndexOf(o);
		boolean changed = super.removeLastOccurrence(o);
		
		if(changed)
			notice(ListDataEvent.INTERVAL_REMOVED, index);
		
		return changed;
	}
	
	@Override
	public E set(int index, E element) {
		E previous = super.set(index, element);
		
		if(previous != element)
			notice(ListDataEvent.CONTENTS_CHANGED, index);
		
		return previous;
	}
	
	//TODO Check other Methods that should not be supported / must be modified
	
}
