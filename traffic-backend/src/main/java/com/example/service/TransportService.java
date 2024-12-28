package com.example.service;

import com.example.model.entity.LatLng;
import com.example.model.entity.Task;

import java.util.List;

public interface TransportService {
    List<List<LatLng>> getRoutes(List<Task> tasks);

    void startCarMovement(Long vehicleId, List<LatLng> route);
}
