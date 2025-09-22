package com.empresa.almacen.adapters.outbound.persistence.repository;

import com.empresa.almacen.domain.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findTop10ByOrderByCreatedAtDesc();
}

