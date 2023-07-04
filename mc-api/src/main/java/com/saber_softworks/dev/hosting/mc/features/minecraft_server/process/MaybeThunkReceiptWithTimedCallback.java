package com.saber_softworks.dev.hosting.mc.features.minecraft_server.process;

import io.vavr.Function1;
import io.vavr.Tuple2;
import io.vavr.control.Option;

public record MaybeThunkReceiptWithTimedCallback<RECEIPT, COMPLETED> (
        Option<Tuple2<RECEIPT, Function1<Long, Option<COMPLETED>>>> maybeThunkReceiptWithTimedCallback
) {}
