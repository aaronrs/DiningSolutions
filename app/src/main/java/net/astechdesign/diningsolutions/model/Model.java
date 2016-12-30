package net.astechdesign.diningsolutions.model;

import java.io.Serializable;

public abstract class Model implements Serializable {

    public final int id;

    protected Model(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
