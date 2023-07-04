package com.saber_softworks.dev.hosting.mc.features.minecraft_server.process;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;

import io.vavr.control.Option;
import org.tinylog.Logger;

public class MinecraftServerController {
    private final AtomicReference<MinecraftServerProcessState> presentedStateBox = new AtomicReference<>(MinecraftServerProcessState.NotInitialized.instance);
    private final AtomicReference<MinecraftServerProcessState> actualStateBox = new AtomicReference<>(MinecraftServerProcessState.NotInitialized.instance);
    private final Executor taskQueue = Executors.newSingleThreadExecutor();
    private final Semaphore modificationSem = new Semaphore(1);
    private final MinecraftServerProcess serverProcess;

    public MinecraftServerController(MinecraftServerProcess serverProcess) {
        this.serverProcess = serverProcess;
    }

    public MinecraftServerProcessState getState() {
        return presentedStateBox.get();
    }

    public MinecraftServerProcessState initialize(String configPath) {
        // TODO: use semaphore
        var checkpointSem = new Semaphore(0);
        taskQueue.execute(() -> {
            try {
                modificationSem.acquire();
                var currentState = actualStateBox.getAndUpdate( // TODO: this is wrong
                        state -> switch (state) {
                            case MinecraftServerProcessState.StartingPoint startingPoint -> new MinecraftServerProcessState.Initializing(configPath);
                            case MinecraftServerProcessState other -> other;
                        }
                );
                presentedStateBox.set(actualStateBox.get());
                checkpointSem.release();
                if (currentState instanceof MinecraftServerProcessState.StartingPoint startingPoint) {
                    actualStateBox.set(this.serverProcess.initialize(startingPoint, configPath));
                    presentedStateBox.set(actualStateBox.get());
                }
            } catch (InterruptedException e) {
                Logger.warn(e);
            } finally {
                modificationSem.release();
            }
        });
        try {
            checkpointSem.acquire();
        } catch (InterruptedException e) {
            Logger.warn(e);
        }
        return getState();
    }

    private Option<InterruptedException> attemptScheduledModification() {
        return null; // TODO: implement
    }
}
