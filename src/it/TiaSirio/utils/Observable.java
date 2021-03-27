package it.TiaSirio.utils;

import java.util.ArrayList;
import java.util.List;

public class Observable<T> {
    private final List<Observer<T>> observers = new ArrayList<>();

    public void addObserver(Observer<T> observer){
        synchronized (observers){
            observers.add(observer);
        }
    }

    public void notifyObservers(T msg){
        synchronized (observers){
            for(Observer<T> observer : observers){
                observer.update(msg);
            }
        }
    }

}
