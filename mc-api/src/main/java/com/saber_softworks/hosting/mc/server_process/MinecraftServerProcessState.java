package com.saber_softworks.hosting.mc.server_process;

import com.saber_softworks.hosting.mc.configs.MinecraftServerConfig;
import io.vavr.collection.Seq;
import lombok.NonNull;

import java.io.InputStream;

public sealed interface MinecraftServerProcessState {
    record NotInitialized() implements MinecraftServerProcessState {}
    record Initialized(
            @NonNull MinecraftServerConfig config
    ) implements MinecraftServerProcessState {}
    record Starting(
            @NonNull Initialized initializationState
    ) implements MinecraftServerProcessState {}
    record FailedToStart(
            @NonNull Initialized initializationState
    ) implements MinecraftServerProcessState {}
    record Running(
            @NonNull Initialized initializedState,
            long startTimeMillis,
            @NonNull InputStream serverOutputStream
    ) implements MinecraftServerProcessState {}
    record Stopping(
            @NonNull Initialized initializedState,
            long startTimeMillis, long stopRequestTimeMillis)
            implements MinecraftServerProcessState {}
    record Crashed(
            @NonNull Initialized initializedState,
            long startTimeMillis, long crashTimeMillis,
            int exitCode,
            @NonNull Seq<String> serverStdErr,
            @NonNull String crashLogPath,
            @NonNull String latestLogPath
    ) implements MinecraftServerProcessState {}
    record Stopped(
            Stopping request,
            long stopTimeMillis
    ) implements MinecraftServerProcessState {}
}
