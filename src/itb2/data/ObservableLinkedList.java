package itb2.data;

import java.util.Collection;
import java.util.EventListener;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class ObservableLinkedList<E> extends LinkedList<E> {
	private static final long serialVersionUID = -9164794867510245113L;
	public static final int ITEMS_ADDED = 1, ITEMS_REMOVED = 2;
	protected final Set<ListListener<E>> listeners = new HashSet<>();
	
	public boolean addListener(ListListener<E> listener) {
		return listeners.add(listener);
	}
	
	public boolean removeListener(ListListener<E> listener) {
		return listeners.remove(listener);
	}
	
	protected void notice(int method) {
		for(ListListener<E> listener : listeners)
			listener.itemsChanged(this, method);
	}
	
	@Override
	public void push(E e) {
		super.push(e);
		notice(ITEMS_ADDED);
	}
	
	@Override
	public E pop() {
		E item = super.pop();
		notice(ITEMS_REMOVED);
		
		return item;
	}
	
	@Override
	public E poll() {
		int size = size();
		E item = super.poll();
		
		if(size != size())
			notice(ITEMS_REMOVED);
		
		return item;
	}
	
	@Override
	public E pollFirst() {
		int size = size();
		E item = super.pollFirst();
		
		if(size != size())
			notice(ITEMS_REMOVED);
		
		return item;
	}
	
	@Override
	public E pollLast() {
		int size = size();
		E item = super.pollLast();
		
		if(size != size())
			notice(ITEMS_REMOVED);
		
		return item;
	}
	
	@Override
	public boolean add(E e) {
		boolean changed = super.add(e);
		
		if(changed)
			notice(ITEMS_ADDED);
		
		return changed;
	}
	
	@Override
	public void add(int index, E element) {
		super.add(index, element);
		notice(ITEMS_ADDED);
	}
	
	@Override
	public boolean addAll(Collection<? extends E> c) {
		boolean changed = super.addAll(c);
		
		if(changed)
			notice(ITEMS_ADDED);
		
		return changed;
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		boolean changed = super.addAll(index, c);
		
		if(changed)
			notice(ITEMS_ADDED);
		
		return changed;
	}
	
	@Override
	public void addFirst(E e) {
		super.addFirst(e);
		notice(ITEMS_ADDED);
	}
	
	@Override
	public void addLast(E e) {
		super.addLast(e);
		notice(ITEMS_ADDED);
	}
	
	@Override
	public void clear() {
		int size = size();
		super.clear();
		
		if(size != size())
			notice(ITEMS_REMOVED);
	}
	
	@Override
	public boolean remove(Object o) {
		boolean changed = super.remove(o);
		
		if(changed)
			notice(ITEMS_REMOVED);
		
		return changed;
	}
	
	@Override
	public boolean removeAll(Collection<?> c) {
		boolean changed = super.removeAll(c);
		
		if(changed)
			notice(ITEMS_REMOVED);
		
		return changed;
	}
	
	@Override
	public E remove() {
		int size = size();
		E item = super.remove();
		
		if(size != size())
			notice(ITEMS_REMOVED);
		
		return item;
	}
	
	@Override
	public E remove(int index) {
		int size = size();
		E item = super.remove(index);
		
		if(size != size())
			notice(ITEMS_REMOVED);
		
		return item;
	}
	
	@Override
	public E removeFirst() {
		int size = size();
		E item = super.removeFirst();
		
		if(size != size())
			notice(ITEMS_REMOVED);
		
		return item;
	}
	
	@Override
	public boolean removeFirstOccurrence(Object o) {
		boolean changed = super.removeFirstOccurrence(o);
		
		if(changed)
			notice(ITEMS_REMOVED);
		
		return changed;
	}
	
	@Override
	public E removeLast() {
		int size = size();
		E item = super.removeLast();
		
		if(size != size())
			notice(ITEMS_REMOVED);
		
		return item;
	}
	
	@Override
	public boolean removeLastOccurrence(Object o) {
		boolean changed = super.removeLastOccurrence(o);
		
		if(changed)
			notice(ITEMS_REMOVED);
		
		return changed;
	}
	
	//TODO Check other Methods that should not be supported / must be modified
	
	public interface ListListener<E> extends EventListener {
		public void itemsChanged(ObservableLinkedList<E> list, int type);
	}
}
