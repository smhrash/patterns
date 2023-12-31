package com.upgrad.patterns.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Stat {

    @JsonProperty("confirmed")
    private Double confirmed;

    @JsonProperty("deaths")
    private Integer deaths;

    @JsonProperty("recovered")
    private Integer recovered;

    public Double getConfirmed() {
        return this.confirmed;
    }
}