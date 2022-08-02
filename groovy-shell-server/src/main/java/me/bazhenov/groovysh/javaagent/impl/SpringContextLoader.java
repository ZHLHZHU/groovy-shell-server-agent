package me.bazhenov.groovysh.javaagent.impl;

import net.bytebuddy.asm.Advice;
import org.springframework.context.ApplicationContext;

/**
 * @author zhuzhihao
 * @date 2022/7/28 20:43
 * @Description
 */
public class SpringContextLoader {

    @Advice.OnMethodEnter
    public static void enter(@Advice.AllArguments Object[] args) {
        final Object context = args[0];
        SpringContextHolder.setContext((ApplicationContext) context);
    }

}
