package com.ds.newdesignpatterns.observer;

public interface Subject {

    void addObserver(Observer observer);

    void delObserver(Observer observer);

    void notifyObserver(Event event);
}
