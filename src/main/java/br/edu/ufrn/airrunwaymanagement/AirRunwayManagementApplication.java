package br.edu.ufrn.airrunwaymanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.edu.ufrn.airrunwaymanagement.service.AirTrafficController;

@SpringBootApplication
public class AirRunwayManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(AirRunwayManagementApplication.class, args);

        AirTrafficController controller = new AirTrafficController();
        controller.initializeTrafficSystem();
    }
}
