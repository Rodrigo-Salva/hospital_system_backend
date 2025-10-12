package com.example.hospitalsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HospitalSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(HospitalSystemApplication.class, args);
        System.out.println("=================================");
        System.out.println("Hospital API corriendo en puerto 9090");
        System.out.println("=================================");
    }

}
