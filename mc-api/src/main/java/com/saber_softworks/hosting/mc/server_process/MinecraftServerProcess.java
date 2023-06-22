package com.saber_softworks.hosting.mc.server_process;

import com.saber_softworks.hosting.mc.server_process.MinecraftServerProcessState.*;

public interface MinecraftServerProcess {
    MinecraftServerProcessState initialize(StartingPoint previousState, String configPath);
}
