package com.laila.pet_symptom_tracker;

import com.laila.pet_symptom_tracker.mainconfig.CustomBanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PetSymptomTrackerApplication {

  public static void main(String[] args) {
    SpringApplication application = new SpringApplication(PetSymptomTrackerApplication.class);
    application.setBanner(new CustomBanner());
    application.run(args);
  }
}
