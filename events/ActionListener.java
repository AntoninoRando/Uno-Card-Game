package events;

@FunctionalInterface
public interface ActionListener {
    public void requestChange(String type, String data);
}
