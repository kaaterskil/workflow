package com.kaaterskil.workflow.bpm;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.kaaterskil.workflow.bpm.common.Error;
import com.kaaterskil.workflow.bpm.common.Message;
import com.kaaterskil.workflow.bpm.common.event.Signal;
import com.kaaterskil.workflow.bpm.common.process.Process;
import com.kaaterskil.workflow.util.CollectionUtil;

@XmlRootElement(name = "model")
@XmlAccessorType(XmlAccessType.NONE)
public class BpmModel {

    @XmlElement(name = "signal", type = Signal.class, required = false)
    List<Signal> signals = new ArrayList<>();

    @XmlElement(name = "message", type = Message.class, required = false)
    List<Message> messages = new ArrayList<>();

    @XmlElement(name = "error", type = Error.class, required = false)
    List<Error> errors = new ArrayList<>();

    @XmlElement(name = "process", type = Process.class, required = true)
    List<Process> processes = new ArrayList<>();

    /*---------- Methods ----------*/

    public Signal getSignal(String id) {
        if (CollectionUtil.isNotEmpty(signals)) {
            for (final Signal signal : signals) {
                if (signal.getId().equals(id)) {
                    return signal;
                }
            }
        }
        return null;
    }

    public Message getMessage(String id) {
        if (CollectionUtil.isNotEmpty(messages)) {
            for (final Message message : messages) {
                if (message.getId().equals(id)) {
                    return message;
                }
            }
        }
        return null;
    }

    public Error getError(String errorCode) {
        if (CollectionUtil.isNotEmpty(errors)) {
            for (final Error error : errors) {
                if (error.getErrorCode().equals(errorCode)) {
                    return error;
                }
            }
        }
        return null;
    }

    public boolean containsErrorRef(String errorCode) {
        if(CollectionUtil.isNotEmpty(errors)) {
            for(final Error each : errors) {
                if(each.getErrorCode() != null && each.getErrorCode().equals(errorCode)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Process getProcess() {
        return getMainProcess();
    }

    public Process getMainProcess() {
        if (CollectionUtil.isNotEmpty(processes)) {
            return processes.get(0);
        }
        return null;
    }

    public Process getProcessById(String id) {
        if (CollectionUtil.isNotEmpty(processes)) {
            for (final Process process : processes) {
                if (process.getId() != null && process.getId().equals(id)) {
                    return process;
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return String.format("BpmModel [signals=%s, processes=%s]", signals, processes);
    }

    /*---------- Getter/Setters ----------*/

    public List<Signal> getSignals() {
        return signals;
    }

    public void setSignals(List<Signal> signals) {
        this.signals = signals;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }

    public List<Process> getProcesses() {
        return processes;
    }

    public void setProcesses(List<Process> processes) {
        this.processes = processes;
    }
}
