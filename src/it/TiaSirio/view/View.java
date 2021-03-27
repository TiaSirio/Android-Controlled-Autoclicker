package it.TiaSirio.view;

import it.TiaSirio.utils.Observable;
import it.TiaSirio.utils.Observer;

public abstract class View extends Observable<String> implements Observer<String> {

    protected View(){

    }

    protected abstract void sendMessage(Object message);
}