package com.saber_softworks.hosting.mc.server_process;

import com.saber_softworks.hosting.mc.configs.MinecraftServerConfig;
import com.saber_softworks.hosting.mc.server_process.MinecraftServerProcessState.*;
import io.vavr.Function0;
import io.vavr.Tuple2;
import io.vavr.control.Option;

public interface MinecraftServerProcess {
    Option<Tuple2<Initializing, Function0<InitializationResult>>> submitInitializationRequestAndGetResultCallback(String pathToConfigYml);
    Option<Tuple2<Starting, Function0<StartResult>>> submitStartRequestAndGetResultCallback(MinecraftServerConfig serverConfig);
    Option<Tuple2<Stopping, Function0<StopResult>>> submitStopRequestAndGetResultCallback();
    MinecraftServerProcessState getState();
}
