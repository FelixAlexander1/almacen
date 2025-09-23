package com.empresa.almacen.application.service;

import com.empresa.almacen.domain.model.Activity;
import com.empresa.almacen.adapters.outbound.persistence.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;

    public List<Activity> getLatestActivities() {
        return activityRepository.findTop10ByOrderByCreatedAtDesc();
    }

    public Activity logActivity(String description) {
        Activity activity = new Activity();
        activity.setDescription(description);
        activity.setCreatedAt(LocalDateTime.now());
        return activityRepository.save(activity);
    }
}

