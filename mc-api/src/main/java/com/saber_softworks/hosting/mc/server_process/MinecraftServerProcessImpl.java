package com.saber_softworks.hosting.mc.server_process;

import com.saber_softworks.hosting.mc.configs.MinecraftServerConfig;
import com.saber_softworks.hosting.mc.server_process.MinecraftServerProcessState.*;
import io.vavr.Function0;
import io.vavr.Tuple2;
import io.vavr.control.Option;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;

public class MinecraftServerProcessImpl implements MinecraftServerProcess{
    private final AtomicReference<Option<Process>> currentServerProcess = new AtomicReference<>(Option.none());
    private final AtomicReference<Option<MinecraftServerConfig>> currentConfig = new AtomicReference<>(Option.none());
    private final AtomicReference<MinecraftServerProcessState> currentState = new AtomicReference<>(NotInitialized.instance);
    private final Executor executor = Executors.newSingleThreadExecutor();

    @Override
    public synchronized Option<Tuple2<Initializing, Function0<InitializationResult>>> submitInitializationRequestAndGetResultCallback(String pathToConfigYml) {
        AtomicReference<Function0<InitializationResult>> callback = new AtomicReference<>();
        Semaphore callbackSem = new Semaphore(0);
        currentState.updateAndGet(state -> switch (state) {
            case StartingPoint startingPoint -> {
                yield null; // TODO: 6/26/23 finish
            }
            case MinecraftServerProcessState other -> other;
        });
        return null;
    }

    @Override
    public synchronized Option<Tuple2<Starting, Function0<StartResult>>> submitStartRequestAndGetResultCallback(MinecraftServerConfig serverConfig) {
        return null;
    }

    @Override
    public synchronized Option<Tuple2<Stopping, Function0<StopResult>>> submitStopRequestAndGetResultCallback() {
        return null;
    }

    @Override
    public MinecraftServerProcessState getState() {
        return currentState.get();
    }
}
