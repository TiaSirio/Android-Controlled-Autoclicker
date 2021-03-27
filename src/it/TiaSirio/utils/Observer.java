package it.TiaSirio.utils;

public interface Observer<T> {

    void update(T msg);
}
