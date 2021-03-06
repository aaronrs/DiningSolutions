package net.astechdesign.diningsolutions.model;

import java.io.Serializable;
import java.util.UUID;

public abstract class Model implements Serializable {

    private UUID id;

    protected Model(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public String getDbId() {
        return id == null ? null : id.toString();
    }
}
