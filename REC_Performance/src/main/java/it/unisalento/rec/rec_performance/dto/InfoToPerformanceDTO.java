package it.unisalento.rec.rec_performance.dto;

public class InfoToPerformanceDTO {
    private float totalEnergyAvailable;
    private float totalEnergy;

    public float getTotalEnergyAvailable() {
        return totalEnergyAvailable;
    }

    public void setTotalEnergyAvailable(float totalEnergyAvailable) {this.totalEnergyAvailable = totalEnergyAvailable;}

    public float getTotalEnergy() {
        return totalEnergy;
    }

    public void setTotalEnergy(float totalEnergy) {
        this.totalEnergy = totalEnergy;
    }
}
