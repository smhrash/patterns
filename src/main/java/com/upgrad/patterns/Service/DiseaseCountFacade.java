package com.upgrad.patterns.Service;

import com.upgrad.patterns.Constants.SourceType;
import com.upgrad.patterns.Interfaces.IndianDiseaseStat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiseaseCountFacade {

    private IndiaDiseaseStatFactory indiaDiseaseStatFactory;

    @Autowired
    public DiseaseCountFacade(IndiaDiseaseStatFactory indiaDiseaseStatFactory) {
        this.indiaDiseaseStatFactory = indiaDiseaseStatFactory;
    }

    public Object getDiseaseShCount() {
        IndianDiseaseStat strategy = indiaDiseaseStatFactory.GetInstance(SourceType.DiseaseSh);
        return strategy.GetActiveCount();
    }

    public Object getJohnHopkinCount() {
        IndianDiseaseStat strategy = indiaDiseaseStatFactory.GetInstance(SourceType.JohnHopkins);
        return strategy.GetActiveCount();
    }

    public Object getInfectedRatio(String sourceType) throws IllegalArgumentException {
        try {
            Float population = 900000000F; // Assuming this is the population
            SourceType sourceEnum = SourceType.valueOf(sourceType);
            Float activeCount = Float.valueOf(indiaDiseaseStatFactory.GetInstance(sourceEnum).GetActiveCount());
            Float percent = Float.valueOf((activeCount / population));

            return String.format("%.3f", percent * 100);
        } catch (Exception e) {
            String message = String.format("Invalid source type specified. Available source type (%s, %s)", SourceType.DiseaseSh, SourceType.JohnHopkins);
            throw new IllegalArgumentException(message);
        }
    }
}
