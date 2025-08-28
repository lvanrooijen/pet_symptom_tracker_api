package com.laila.pet_symptom_tracker.entities.user;

import com.laila.pet_symptom_tracker.mainconfig.Routes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Routes.USERS)
@RequiredArgsConstructor
@CrossOrigin(origins = "${pet_symptom_tracker.cors}")
public class UserController {}
