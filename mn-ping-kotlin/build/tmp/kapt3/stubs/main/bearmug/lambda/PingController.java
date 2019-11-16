package bearmug.lambda;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u0005\u001a\u00020\u0006H\u0007J\u0016\u0010\u0007\u001a\u0010\u0012\f\u0012\n \t*\u0004\u0018\u00010\u00060\u00060\bH\u0007J\b\u0010\n\u001a\u00020\u0006H\u0007J\u0016\u0010\u000b\u001a\u0010\u0012\f\u0012\n \t*\u0004\u0018\u00010\u00060\u00060\bH\u0007R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\f"}, d2 = {"Lbearmug/lambda/PingController;", "", "pingService", "Lbearmug/lambda/PingService;", "(Lbearmug/lambda/PingService;)V", "pingGraal", "", "pingGraalReactive", "Lreactor/core/publisher/Mono;", "kotlin.jvm.PlatformType", "pingJava", "pingJavaReactive", "mn-ping-kotlin"})
@io.micronaut.http.annotation.Controller(value = "/ping")
public final class PingController {
    private final bearmug.lambda.PingService pingService = null;
    
    @org.jetbrains.annotations.NotNull()
    @io.micronaut.http.annotation.Get(value = "/jvm-block")
    public final java.lang.String pingJava() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @io.micronaut.http.annotation.Get(value = "/graal-block")
    public final java.lang.String pingGraal() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @io.micronaut.http.annotation.Get(value = "/jvm-reactive")
    public final reactor.core.publisher.Mono<java.lang.String> pingJavaReactive() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @io.micronaut.http.annotation.Get(value = "/graal-reactive")
    public final reactor.core.publisher.Mono<java.lang.String> pingGraalReactive() {
        return null;
    }
    
    public PingController(@org.jetbrains.annotations.NotNull()
    bearmug.lambda.PingService pingService) {
        super();
    }
}