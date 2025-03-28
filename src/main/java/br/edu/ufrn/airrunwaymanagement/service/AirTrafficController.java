package br.edu.ufrn.airrunwaymanagement.service;

import br.edu.ufrn.airrunwaymanagement.enums.AircraftState;
import br.edu.ufrn.airrunwaymanagement.model.Airplane;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class AirTrafficController {
    private static final int MIN_AIRPLANES = 3;
    private static final int MAX_AIRPLANES = 10;
    private final AirportRunway airportRunway;
    private final List<Airplane> airplanes;

    public AirTrafficController() {
        this.airportRunway = new AirportRunway();
        this.airplanes = new ArrayList<>();
    }

    public void initializeTrafficSystem() {
        Scanner terminalInputscanner = null;

        try {
            terminalInputscanner = new Scanner(System.in);

            System.out.println("=== Sistema de Controle de Tráfego Aéreo ===");
            System.out.println("1 - Inserir quantidade de aviões manualmente");
            System.out.println("2 - Gerar Aviões aleatoriamente");
            System.out.print("Escolha uma opção: ");

            int userOption = terminalInputscanner.nextInt();
            int numberOfAirplanes;

            switch (userOption) {
                case 1:
                    System.out.print("Insira a quantidade de aviões (mínimo 2): ");
                    numberOfAirplanes = terminalInputscanner.nextInt();

                    if (numberOfAirplanes < 2) {
                        System.out.println("Ajustando para satisfazer a quantidade mínima...");
                        numberOfAirplanes = 2;
                    }

                    createAirplanes(numberOfAirplanes);
                    break;
                case 2:
                    numberOfAirplanes = generateAirplaneNumber(MIN_AIRPLANES, MAX_AIRPLANES);
                    System.out.println("Aviões gerados pelo sistema: " + numberOfAirplanes + " aviões");
                    createAirplanes(numberOfAirplanes);
                    break;
                default:
                    System.out.println("Opção inválida! Gerando aviões automaticamente...");
                    numberOfAirplanes = generateAirplaneNumber(MIN_AIRPLANES, MAX_AIRPLANES);
                    System.out.println("Aviões gerados pelo sistema: " + numberOfAirplanes + " aviões");
                    createAirplanes(numberOfAirplanes);
            }

            startTrafficSimulation();

        } finally {
            if (terminalInputscanner != null) {
                terminalInputscanner.close();
            }
        }
    }

    private void createAirplanes(int quantity) {
        Random random = new Random();
        AircraftState[] aircraftStateList = AircraftState.values();

        for (int i = 1; i <= quantity; i++) {
            AircraftState aircraftState = aircraftStateList[random.nextInt(aircraftStateList.length)];
            Airplane airplane = new Airplane(airportRunway, aircraftState, i);

            airplanes.add(airplane);
        }
    }

    private void startTrafficSimulation() {
        System.out.println("\nIniciando simulação do aeroporto...\n");

        for (Airplane airplane : airplanes) {
            airplane.start();
        }

        for (Airplane airplane : airplanes) {
            try {
                airplane.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Erro ao aguardar finalização do avião #" + airplane.name);
            }
        }
        System.out.println("\nTodas as aeronaves completaram suas operações.");
    }

    private int generateAirplaneNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }
}