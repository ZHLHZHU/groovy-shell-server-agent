package me.bazhenov.groovysh.javaagent.impl;


import me.bazhenov.groovysh.javaagent.ContextHolder;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.NamedElement;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import org.springframework.context.ApplicationContext;

import java.lang.instrument.Instrumentation;
import java.util.concurrent.CompletableFuture;

/**
 * @author zhuzhihao
 * @date 2022/7/28 16:48
 * @Description
 */
public class SpringContextHolder implements ContextHolder<ApplicationContext> {

    private static final CompletableFuture<ApplicationContext> future = new CompletableFuture<>();


    @Override
    public String contextName() {
        return "spring";
    }

    public static void setContext(ApplicationContext context) {
        future.complete(context);
    }

    @Override
    public CompletableFuture<ApplicationContext> loadContext(String agentArgs, Instrumentation instrumentation) {
        final ElementMatcher.Junction<NamedElement> springApplicationType = ElementMatchers.nameEndsWith("ApplicationObjectSupport");
        final AgentBuilder.Transformer transformer = (builder, typeDescription, classLoader, javaModule) -> builder.method(ElementMatchers.named("setApplicationContext"))
                .intercept(Advice.to(SpringContextLoader.class));
        new AgentBuilder.Default()
                .with(new AgentBuilder.InitializationStrategy.SelfInjection.Eager())
                .type(springApplicationType)
                .transform(transformer)
                .installOn(instrumentation);

        return future;
    }

}
