package controller;

import view.GUI;

public class Controller {

    GUI gui;

    public Controller(GUI gui) {
        this.gui = gui;
    }

    public static void main(String[] args) {
        GUI.launch(GUI.class);
    }
}
