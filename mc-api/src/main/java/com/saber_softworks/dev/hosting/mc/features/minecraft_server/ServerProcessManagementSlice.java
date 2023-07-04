package com.saber_softworks.dev.hosting.mc.features.minecraft_server;

import com.saber_softworks.dev.hosting.mc.features.State;
import com.saber_softworks.dev.lib.redux_java.Slice;
import io.vavr.Function1;
import io.vavr.Function2;

public class ServerProcessManagementSlice implements Slice<State, ServerProcessManagementState, ServerProcessManagementAction> {
    @Override
    public String topic() {
        return "MinecraftServer";
    }

    @Override
    public Class<ServerProcessManagementAction> payloadClass() {
        return ServerProcessManagementAction.class;
    }

    @Override
    public Function1<State, ServerProcessManagementState> selector() {
        return null; // TODO: 7/3/23 finish
    }

    @Override
    public Function2<State, ServerProcessManagementState, State> integrator() {
        return null; // TODO: 7/3/23 finish
    }

    @Override
    public Function2<ServerProcessManagementState, ServerProcessManagementAction, ServerProcessManagementState> reducer() {
        return null; // TODO: 7/3/23 finish
    }
}
