package me.bazhenov.groovysh.javaagent;

import com.sun.tools.javac.util.Pair;
import me.bazhenov.groovysh.GroovyShellService;
import me.bazhenov.groovysh.javaagent.impl.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhuzhihao
 * @date 2022/7/28 16:46
 * @Description
 */
public class AgentStartUp {

    private static final Logger log = LoggerFactory.getLogger(AgentStartUp.class);

    private static final GroovyShellService service = new GroovyShellService();

    private static String host;

    private static int port;

    public static void premain(String agentArgs, Instrumentation instrumentation) {
        log.info("start up groovy shell agent");
        log.info("agent args: {}", agentArgs);

        final Map<String, String> argsMap = Arrays.stream(agentArgs.split(",")).map(e -> e.split(":")).collect(Collectors.toMap(e -> e[0], e -> e[1]));
        host

        final ContextHolder<ApplicationContext> contextHolder = new SpringContextHolder();
        contextHolder.loadContext(agentArgs, instrumentation).thenAccept(ctx -> {
            if (service.getIsServiceAlive().get()) {
                return;
            }

            Map<String, Object> bindings = new HashMap<>();
            bindings.put("ctx", ctx);
            service.setPort(1025);
            service.setBindings(bindings);
            try {
                log.info("groovy shell server starting...");
                service.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}