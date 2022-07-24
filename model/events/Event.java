package model.events;

import java.util.Objects;

public class Event {
    private String category;
    private String label;

    public String getCategory() {
        return category;
    }

    public String getLabel() {
        return label;
    }

    protected static String getCategoryOf(String label) {
        switch (label) {
            case "enemyTurn":
            case "humanTurn":
            case "gameStart":
                return "turn";
            default:
                return "";
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(4);
        sb.append("#").append(getCategory()).append(" ").append(label);
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        // TODO penso manchi qualcosa, il faralli aveva spiegato come fare
        if (o == null)
            return false;
        if (!o.getClass().equals(getClass()))
            return false;
        return getLabel().equals(((Event) o).getLabel());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this); // TODO non so se vada bene
    }
}
