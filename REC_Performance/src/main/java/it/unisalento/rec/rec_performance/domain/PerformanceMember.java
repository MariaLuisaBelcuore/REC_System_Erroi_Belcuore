package it.unisalento.rec.rec_performance.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document("performanceMember")
public class PerformanceMember {
    @Id
    private String id;
    private String memberEmail;
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

    public String getMemberEmail() {
        return memberEmail;
    }

    public void setMemberEmail(String memberEmail) {
        this.memberEmail = memberEmail;
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

    public float getEnergyResold() {
        return energyResold;
    }

    public void setEnergyResold(float energyResold) {
        this.energyResold = energyResold;
    }
}
