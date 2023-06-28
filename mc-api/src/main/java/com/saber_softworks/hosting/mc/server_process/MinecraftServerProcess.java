package com.saber_softworks.hosting.mc.server_process;

import com.saber_softworks.hosting.mc.configs.MinecraftServerConfig;
import com.saber_softworks.hosting.mc.server_process.MinecraftServerProcessState.*;
import io.vavr.Function0;
import io.vavr.Tuple2;
import io.vavr.control.Option;

public interface MinecraftServerProcess {
    MaybeThunkReceiptWithTimedCallback<Initializing, InitializationResult> submitInitializationRequestAndGetResultCallback(String pathToConfigYml);
    MaybeThunkReceiptWithTimedCallback<Starting, StartResult> submitStartRequestAndGetResultCallback(MinecraftServerConfig serverConfig);
    MaybeThunkReceiptWithTimedCallback<Stopping, StopResult> submitStopRequestAndGetResultCallback();
    MinecraftServerProcessState getState();
}
