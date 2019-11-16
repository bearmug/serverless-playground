package bearmug.lambda;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u000f\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0010\u0010\u0002\u001a\u00020\u00038\u0002X\u0083\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lbearmug/lambda/PingControllerTest;", "Lio/kotlintest/specs/StringSpec;", "httpClient", "Lio/micronaut/http/client/HttpClient;", "(Lio/micronaut/http/client/HttpClient;)V", "mn-ping-kotlin"})
@io.micronaut.test.annotation.MicronautTest()
public final class PingControllerTest extends io.kotlintest.specs.StringSpec {
    @javax.inject.Inject()
    private final io.micronaut.http.client.HttpClient httpClient = null;
    
    public PingControllerTest(@org.jetbrains.annotations.NotNull()
    @io.micronaut.http.client.annotation.Client(value = "/")
    io.micronaut.http.client.HttpClient httpClient) {
        super(null);
    }
}