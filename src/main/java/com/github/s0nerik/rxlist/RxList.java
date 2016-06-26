package com.github.s0nerik.rxlist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import rx.Observable;
import rx.subjects.PublishSubject;

public class RxList<E> implements List<E> {
    private final List<E> innerList;
    private final PublishSubject<Event<E>> eventsSubject = PublishSubject.create();

    private boolean notifyEachItemOnClear = false;

    public RxList() {
        this.innerList = new ArrayList<>();
    }

    public RxList(boolean notifyEachItemOnClear) {
        this();
        this.notifyEachItemOnClear = notifyEachItemOnClear;
    }

    public RxList(List<E> innerList) {
        this.innerList = innerList;
    }

    public RxList(List<E> innerList, boolean notifyEachItemOnClear) {
        this(innerList);
        this.notifyEachItemOnClear = notifyEachItemOnClear;
    }

    public Observable<Event<E>> events() {
        return eventsSubject;
    }

    @Override
    public void add(int location, E object) {
        innerList.add(location, object);
        eventsSubject.onNext(Event.create(Event.Type.ITEM_ADDED, location, object));
    }

    @Override
    public boolean add(E object) {
        boolean result = innerList.add(object);
        if (result) {
            eventsSubject.onNext(Event.create(Event.Type.ITEM_ADDED, innerList.size(), object));
        }
        return result;
    }

    @Override
    public boolean addAll(int location, Collection<? extends E> collection) {
        boolean result = innerList.addAll(location, collection);
        if (result) {
            int i = location;
            for (E item : collection) {
                eventsSubject.onNext(Event.create(Event.Type.ITEM_ADDED, i++, item));
            }
        }
        return result;
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        int startIndex = innerList.size();
        boolean result = innerList.addAll(collection);
        if (result) {
            int i = startIndex;
            for (E item : collection) {
                eventsSubject.onNext(Event.create(Event.Type.ITEM_ADDED, i++, item));
            }
        }
        return result;
    }

    @Override
    public void clear() {
        if (notifyEachItemOnClear) {
            List<E> oldItems = new ArrayList<>(innerList);
            innerList.clear();
            int i = 0;
            for (E oldItem : oldItems) {
                eventsSubject.onNext(Event.create(Event.Type.ITEM_REMOVED, i++, oldItem));
            }
        } else {
            innerList.clear();
        }
        eventsSubject.onNext(Event.create(Event.Type.ITEMS_CLEARED, -1, (E) null));
    }

    @Override
    public boolean contains(Object object) {
        return innerList.contains(object);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return innerList.containsAll(collection);
    }

    @Override
    public E get(int location) {
        return innerList.get(location);
    }

    @Override
    public int indexOf(Object object) {
        return innerList.indexOf(object);
    }

    @Override
    public boolean isEmpty() {
        return innerList.isEmpty();
    }

    @Override
    public Iterator<E> iterator() {
        return innerList.iterator();
    }

    @Override
    public int lastIndexOf(Object object) {
        return innerList.lastIndexOf(object);
    }

    @Override
    public ListIterator<E> listIterator() {
        return innerList.listIterator();
    }

    @Override
    public ListIterator<E> listIterator(int location) {
        return innerList.listIterator(location);
    }

    @Override
    public E remove(int location) {
        E removedItem = innerList.remove(location);
        eventsSubject.onNext(Event.create(Event.Type.ITEM_REMOVED, location, removedItem));
        return removedItem;
    }

    @Override
    public boolean remove(Object object) {
        int removedIndex = innerList.indexOf(object);
        boolean result = innerList.remove(object);
        if (result) {
            eventsSubject.onNext(Event.create(Event.Type.ITEM_REMOVED, removedIndex, (E) object));
        }
        return result;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        List<Object> removedItems = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        for (Object item : collection) {
            int index = innerList.indexOf(item);
            if (index > -1) {
                indices.add(index);
                removedItems.add(item);
            }
        }

        boolean result = innerList.removeAll(collection);
        if (result) {
            int i = 0;
            for (Object removedItem : removedItems) {
                eventsSubject.onNext(Event.create(Event.Type.ITEM_REMOVED, indices.get(i++), (E) removedItem));
            }
        }

        return result;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        List<Object> removedItems = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        int index = 0;
        for (Object item : innerList) {
            boolean contains = collection.contains(item);
            if (!contains) {
                indices.add(index);
                removedItems.add(item);
            }
            index++;
        }

        boolean result = innerList.retainAll(collection);
        if (result) {
            int i = 0;
            for (Object removedItem : removedItems) {
                eventsSubject.onNext(Event.create(Event.Type.ITEM_REMOVED, indices.get(i++), (E) removedItem));
            }
        }

        return result;
    }

    @Override
    public E set(int location, E object) {
        E previous = innerList.set(location, object);
        eventsSubject.onNext(Event.create(Event.Type.ITEM_CHANGED, location, object));
        return previous;
    }

    @Override
    public int size() {
        return innerList.size();
    }

    @Override
    public List<E> subList(int start, int end) {
        return innerList.subList(start, end);
    }

    @Override
    public Object[] toArray() {
        return innerList.toArray();
    }

    @Override
    public <T> T[] toArray(T[] array) {
        return innerList.toArray(array);
    }
}
