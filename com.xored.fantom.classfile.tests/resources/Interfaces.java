import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;


public class Interfaces implements Serializable, Collection<Object> {
    public int size() {
    	return 0;
    }

    public boolean isEmpty() {
    	return true;
    }

    public boolean contains(Object o) {
    	return false;
    }

    public Iterator<Object> iterator() {
    	return new Iterator<Object>() {
    		public boolean hasNext() {
    	    	return false;
    	    }

    		public Object next() {
    	    	throw new NoSuchElementException();
    	    }

    		public void remove() {
    	    }
    	};
    }

    public Object[] toArray() {
    	return new Object[0];
    }

    public <T> T[] toArray(T[] a) {
    	return a;
    }

    public boolean add(Object e) {
    	return false;
    }

    public boolean remove(Object o) {
    	return false;
    }

    public boolean containsAll(Collection<?> c) {
    	return false;
    }

    public boolean addAll(Collection<? extends Object> c) {
    	return false;
    }

    public boolean removeAll(Collection<?> c) {
    	return false;
    }

    public boolean retainAll(Collection<?> c) {
    	c.clear();
    	return true;
    }

    public void clear() {
    }

    public boolean equals(Object o) {
    	return o instanceof Interfaces;
    }

    public int hashCode() {
    	return 0;
    }
}
