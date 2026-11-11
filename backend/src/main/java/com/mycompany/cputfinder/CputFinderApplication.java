/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author paci
 */
package com.mycompany.cputfinder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the web version of cputFinder.
 *
 * This replaces launching RoomSearchPanel (Swing) directly. Run this
 * class's main() (or `mvn spring-boot:run`), then open
 * http://localhost:8080/User_Profile.html in a browser.
 *
 * Sitting in the base package com.mycompany.cputfinder means Spring Boot's
 * component scan automatically picks up everything underneath it,
 * including the new com.mycompany.cputfinder.web package below.
 */
@SpringBootApplication
public class CputFinderApplication {
    public static void main(String[] args) {
        SpringApplication.run(CputFinderApplication.class, args);
    }
}
