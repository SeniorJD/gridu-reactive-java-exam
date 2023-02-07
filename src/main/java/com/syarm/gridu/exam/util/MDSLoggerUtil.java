package com.syarm.gridu.exam.util;

import org.slf4j.MDC;
import reactor.core.publisher.Signal;

import java.util.Optional;
import java.util.function.Consumer;

public class MDSLoggerUtil {
    private static final String MDC_KEY = "MDC_KEY";

    public static <T> Consumer<Signal<T>> logOnNext(String loggerKey, Consumer<T> logStatement) {
        return signal -> {
            if (!signal.isOnNext()) return;

            Optional<String> toPutInMdc = signal.getContextView().getOrEmpty(loggerKey);

            toPutInMdc.ifPresentOrElse(tpim -> {
                        try (MDC.MDCCloseable ignored = MDC.putCloseable(MDC_KEY, tpim)) {
                            logStatement.accept(signal.get());
                        }
                    },
                    () -> logStatement.accept(signal.get()));
        };
    }

    public static <T> Consumer<Signal<T>> logOnError(String loggerKey, Consumer<Throwable> logStatement) {
        return signal -> {
            if (!signal.isOnError()) return;

            Optional<String> toPutInMdc = signal.getContextView().getOrEmpty(loggerKey);

            toPutInMdc.ifPresentOrElse(tpim -> {
                        try (MDC.MDCCloseable ignored = MDC.putCloseable(MDC_KEY, tpim)) {
                            logStatement.accept(signal.getThrowable());
                        }
                    },
                    () -> logStatement.accept(signal.getThrowable()));
        };
    }
}
