package br.edu.ufrn.airrunwaymanagement.model;

import br.edu.ufrn.airrunwaymanagement.enums.AircraftState;
import br.edu.ufrn.airrunwaymanagement.service.AirportRunway;

public class Airplane extends Thread {
    private final AirportRunway airportRunway;
    public final int name;
    private AircraftState aircraftState;

    public Airplane(AirportRunway airportRunway, AircraftState aircraftState, int name) {
        this.airportRunway = airportRunway;
        this.aircraftState = aircraftState;
        this.name = name;
    }

    public void run() {
        try {
           airportRunway.executeRequest(this);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Falha na operação da aeronave #" + name);
        }
    }

    public AircraftState getAircraftState() {
        return aircraftState;
    }
}