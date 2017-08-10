package com.nike.cerberus.endpoints;


import com.nike.riposte.server.http.RequestInfo;
import com.nike.riposte.server.http.ResponseInfo;
import com.nike.riposte.server.http.StandardEndpoint;
import com.nike.riposte.util.AsyncNettyHelper;
import com.nike.riposte.util.Matcher;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * Dummy endpoint for performance testing
 */
public class DummyEndpoint extends StandardEndpoint<DummyEndpoint.Foo, DummyEndpoint.Bar> {

    public static final String PATH = "/dummy";

    private final Logger log = LoggerFactory.getLogger(getClass());

    public DummyEndpoint() {
    }

    @Override
    public CompletableFuture<ResponseInfo<Bar>> execute(final RequestInfo<Foo> request,
                                                        final Executor longRunningTaskExecutor,
                                                        final ChannelHandlerContext ctx) {
        return CompletableFuture.supplyAsync(
                AsyncNettyHelper.supplierWithTracingAndMdc(() -> doSomething(request.getContent()), ctx),
                longRunningTaskExecutor
        );
    }

    public ResponseInfo<Bar> doSomething(final Foo foo) {
        if (foo == null) {
            log.info("foo was null");
        } else {
            log.info("foo was " + foo.getId());
        }

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            log.info("dummy", e);
            Thread.interrupted();
        }

        log.info("dummy done sleeping");

        Bar bar = new Bar();
        bar.setBbb(System.currentTimeMillis());

        return ResponseInfo.<Bar>newBuilder(bar)
                .withHttpStatusCode(200)
                .build();
    }

    @Override
    public Matcher requestMatcher() {
        return Matcher.match(PATH, HttpMethod.POST);
    }

    public static class Foo {
        private int id;

        public int getId() {
            return id;
        }

        public Foo setId(int id) {
            this.id = id;
            return this;
        }
    }


    public static class Bar {

        private long bbb;

        public long getBbb() {
            return bbb;
        }

        public Bar setBbb(long bbb) {
            this.bbb = bbb;
            return this;
        }
    }

}
