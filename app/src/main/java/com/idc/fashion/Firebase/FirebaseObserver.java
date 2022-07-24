package com.idc.fashion.Firebase;


import androidx.lifecycle.DefaultLifecycleObserver;

import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;

public class FirebaseObserver {
    private DefaultLifecycleObserver observer;
    private List<ListenerRegistration> listeners;

    public FirebaseObserver(DefaultLifecycleObserver observer) {
        this.observer = observer;
        this.listeners = new ArrayList<>();
    }

    public FirebaseObserver(DefaultLifecycleObserver observer,
                            List<ListenerRegistration> listenerRegistrationList) {
        this.observer = observer;
        this.listeners = listenerRegistrationList;
    }

    public boolean addListener(ListenerRegistration listenerRegistration) {
        return listeners.add(listenerRegistration);
    }
    public boolean removeListener(ListenerRegistration listenerRegistration) {
        return listeners.remove(listenerRegistration);
    }

    public DefaultLifecycleObserver getObserver() {
        return observer;
    }

    public void setObserver(DefaultLifecycleObserver observer) {
        this.observer = observer;
    }

    public void removeAllListeners() {
        for (ListenerRegistration listenerRegistration : listeners)
            listenerRegistration.remove();
    }

    public boolean hasListeners() {
        return !listeners.isEmpty();
    }
}
