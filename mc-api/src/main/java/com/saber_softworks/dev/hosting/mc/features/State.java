package com.saber_softworks.dev.hosting.mc.features;

public record State(

) {
    public static State getDefault() {
        return new State();
    }
}
