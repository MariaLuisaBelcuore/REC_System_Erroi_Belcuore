package it.unisalento.rec.rec_task.dto;

public class TaskDTO {
    private String id;
    private String name;
    private String description;
    private int executionTime;
    private String linkGit;
    private String os;
    private int memory;
    private String processorModel;
    private float processorVelocity;
    private String clientEmail;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public int getExecutionTime() {
        return executionTime;
    }
    public void setExecutionTime(int executionTime) {
        this.executionTime = executionTime;
    }
    public String getLinkGit() {
        return linkGit;
    }
    public void setLinkGit(String linkGit) {
        this.linkGit = linkGit;
    }
    public String getOs() {
        return os;
    }
    public void setOs(String os) {
        this.os = os;
    }
    public int getMemory() {
        return memory;
    }
    public void setMemory(int memory) {
        this.memory = memory;
    }
    public String getProcessorModel() {
        return processorModel;
    }
    public void setProcessorModel(String processorModel) {
        this.processorModel = processorModel;
    }
    public float getProcessorVelocity() {
        return processorVelocity;
    }
    public void setProcessorVelocity(float processorVelocity) {
        this.processorVelocity = processorVelocity;
    }
    public String getClientEmail() {
        return clientEmail;
    }
    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }
}
