package com.saber_softworks.dev.hosting.mc.features.minecraft_server;

import com.saber_softworks.dev.hosting.mc.features.State;
import com.saber_softworks.dev.lib.redux_java.Slice;
import com.saber_softworks.dev.lib.redux_java.slices.Integration;
import io.vavr.Function1;
import io.vavr.Function2;

import com.saber_softworks.dev.hosting.mc.features.minecraft_server.ServerProcessManagementAction.*;

public class ServerProcessManagementSlice implements Slice<State, ServerProcessManagementState, ServerProcessManagementAction> {
    @Override
    public String topic() {
        return "minecraft-server-management";
    }

    @Override
    public Class<ServerProcessManagementAction> payloadClass() {
        return ServerProcessManagementAction.class;
    }

    @Override
    public Function1<State, ServerProcessManagementState> selector() {
        return State::processManagement;
    }

    @Override
    public Function2<State, ServerProcessManagementState, State> integrator() {
        return Integration.of(State::withProcessManagement).memoize(Record::equals);
    }

    @Override
    public Function2<ServerProcessManagementState, ServerProcessManagementAction, ServerProcessManagementState> reducer() {
        return (state, action) -> {
            if (action instanceof FromServerProcess fromServerProcess)
                state = state.withCurrentProcessState(fromServerProcess.currentProcessState());
            return switch (action) {
                case Request request -> state
                        .withOpenRequests(state.openRequests().put(
                                request.uuid(), request
                        ));
                case Response response -> state
                        .withOpenRequests(state.openRequests().remove(
                                response.request().uuid()
                        ))
                        .withUnacknowledgedResponses(state.unacknowledgedResponses().put(
                                response.request().uuid(),
                                response
                        ));
                case ResponseAck(Response response) -> state
                        .withUnacknowledgedResponses(state.unacknowledgedResponses().remove(
                                response.request().uuid()
                        ));
                default -> state;
            };
        };
    }
}
