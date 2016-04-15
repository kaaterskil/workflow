package com.kaaterskil.workflow.engine.parser;

public enum ImplementationType {

    CLASS("class"),
    EXPRESSION("expression"),
    DELEGATE_EXPRESSION("delegateExpression"),
    INSTANCE("instance"),
    THROW_SIGNAL_EVENT("throwSignalEvent"),
    THROW_MESSAGE_EVENT("throwMessageEvent"),
    THROW_ERROR_EVENT("throwErrorEvent"),
    INVALID_THROW_EVENT("invliadThrowEvent");

    private String meaning;

    private ImplementationType(String meaning) {
        this.meaning = meaning;
    }

    public String getMeaning() {
        return meaning;
    }

}
