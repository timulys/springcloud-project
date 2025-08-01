package com.example.gateway.filter;

import lombok.extern.log4j.Log4j2;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

@Log4j2
@Component
public class LoggingResponseFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpResponse originalResponse = exchange.getResponse();
        DataBufferFactory bufferFactory = originalResponse.bufferFactory();

        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux) {
                    Flux<? extends DataBuffer> flux = (Flux<? extends DataBuffer>) body;

                    return super.writeWith(
                            flux.collectList().map(dataBuffers -> {
                                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                                dataBuffers.forEach(buffer -> {
                                    byte[] bytes = new byte[buffer.readableByteCount()];
                                    buffer.read(bytes);
                                    outputStream.write(bytes, 0, bytes.length);
                                });

                                byte[] content = outputStream.toByteArray();
                                String bodyString = new String(content, StandardCharsets.UTF_8);
                                log.info("Response Body: {}", bodyString);

                                // 원본 응답 바디를 다시 감싼 DataBuffer를 반환
                                return bufferFactory.wrap(content);
                            })
                    );
                }

                return super.writeWith(body); // body가 Flux가 아닌 경우 그대로 전달
            }
        };

        return chain.filter(exchange.mutate().response(decoratedResponse).build());
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
