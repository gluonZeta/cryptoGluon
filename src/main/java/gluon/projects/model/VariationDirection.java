package gluon.projects.model;

public enum VariationDirection {
    UP("up"),
    DOWN("down"),
    RAS("ras");

    private final String direction;

    VariationDirection(String direction) {
        this.direction = direction;
    }

    public String getDirection() {
        return direction;
    }
}
