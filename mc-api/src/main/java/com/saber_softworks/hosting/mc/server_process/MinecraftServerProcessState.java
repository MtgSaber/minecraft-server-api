package com.saber_softworks.hosting.mc.server_process;

import com.saber_softworks.hosting.mc.configs.MinecraftServerConfig;
import io.vavr.collection.Seq;
import lombok.NonNull;
import lombok.With;

import java.io.InputStream;

public sealed interface MinecraftServerProcessState {
    sealed interface NotRunning extends MinecraftServerProcessState {}
    sealed interface InitializationResult extends MinecraftServerProcessState {}
    sealed interface StartResult extends MinecraftServerProcessState {}
    sealed interface RunResult extends MinecraftServerProcessState {}
    sealed interface StopResult extends MinecraftServerProcessState {}
    sealed interface StartingPoint extends MinecraftServerProcessState {}

    enum NotInitialized implements StartingPoint, NotRunning, MinecraftServerProcessState {
        instance;

        public static NotInitialized getInstance() { return instance; }
    }
    @With record Initializing(
            String serverConfigPath
    ) implements NotRunning, MinecraftServerProcessState {}
    @With record InitializationFailure(
            Initializing source,
            Exception cause
    ) implements NotRunning, InitializationResult, MinecraftServerProcessState {}
    @With record Initialized(
            @NonNull MinecraftServerConfig config
    ) implements NotRunning, InitializationResult, MinecraftServerProcessState {}
    @With record Starting(
            @NonNull Initialized initializationState
    ) implements NotRunning, MinecraftServerProcessState {}
    @With record FailedToStart(
            @NonNull Initialized initializationState
    ) implements NotRunning, StartResult, MinecraftServerProcessState {}
    @With record Running(
            @NonNull Initialized initializedState,
            long startTimeMillis,
            @NonNull InputStream serverOutputStream
    ) implements StartResult, MinecraftServerProcessState {}
    @With record Crashed(
            @NonNull Initialized initializedState,
            long startTimeMillis, long crashTimeMillis,
            int exitCode,
            @NonNull Seq<String> serverStdErr,
            @NonNull String crashLogPath,
            @NonNull String latestLogPath
    ) implements NotRunning, RunResult, MinecraftServerProcessState {}
    @With record Stopping(
            @NonNull Initialized initializedState,
            long startTimeMillis, long stopRequestTimeMillis
    ) implements NotRunning, RunResult, MinecraftServerProcessState {}
    @With record StopFailure(
            @NonNull Stopping source
    ) implements NotRunning, StopResult, MinecraftServerProcessState {}
    @With record Stopped(
            Stopping request,
            long stopTimeMillis
    ) implements StartingPoint, NotRunning, StopResult, MinecraftServerProcessState {}
}
