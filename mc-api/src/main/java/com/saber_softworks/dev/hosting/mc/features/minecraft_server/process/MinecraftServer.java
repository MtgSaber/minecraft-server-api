package com.saber_softworks.dev.hosting.mc.features.minecraft_server.process;

import io.vavr.collection.Seq;

public class MinecraftServer {
    private final Process process;

    public MinecraftServer(Process process) {
        this.process = process;
    }

    // TODO: 7/5/23 Add methods for requesting stdout

    public boolean isRunning() {
        return process.isAlive();
    }

    public void issueCommand(Seq<String> commandTokens) {
        // TODO: 7/5/23 implement
    }

    public void stopAndBlockUntilTermination() {
        // TODO: 7/5/23 implement
    }
}
