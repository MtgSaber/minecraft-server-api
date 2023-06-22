package com.saber_softworks.hosting.mc.server_process;

public sealed interface MinecraftServerProcessState {
    record NotInitialized() implements MinecraftServerProcessState {}
    record Initialized() implements MinecraftServerProcessState {}
    record Starting() implements MinecraftServerProcessState {}
    record FailedToStart() implements MinecraftServerProcessState {}
    record Running() implements MinecraftServerProcessState {}
    record Stopping() implements MinecraftServerProcessState {}
    record Crashed() implements MinecraftServerProcessState {}
    record Stopped() implements MinecraftServerProcessState {}
}
