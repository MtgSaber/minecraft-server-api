package com.saber_softworks.dev.hosting.mc.features.minecraft_server.process;

import com.saber_softworks.dev.hosting.mc.configs.MinecraftServerConfig;
import com.saber_softworks.dev.hosting.mc.features.minecraft_server.process.MinecraftServerProcessState.*;
import io.vavr.*;
import io.vavr.control.Option;
import org.tinylog.Logger;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

public class MinecraftServerProcessImpl implements MinecraftServerProcess, AutoCloseable {
    private final AtomicReference<Option<Process>> currentServerProcess = new AtomicReference<>(Option.none());
    private final AtomicReference<Option<MinecraftServerConfig>> currentConfig = new AtomicReference<>(Option.none());
    private final AtomicReference<MinecraftServerProcessState> currentState = new AtomicReference<>(NotInitialized.instance);
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public synchronized MaybeThunkReceiptWithTimedCallback<Initializing, InitializationResult> submitInitializationRequestAndGetResultCallback(String pathToConfigYml) {
        return attemptThunkAndGetReceiptWithTimedCallbackIfValidated(
                StartingPoint.class,
                ignored -> new Initializing(pathToConfigYml),
                this::initialize
        );
    }

    private InitializationResult initialize(StartingPoint startingPoint, Initializing initializing) {
        return null; // TODO: 6/28/23 implement; be sure to update config box
    }

    @Override
    public MaybeThunkReceiptWithTimedCallback<Starting, StartResult> submitStartRequestAndGetResultCallback(MinecraftServerConfig serverConfig) {
        return null; // TODO: 6/28/23 implement same as initialize override
    }

    @Override
    public MaybeThunkReceiptWithTimedCallback<Stopping, StopResult> submitStopRequestAndGetResultCallback() {
        return null; // TODO: 6/28/23 implement same as initialize override
    }

    private synchronized <
            SENTINEL extends MinecraftServerProcessState,
            IN_PROGRESS extends MinecraftServerProcessState,
            COMPLETED extends MinecraftServerProcessState
    > MaybeThunkReceiptWithTimedCallback<IN_PROGRESS, COMPLETED> attemptThunkAndGetReceiptWithTimedCallbackIfValidated(
            Class<SENTINEL> sentinelClass,
            Function1<SENTINEL, IN_PROGRESS> inProgressStepAfterValidation,
            Function2<SENTINEL, IN_PROGRESS, COMPLETED> completionStep
    ) {
        AtomicReference<Option<Tuple2<IN_PROGRESS, Function1<Long, Option<COMPLETED>>>>> resultBox = new AtomicReference<>(Option.none());
        currentState.updateAndGet(state ->  {
            try {
                var sentinel = sentinelClass.cast(state);
                var inProgress = inProgressStepAfterValidation.apply(sentinel);

                AtomicReference<COMPLETED> completionBox = new AtomicReference<>();
                Semaphore callbackSem = new Semaphore(0);
                resultBox.set(Option.some(Tuple.of(
                        inProgress,
                        timeoutMillis -> {
                            try {
                                var success = callbackSem.tryAcquire(timeoutMillis, TimeUnit.MILLISECONDS);
                                if (!success)
                                    return Option.none();
                                return Option.some(completionBox.get());
                            } catch (InterruptedException e) {
                                Logger.warn(e);
                                return Option.none();
                            }
                        }
                )));
                executor.execute(() -> {
                    var completed = completionStep.apply(sentinel, inProgress);
                    currentState.set(completed);
                    completionBox.set(completed);
                    callbackSem.release();
                });

                return inProgress; // TODO: 6/26/23 finish
            } catch (ClassCastException e) {
                return state;
            }
        });
        return new MaybeThunkReceiptWithTimedCallback<>(resultBox.get());
    }

    @Override
    public MinecraftServerProcessState getState() {
        return currentState.get();
    }

    @Override
    public void close() throws Exception {
        executor.close();
        // TODO: 6/28/23 close server process
    }
}
