package itb2.data;

import java.util.Collection;
import java.util.Comparator;
import java.util.EventListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class ObservableTreeSet<E> extends TreeSet<E> {
	private static final long serialVersionUID = -8354061612671698512L;
	public static final int ITEMS_ADDED = 1, ITEMS_REMOVED = 2;
	protected final Set<SetListener<E>> listeners = new HashSet<>();
	
	public ObservableTreeSet() {}
	
	public ObservableTreeSet(Comparator<E> comparator) {
		super(comparator);
	}
	
	public boolean addListener(SetListener<E> listener) {
		return listeners.add(listener);
	}
	
	public boolean removeListener(SetListener<E> listener) {
		return listeners.remove(listener);
	}
	
	protected void notice(int method) {
		for(SetListener<E> listener : listeners)
			listener.itemsChanged(this, method);
	}
	
	@Override
	public boolean add(E e) {
		boolean changed = super.add(e);
		
		if(changed)
			notice(ITEMS_ADDED);
		
		return changed;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		boolean changed = super.addAll(c);
		
		if(changed)
			notice(ITEMS_ADDED);
		
		return changed;
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
	public boolean retainAll(Collection<?> c) {
		boolean changed = super.retainAll(c);

		if(changed)
			notice(ITEMS_REMOVED);
		
		return changed;
	}

	@Override
	public Iterator<E> iterator() {
		Iterator<E> wrappedIterator = super.iterator();
		
		return new Iterator<E>() {
			@Override
			public boolean hasNext() {
				return wrappedIterator.hasNext();
			}
			
			@Override
			public E next() {
				return wrappedIterator.next();
			}
		};
	}
	
	@Override
	public SortedSet<E> headSet(E toElement) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public SortedSet<E> subSet(E fromElement, E toElement) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public SortedSet<E> tailSet(E fromElement) {
		throw new UnsupportedOperationException("Not yet implemented");
	}
	
	//TODO Check other Methods that should not be supported / must be modified
	
	public interface SetListener<E> extends EventListener {
		public void itemsChanged(ObservableTreeSet<E> set, int type);
	}

}
