package it.unisalento.rec.rec_energyresources.dto;

public class ResourceBillDTO {
    private String description;
    private String typeEnergy;
    private float kWh;
    private String os;
    private int memory;
    private String processorModel;
    private float processorVelocity;
    private float cost;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTypeEnergy() {
        return typeEnergy;
    }

    public void setTypeEnergy(String typeEnergy) {
        this.typeEnergy = typeEnergy;
    }

    public float getkWh() {
        return kWh;
    }

    public void setkWh(float kWh) {
        this.kWh = kWh;
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

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }
}
