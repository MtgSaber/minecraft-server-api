package com.saber_softworks.dev.hosting.mc.features.minecraft_server.process;

import com.saber_softworks.dev.hosting.mc.configs.MinecraftServerConfig;

public interface MinecraftServerProcess {
    MaybeThunkReceiptWithTimedCallback<MinecraftServerProcessState.Initializing, MinecraftServerProcessState.InitializationResult> submitInitializationRequestAndGetResultCallback(String pathToConfigYml);
    MaybeThunkReceiptWithTimedCallback<MinecraftServerProcessState.Starting, MinecraftServerProcessState.StartResult> submitStartRequestAndGetResultCallback(MinecraftServerConfig serverConfig);
    MaybeThunkReceiptWithTimedCallback<MinecraftServerProcessState.Stopping, MinecraftServerProcessState.StopResult> submitStopRequestAndGetResultCallback();
    MinecraftServerProcessState getState();
}
