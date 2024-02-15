package model;

public interface Shape {

    void draw();

    void resize();

    void move();

    void delete();

    void addChild(Shape shape);
}
