package com.example.demo.maintenance.domain.model;

import com.example.demo.inventory.domain.model.PlantInventoryItem;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class MaintenancePlan {
    @Id
    @GeneratedValue
    Long id;

    int year_of_action;

    @OneToMany(cascade={CascadeType.MERGE})
    List<MaintenanceTask> tasks;

    @ManyToOne
    PlantInventoryItem plant;
}

