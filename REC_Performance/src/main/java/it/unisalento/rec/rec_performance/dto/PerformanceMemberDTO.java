package it.unisalento.rec.rec_performance.dto;

import java.time.LocalDateTime;

public class PerformanceMemberDTO {
    private String id;
    private LocalDateTime calculationDate;
    private float totalEnergyAvailable;
    private float totalEnergy;
    private float energyResold;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getCalculationDate() {
        return calculationDate;
    }

    public void setCalculationDate(LocalDateTime calculationDate) {
        this.calculationDate = calculationDate;
    }

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

    public float getEnergyResold() {return energyResold;}

    public void setEnergyResold(float energyResold) {
        this.energyResold = energyResold;
    }
}
