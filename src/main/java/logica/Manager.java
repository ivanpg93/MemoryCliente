/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import java.util.ArrayList;
import java.util.List;
import presentacion.PresentationLayer;

/**
 * Clase Singleton encargada de gestionar los controllers Permite interactuar
 * entre pantallas.
 */
public class Manager {

    private static Manager single_instance = null;
    private final List<PresentationLayer> controllers;

    private Manager() {
        this.controllers = new ArrayList<>();
    }

    /**
     * *
     * Añadimos un controlador
     *
     * @param x controlador
     */
    public void addController(PresentationLayer x) {
        if (controllers.isEmpty()) {
            this.controllers.add(x);
           
            return;
        }

        if (getController(x.getClass()) == null) {
            this.controllers.add(x);
        } else {
            this.controllers.remove(getController(x.getClass()));
            this.controllers.add(x);
        }
    }

    /**
     * *
     * Obtiene un controlador según el nombre de la clase.
     *
     * @param type la clase en cuestión.
     * @return
     */
    public Object getController(Class type) {
        Object ret = null;

        for (Object c : controllers) {
            if (c.getClass() == type) {
                ret = c;
            }
        }

        return ret;
    }

    /**
     * *
     * Devuelve la instancia del Singleton
     *
     * @return instancia del singleton
     */
    public static Manager getInstance() {
        if (single_instance == null) {
            single_instance = new Manager();
        }

        return single_instance;
    }
}
