package it.unisalento.rec.rec_energyresources.dto;

public class ResourceRequestDTO {
    private int availableTime;
    private String os;
    private int memory;
    private String processorModel;
    private float processorVelocity;

    public int getAvailableTime() {return availableTime;}

    public void setAvailableTime(int availableTime) {this.availableTime = availableTime;}

    public String getOs() {return os;}

    public void setOs(String os) {this.os = os;}

    public int getMemory() {return memory;}

    public void setMemory(int memory) {this.memory = memory;}

    public String getProcessorModel() {return processorModel;}

    public void setProcessorModel(String processorModel) {this.processorModel = processorModel;}

    public float getProcessorVelocity() {return processorVelocity;}

    public void setProcessorVelocity(float processorVelocity) {this.processorVelocity = processorVelocity;}
}
