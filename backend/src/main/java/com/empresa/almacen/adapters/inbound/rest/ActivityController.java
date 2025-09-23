package com.empresa.almacen.adapters.inbound.rest;

import com.empresa.almacen.domain.model.Activity;
import com.empresa.almacen.application.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    @GetMapping("/latest")
    public List<Activity> getLatestActivities() {
        return activityService.getLatestActivities();
    }
}

