package me.bazhenov.groovysh.javaagent;

import org.springframework.context.ApplicationContext;

import java.lang.instrument.Instrumentation;
import java.util.concurrent.CompletableFuture;

public interface ContextHolder<T> {

    String contextName();

    CompletableFuture<ApplicationContext> loadContext(String agentArgs, Instrumentation instrumentation);
}
