package com.saber_softworks.dev.hosting.mc;

import com.saber_softworks.dev.hosting.mc.features.State;
import com.saber_softworks.dev.lib.redux_java.Redux;
import com.saber_softworks.dev.lib.redux_java.core.Store;
import io.vavr.collection.List;
import io.vavr.control.Option;

public class Main {
    public static void main(String[] args) {
        Store<State> store = Redux.createStore(State.initial(), Option.none(), List.empty());
        // TODO: create features and pass store as registrar
    }
}
