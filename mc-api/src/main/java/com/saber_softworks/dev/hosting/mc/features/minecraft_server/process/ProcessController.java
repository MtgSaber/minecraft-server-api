package com.saber_softworks.dev.hosting.mc.features.minecraft_server.process;

import com.saber_softworks.dev.hosting.mc.configs.MinecraftServerConfig;
import io.vavr.collection.List;
import io.vavr.control.Option;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;

import com.saber_softworks.dev.hosting.mc.features.minecraft_server.process.MinecraftServerProcessState.*;
import lombok.NonNull;

public class ProcessController {
    private volatile MinecraftServerProcessState currentState;
    private volatile Option<MinecraftServer> processIfRunning;
    private volatile Option<MinecraftServerConfig> configIfLoaded;

    private final Executor executor;

    public ProcessController(Executor executor) {
        this.executor = executor;

        this.currentState = NotInitialized.instance;
        this.processIfRunning = Option.none();
        this.configIfLoaded = Option.none();
    }

    public synchronized boolean requestInitializationStart(String yamlContent) {
        currentState = new Initializing(yamlContent);
        if (currentState instanceof StartingPoint startingPoint) {
            executor.execute(() -> {
                synchronized (this) {
                    currentState = this.initialize(yamlContent, startingPoint);
                }
            });

            return true;
        }
        return false;
    }

    private @NonNull InitializationResult initialize(String yamlContent, StartingPoint startingPoint) {
        return null; // TODO: 7/5/23 use config loader
    }

    public synchronized boolean requestProcessStart() {
        return configIfLoaded.map(config -> {
            if (currentState instanceof Initialized initialized) {
                currentState = new Starting(initialized);
                executor.execute(() -> {
                    synchronized (this) {
                        currentState = this.start(config, initialized);
                    }
                });

                return true;
            }
            return false;
        }).getOrElse(false);
    }

    private @NonNull StartResult start(MinecraftServerConfig config, Initialized initializedState) {
        try {
            var builder = new ProcessBuilder(List
                    .of(config.pathToJreIfNotDefaultJavaAlias().getOrElse("java"))
                    .appendAll(config.jvmArgs().map(pair -> "-" + pair._1 + "=" + pair._2))
                    .append(config.pathToServerDirectory() + config.serverJarFilename())
                    .appendAll(config.serverJarArgs())
                    .asJava()
            );
            var process = builder.start();
            this.processIfRunning = Option.some(new MinecraftServer(process));
            return new Running(initializedState, System.currentTimeMillis(), process.getInputStream());
        } catch (IOException e) {
            return new FailedToStart(initializedState, e);
        }
    }

    // TODO: 7/5/23 add method for stopping server process

    // TODO: 7/5/23 add method for requesting server world file
}
