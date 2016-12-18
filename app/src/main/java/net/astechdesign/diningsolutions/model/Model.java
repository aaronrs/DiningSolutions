package net.astechdesign.diningsolutions.model;

import java.util.List;
import java.util.UUID;

public abstract class Model {

    public abstract UUID getId();

    public abstract List getChildren();

}
