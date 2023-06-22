package com.saber_softworks.hosting.mc.server_process;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;
import com.saber_softworks.hosting.mc.server_process.MinecraftServerProcessState.*;

public class MinecraftServerProcessWrapper {
    private final AtomicReference<Process> serverProcessBox = new AtomicReference<>();
    private final AtomicReference<MinecraftServerProcessState> stateBox = new AtomicReference<>();
    private final Executor taskQueue = Executors.newSingleThreadExecutor();
    private final Semaphore modificationSem = new Semaphore(1);

    public MinecraftServerProcessState getState() {
        return stateBox.get();
    }

    public MinecraftServerProcessState initialize(String configPath) {
        // TODO: use semaphore
        return stateBox.updateAndGet(state -> switch (state) {
            case StartingPoint startingPoint -> {
                taskQueue.execute(() -> initialize(startingPoint, configPath));
                yield new Initializing(configPath);
            }
            case MinecraftServerProcessState other -> other;
        });
    }

    private void initialize(StartingPoint previousState, String configPath) {

    }
}
