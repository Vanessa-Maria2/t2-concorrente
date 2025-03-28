package br.edu.ufrn.airrunwaymanagement.service;

import br.edu.ufrn.airrunwaymanagement.model.Airplane;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AirportRunway {
    private final Lock lock = new ReentrantLock();
    private final Condition runwayClear = lock.newCondition();
    private final AtomicInteger landingCount = new AtomicInteger(0);
    private final AtomicInteger takeOffCount = new AtomicInteger(0);

    public void executeRequest(Airplane airplane) throws InterruptedException {
        switch (airplane.getAircraftState()) {
            case LAND -> this.landingRequest(airplane);
            case TAKEOFF -> this.takeOffRequest(airplane);
        }
    }

    private void landingRequest(Airplane airplane) throws InterruptedException {
        landingCount.incrementAndGet();
        System.out.println("Aeronave #" + airplane.name
                + ": Solicitando permissão para pouso | Aeronaves aguardando pouso: " + landingCount);
        lock.lock();
        System.out.println("Aeronave #" + airplane.name + ": LOCK");
        try {
            System.out.println("Aeronave #" + airplane.name + ": Pouso em andamento...");
            Thread.sleep(2000);
            System.out.println("Aeronave #" + airplane.name + ": Pouso finalizado...");
            landingCount.decrementAndGet();
        } finally {
            runwayClear.signal();
            System.out.println("Aeronave #" + airplane.name + ": UN LOCK");
            lock.unlock();
        }
    }

    private void takeOffRequest(Airplane airplane) throws InterruptedException {
        takeOffCount.incrementAndGet();
        System.out.println("Aeronave #" + airplane.name
                + ": Solicitando permissão para decolar | Aeronaves aguardando decolar: " + takeOffCount);
        lock.lock();
        System.out.println("Aeronave #" + airplane.name + ": LOCK");
        try {
            while (landingCount.get() > 0) {
                System.out.println("Aeronave #" + airplane.name + ": CHECK AWAIT");
                runwayClear.await();
                System.out.println("Aeronave #" + airplane.name + ": GET THE NOTIFY");
            }
            System.out.println("Aeronave #" + airplane.name + ": Decolagem em andamento...");
            Thread.sleep(1000);
            System.out.println("Aeronave #" + airplane.name + ": Decolagem finalizada...");
            takeOffCount.decrementAndGet();
        } finally {
            runwayClear.signal();
            System.out.println("Aeronave #" + airplane.name + ": UN LOCK");
            lock.unlock();
        }
    }
}