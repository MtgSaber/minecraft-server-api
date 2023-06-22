package com.saber_softworks.hosting.mc.server_process;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;
import com.saber_softworks.hosting.mc.server_process.MinecraftServerProcessState.*;
import org.tinylog.Logger;

public class MinecraftServerController {
    private final AtomicReference<MinecraftServerProcessState> presentedStateBox = new AtomicReference<>(NotInitialized.instance);
    private final AtomicReference<MinecraftServerProcessState> actualStateBox = new AtomicReference<>(NotInitialized.instance);
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
                var currentState = presentedStateBox.getAndUpdate(ignored -> actualStateBox.updateAndGet(
                        state -> switch (state) {
                            case StartingPoint startingPoint -> new Initializing(configPath);
                            case MinecraftServerProcessState other -> other;
                        }
                ));
                if (currentState instanceof StartingPoint startingPoint) {
                    var initializationResult = this.serverProcess.initialize(startingPoint, configPath);
                    // TODO: finish this
                }
            } catch (InterruptedException e) {
                Logger.warn(e);
            } finally {
                modificationSem.release();
            }
        });
        return getState();
    }
}
