package com.kaaterskil.workflow.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kaaterskil.workflow.engine.behavior.BehaviorHelper;
import com.kaaterskil.workflow.engine.behavior.handler.ServiceTaskBehaviorHandler;
import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventDispatcher;
import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventDispatcherImpl;
import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventListener;
import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventType;
import com.kaaterskil.workflow.engine.delegate.invocation.DelegateInterceptor;
import com.kaaterskil.workflow.engine.deploy.BpmDeployer;
import com.kaaterskil.workflow.engine.deploy.CacheManager;
import com.kaaterskil.workflow.engine.deploy.Deployer;
import com.kaaterskil.workflow.engine.deploy.DeploymentCache;
import com.kaaterskil.workflow.engine.deploy.DeploymentCacheImpl;
import com.kaaterskil.workflow.engine.deploy.ProcessDefinitionCacheEntry;
import com.kaaterskil.workflow.engine.event.CompensationEventHandler;
import com.kaaterskil.workflow.engine.event.EventHandler;
import com.kaaterskil.workflow.engine.event.MessageEventHandler;
import com.kaaterskil.workflow.engine.event.SignalEventHandler;
import com.kaaterskil.workflow.engine.executor.AsyncExecutor;
import com.kaaterskil.workflow.engine.executor.AsyncJobHandler;
import com.kaaterskil.workflow.engine.executor.BaseAsyncExecutor;
import com.kaaterskil.workflow.engine.executor.JobHandler;
import com.kaaterskil.workflow.engine.interceptor.CommandContextFactory;
import com.kaaterskil.workflow.engine.interceptor.CommandContextInterceptor;
import com.kaaterskil.workflow.engine.interceptor.CommandInterceptor;
import com.kaaterskil.workflow.engine.interceptor.CommandInvoker;
import com.kaaterskil.workflow.engine.interceptor.LogInterceptor;
import com.kaaterskil.workflow.engine.parser.BpmParseHelper;
import com.kaaterskil.workflow.engine.parser.factory.ActivityBehaviorFactory;
import com.kaaterskil.workflow.engine.parser.factory.ListenerFactory;
import com.kaaterskil.workflow.engine.parser.factory.ListenerFactoryImpl;
import com.kaaterskil.workflow.engine.parser.handler.ParseHandler;
import com.kaaterskil.workflow.engine.parser.handler.ProcessParseHandler;
import com.kaaterskil.workflow.engine.parser.handler.SequenceFlowParseHandler;
import com.kaaterskil.workflow.engine.parser.handler.StartEventParseHandler;
import com.kaaterskil.workflow.engine.persistence.entity.EventSubscriptionType;
import com.kaaterskil.workflow.engine.service.DeploymentService;
import com.kaaterskil.workflow.engine.service.TokenService;
import com.kaaterskil.workflow.engine.variable.BooleanType;
import com.kaaterskil.workflow.engine.variable.ByteArrayType;
import com.kaaterskil.workflow.engine.variable.DateType;
import com.kaaterskil.workflow.engine.variable.FloatType;
import com.kaaterskil.workflow.engine.variable.IntegerType;
import com.kaaterskil.workflow.engine.variable.LongType;
import com.kaaterskil.workflow.engine.variable.StringType;
import com.kaaterskil.workflow.engine.variable.VariableTypeHelper;

@Component
public class ProcessEngineService {

    public static ProcessEngineService create() {
        return new ProcessEngineService();
    }

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private DeploymentService deploymentService;

    private CommandInterceptor commandInvoker;
    private CommandInterceptor commandExecutor;
    private List<CommandInterceptor> commandInterceptors;

    private CacheManager cacheManager;
    private BpmDeployer bpmDeployer;
    private BpmParseHelper bpmParseHelper;
    private List<Deployer> deployers;
    private DeploymentCache<ProcessDefinitionCacheEntry> processDefinitionCache;
    private VariableTypeHelper variableTypeHelper;

    private CommandContextFactory commandContextFactory;
    private ProcessEngine processEngine;

    protected Map<String, JobHandler> jobHandlers;
    private AsyncExecutor asyncExecutor;
    protected int lockTimeAsyncJobWaitTimeSeconds = 60;
    protected long asyncFailedJobWaitTimeMillis = 10 * 1000;

    private ListenerFactory listenerFactory;
    private ActivityBehaviorFactory activityBehaviorFactory;
    private BehaviorHelper behaviorHelper;
    private DelegateInterceptor delegateInterceptor;

    private Map<EventSubscriptionType, EventHandler> eventHandlers;
    private WorkflowEventDispatcher eventDispatcher;
    private List<WorkflowEventListener> eventListeners;
    private Map<WorkflowEventType, List<WorkflowEventListener>> typedEventListeners;

    protected ProcessEngineService() {
    }

    /*---------- Methods ----------*/

    public ProcessEngine createProcessEngine() {
        init();
        processEngine = new ProcessEngine(this);
        return processEngine;
    }

    private void init() {
        initVariableTypeHelper();
        initCommandContextFactory();
        initCommandExecutors();
        initServices();
        initListenerFactory();
        initActivityBehaviorFactory();
        initBpmParseHelper();
        initBehaviorHelper();
        initDeployers();
        initJobHandlers();
        initAsyncExecutor();
        initDelegateInterceptor();
        initEventHandlers();
        initEventDispatcher();
    }

    private void initVariableTypeHelper() {
        if(variableTypeHelper == null) {
            variableTypeHelper = new VariableTypeHelper();

            variableTypeHelper.addType(new BooleanType());
            variableTypeHelper.addType(new IntegerType());
            variableTypeHelper.addType(new LongType());
            variableTypeHelper.addType(new DateType());
            variableTypeHelper.addType(new FloatType());
            variableTypeHelper.addType(new StringType());
            variableTypeHelper.addType(new ByteArrayType());
        }
    }

    private void initCommandContextFactory() {
        if (commandContextFactory == null) {
            commandContextFactory = new CommandContextFactory();
        }
        commandContextFactory.setProcessEngineService(this);
    }

    private void initCommandExecutors() {
        initCommandInvoker();
        initCommandInterceptors();
        initCommandExecutor();
    }

    private void initCommandInvoker() {
        if (commandInvoker == null) {
            commandInvoker = new CommandInvoker();
        }
    }

    private void initCommandInterceptors() {
        if (commandInterceptors == null) {
            commandInterceptors = new ArrayList<CommandInterceptor>();
            commandInterceptors.addAll(getDefaultCommandInterceptors());
            commandInterceptors.add(commandInvoker);
        }
    }

    private List<CommandInterceptor> getDefaultCommandInterceptors() {
        final List<CommandInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new LogInterceptor());
        interceptors.add(new CommandContextInterceptor(commandContextFactory, this));
        return interceptors;
    }

    private void initCommandExecutor() {
        if (commandExecutor == null) {
            commandExecutor = initInterceptorChain(commandInterceptors);
        }
    }

    private CommandInterceptor initInterceptorChain(List<CommandInterceptor> chain) {
        final int len = chain.size() - 1;
        for (int i = 0; i < len; i++) {
            chain.get(i).setNext(chain.get(i + 1));
        }
        return chain.get(0);
    }

    private void initServices() {
        initService(runtimeService);
        initService(repositoryService);
    }

    private void initService(AbstractService service) {
        service.setCommandExecutor(commandExecutor);
    }

    private void initListenerFactory() {
        if (listenerFactory == null) {
            listenerFactory = new ListenerFactoryImpl();
        }
    }

    private void initActivityBehaviorFactory() {
        if(activityBehaviorFactory == null) {
            activityBehaviorFactory = new ActivityBehaviorFactory();
        }
    }

    private void initBpmParseHelper() {
        if (bpmParseHelper == null) {
            bpmParseHelper = new BpmParseHelper();

            final List<ParseHandler> defaultHandlers = new ArrayList<>();
            defaultHandlers.add(new ProcessParseHandler());
            defaultHandlers.add(new SequenceFlowParseHandler());
            defaultHandlers.add(new StartEventParseHandler());

            bpmParseHelper.addHandlers(defaultHandlers);
        }
    }

    private void initBehaviorHelper() {
        if(behaviorHelper == null) {
            behaviorHelper = new BehaviorHelper();

            behaviorHelper.addHandler(new ServiceTaskBehaviorHandler());
        }
    }

    private void initDeployers() {
        if (deployers == null) {
            deployers = getDefaultDeployers();
        }
        deploymentService.setDeployers(deployers);

        if (processDefinitionCache == null) {
            processDefinitionCache = new DeploymentCacheImpl<ProcessDefinitionCacheEntry>();
        }
        deploymentService.setProcessDefinitionCache(processDefinitionCache);
    }

    private List<Deployer> getDefaultDeployers() {
        final List<Deployer> defaultDeployers = new ArrayList<>();

        if (bpmDeployer == null) {
            bpmDeployer = new BpmDeployer();
        }
        if (cacheManager == null) {
            cacheManager = new CacheManager();
        }
        bpmDeployer.setCacheManager(cacheManager);
        defaultDeployers.add(bpmDeployer);

        return defaultDeployers;
    }

    private void initJobHandlers() {
        if (jobHandlers == null) {
            jobHandlers = new HashMap<String, JobHandler>();

            final AsyncJobHandler asyncJobHandler = new AsyncJobHandler();
            jobHandlers.put(asyncJobHandler.getType(), asyncJobHandler);
        }
    }

    private void initAsyncExecutor() {
        if (asyncExecutor == null) {
            asyncExecutor = new BaseAsyncExecutor();
        }
        asyncExecutor.setCommandExecutor(commandExecutor);
    }

    private void initDelegateInterceptor() {
        if (delegateInterceptor == null) {
            delegateInterceptor = new DelegateInterceptor();
        }
    }

    private void initEventHandlers() {
        if (eventHandlers == null) {
            eventHandlers = new HashMap<EventSubscriptionType, EventHandler>();

            final SignalEventHandler signalEventHandler = new SignalEventHandler();
            eventHandlers.put(signalEventHandler.getEventHandlerType(), signalEventHandler);

            final CompensationEventHandler compensationEventHandler = new CompensationEventHandler();
            eventHandlers.put(compensationEventHandler.getEventHandlerType(),
                    compensationEventHandler);

            final MessageEventHandler messageEventHandler = new MessageEventHandler();
            eventHandlers.put(messageEventHandler.getEventHandlerType(), messageEventHandler);
        }
    }

    private void initEventDispatcher() {
        if (eventDispatcher == null) {
            eventDispatcher = new WorkflowEventDispatcherImpl();
        }

        if (eventListeners != null) {
            for (final WorkflowEventListener listener : eventListeners) {
                eventDispatcher.addEventListener(listener);
            }
        }

        if (typedEventListeners != null) {
            for (final WorkflowEventType key : typedEventListeners.keySet()) {
                final WorkflowEventType[] types = { key };
                for (final WorkflowEventListener listener : typedEventListeners.get(key)) {
                    eventDispatcher.addEventListener(listener, types);
                }
            }
        }
    }

    public TokenService getTokenService() {
        return repositoryService.getTokenService();
    }

    public EventHandler getEventHandler(EventSubscriptionType eventType) {
        return eventHandlers.get(eventType);
    }

    /*---------- Getter/Setters ----------*/

    public VariableTypeHelper getVariableTypeHelper() {
        return variableTypeHelper;
    }

    public void setVariableTypeHelper(VariableTypeHelper variableTypeHelper) {
        this.variableTypeHelper = variableTypeHelper;
    }

    public RuntimeService getRuntimeService() {
        return runtimeService;
    }

    public void setRuntimeService(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    public RepositoryService getRepositoryService() {
        return repositoryService;
    }

    public void setRepositoryService(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    public CommandInterceptor getCommandInvoker() {
        return commandInvoker;
    }

    public void setCommandInvoker(CommandInterceptor commandInvoker) {
        this.commandInvoker = commandInvoker;
    }

    public CommandInterceptor getCommandExecutor() {
        return commandExecutor;
    }

    public void setCommandExecutor(CommandInterceptor commandExecutor) {
        this.commandExecutor = commandExecutor;
    }

    public List<CommandInterceptor> getCommandInterceptors() {
        return commandInterceptors;
    }

    public void setCommandInterceptors(List<CommandInterceptor> commandInterceptors) {
        this.commandInterceptors = commandInterceptors;
    }

    public DeploymentService getDeploymentService() {
        return deploymentService;
    }

    public void setDeploymentService(DeploymentService deploymentService) {
        this.deploymentService = deploymentService;
    }

    public CacheManager getCacheManager() {
        return cacheManager;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public BpmDeployer getBpmDeployer() {
        return bpmDeployer;
    }

    public void setBpmDeployer(BpmDeployer bpmDeployer) {
        this.bpmDeployer = bpmDeployer;
    }

    public BpmParseHelper getBpmParseHelper() {
        return bpmParseHelper;
    }

    public void setBpmParseHelper(BpmParseHelper bpmParseHelper) {
        this.bpmParseHelper = bpmParseHelper;
    }

    public BehaviorHelper getBehaviorHelper() {
        return behaviorHelper;
    }

    public void setBehaviorHelper(BehaviorHelper behaviorHelper) {
        this.behaviorHelper = behaviorHelper;
    }

    public DeploymentCache<ProcessDefinitionCacheEntry> getProcessDefinitionCache() {
        return processDefinitionCache;
    }

    public void setProcessDefinitionCache(
            DeploymentCache<ProcessDefinitionCacheEntry> processDefinitionCache) {
        this.processDefinitionCache = processDefinitionCache;
    }

    public ListenerFactory getListenerFactory() {
        return listenerFactory;
    }

    public void setListenerFactory(ListenerFactory listenerFactory) {
        this.listenerFactory = listenerFactory;
    }

    public ActivityBehaviorFactory getActivityBehaviorFactory() {
        return activityBehaviorFactory;
    }

    public void setActivityBehaviorFactory(ActivityBehaviorFactory activityBehaviorFactory) {
        this.activityBehaviorFactory = activityBehaviorFactory;
    }

    public Map<String, JobHandler> getJobHandlers() {
        return jobHandlers;
    }

    public void setJobHandlers(Map<String, JobHandler> jobHandlers) {
        this.jobHandlers = jobHandlers;
    }

    public AsyncExecutor getAsyncExecutor() {
        return asyncExecutor;
    }

    public void setAsyncExecutor(AsyncExecutor asyncExecutor) {
        this.asyncExecutor = asyncExecutor;
    }

    public int getLockTimeAsyncJobWaitTimeSeconds() {
        return lockTimeAsyncJobWaitTimeSeconds;
    }

    public void setLockTimeAsyncJobWaitTimeSeconds(int lockTimeAsyncJobWaitTimeSeconds) {
        this.lockTimeAsyncJobWaitTimeSeconds = lockTimeAsyncJobWaitTimeSeconds;
    }

    public long getAsyncFailedJobWaitTimeMillis() {
        return asyncFailedJobWaitTimeMillis;
    }

    public void setAsyncFailedJobWaitTimeMillis(long asyncFailedJobWaitTimeMillis) {
        this.asyncFailedJobWaitTimeMillis = asyncFailedJobWaitTimeMillis;
    }

    public DelegateInterceptor getDelegateInterceptor() {
        return delegateInterceptor;
    }

    public void setDelegateInterceptor(DelegateInterceptor delegateInterceptor) {
        this.delegateInterceptor = delegateInterceptor;
    }

    public Map<EventSubscriptionType, EventHandler> getEventHandlers() {
        return eventHandlers;
    }

    public void setEventHandlers(Map<EventSubscriptionType, EventHandler> eventHandlers) {
        this.eventHandlers = eventHandlers;
    }

    public WorkflowEventDispatcher getEventDispatcher() {
        return eventDispatcher;
    }

    public void setEventDispatcher(WorkflowEventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    public List<WorkflowEventListener> getEventListeners() {
        return eventListeners;
    }

    public void setEventListeners(List<WorkflowEventListener> eventListeners) {
        this.eventListeners = eventListeners;
    }

    public Map<WorkflowEventType, List<WorkflowEventListener>> getTypedEventListeners() {
        return typedEventListeners;
    }

    public void setTypedEventListeners(
            Map<WorkflowEventType, List<WorkflowEventListener>> typedEventListeners) {
        this.typedEventListeners = typedEventListeners;
    }
}
