package com.kaaterskil.workflow.engine.context;

import java.util.Stack;

import com.kaaterskil.workflow.engine.ProcessEngineService;
import com.kaaterskil.workflow.engine.interceptor.CommandContext;
import com.kaaterskil.workflow.engine.interceptor.CommandInvocationContext;
import com.kaaterskil.workflow.engine.operation.Workflow;

public class Context {

    protected static ThreadLocal<Stack<CommandContext>> commandContextThreadLocal = new ThreadLocal<>();
    protected static ThreadLocal<Stack<CommandInvocationContext>> commandInvocationContextThreadLocal = new ThreadLocal<>();
    protected static ThreadLocal<Stack<ProcessEngineService>> processEngineServiceThreadLocal = new ThreadLocal<>();

    /*---------- CommandContext ----------*/

    public static CommandContext getCommandContext() {
        final Stack<CommandContext> stack = getStack(commandContextThreadLocal);
        if (stack.isEmpty()) {
            return null;
        }
        return stack.peek();
    }

    public static void setCommandContext(CommandContext commandContext) {
        getStack(commandContextThreadLocal).push(commandContext);
    }

    public static void removeCommandContext() {
        getStack(commandContextThreadLocal).pop();
    }

    public static Workflow getWorkflow() {
        return getCommandContext().getWorkflow();
    }

    /*---------- CommandInvocationContext ----------*/

    public static CommandInvocationContext getCommandInvocationContext() {
        final Stack<CommandInvocationContext> stack = getStack(commandInvocationContextThreadLocal);
        if (stack.isEmpty()) {
            return null;
        }
        return stack.peek();
    }

    public static void setCommandInvocationContext(CommandInvocationContext commandInvocationContext) {
        getStack(commandInvocationContextThreadLocal).push(commandInvocationContext);
    }

    public static void removeCommandInvocationContext() {
        getStack(commandInvocationContextThreadLocal).pop();
    }

    /*---------- ProcessEngineService ----------*/

    public static ProcessEngineService getProcessEngineService() {
        final Stack<ProcessEngineService> stack = getStack(processEngineServiceThreadLocal);
        if(stack.isEmpty()) {
            return null;
        }
        return stack.peek();
    }

    public static void setProcessEngineService(ProcessEngineService processEngineService) {
        getStack(processEngineServiceThreadLocal).push(processEngineService);
    }

    public static void removeProcessEngineService() {
        getStack(processEngineServiceThreadLocal).pop();
    }

    protected static <T> Stack<T> getStack(ThreadLocal<Stack<T>> threadLocal) {
        Stack<T> stack = threadLocal.get();
        if (stack == null) {
            stack = new Stack<T>();
            threadLocal.set(stack);
        }
        return stack;
    }
}
