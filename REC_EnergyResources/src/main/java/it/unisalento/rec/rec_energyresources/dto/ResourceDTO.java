package it.unisalento.rec.rec_energyresources.dto;

public class ResourceDTO {
    private String id;
    private String description;
    private String owner;
    private String memberEmail;
    private String typeEnergy;
    private int availableTime;
    private float kWh;
    private String os;
    private int memory;
    private String processorModel;
    private float processorVelocity;
    private boolean availability;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {return description;}

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {this.owner = owner;}

    public String getMemberEmail() {return memberEmail;}

    public void setMemberEmail(String memberEmail) {
        this.memberEmail = memberEmail;
    }

    public String getTypeEnergy() {
        return typeEnergy;
    }

    public void setTypeEnergy(String typeEnergy) {
        this.typeEnergy = typeEnergy;
    }

    public int getAvailableTime() {
        return availableTime;
    }

    public void setAvailableTime(int availableTime) {
        this.availableTime = availableTime;
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

    public boolean isAvailability() {return availability;}

    public void setAvailability(boolean availability) {this.availability = availability;}
}
