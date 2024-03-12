package entity;

import jakarta.persistence.*;

@Entity
public class Settings {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    private boolean isDrawLengths;

    private boolean isDrawGrid;

    private double gridHeight;

    private double gridWidth;

    private int gridSize;

}
