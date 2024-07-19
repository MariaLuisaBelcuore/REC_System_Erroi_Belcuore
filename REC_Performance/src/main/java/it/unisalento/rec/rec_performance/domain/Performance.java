package it.unisalento.rec.rec_performance.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document("performance")
public class Performance {
    @Id
    private String id;
    private LocalDateTime calculationDate;
    private float energyResoldPercentage;
    private float totalEarnings;
    private float totalEnergyAvailable;
    private float totalEnergyConsumed;

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

    public float getEnergyResoldPercentage() {
        return energyResoldPercentage;
    }

    public void setEnergyResoldPercentage(float energyResoldPercentage) {this.energyResoldPercentage = energyResoldPercentage;}

    public float getTotalEarnings() {
        return totalEarnings;
    }

    public void setTotalEarnings(float totalEarnings) {
        this.totalEarnings = totalEarnings;
    }

    public float getTotalEnergyAvailable() {
        return totalEnergyAvailable;
    }

    public void setTotalEnergyAvailable(float totalEnergyAvailable) {this.totalEnergyAvailable = totalEnergyAvailable;}

    public float getTotalEnergyConsumed() {
        return totalEnergyConsumed;
    }

    public void setTotalEnergyConsumed(float totalEnergyConsumed) {
        this.totalEnergyConsumed = totalEnergyConsumed;
    }
}