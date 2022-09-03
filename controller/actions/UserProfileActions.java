package controller.actions;

import events.ActionListener;

public abstract class UserProfileActions {
    private static ActionListener al;

    public static void setActionListener(ActionListener listener) {
        al = listener;
    }

    public static void changeNick(String nickname) {
        al.requestChange("nick", nickname);
    }

    public static void changeIcon(String icon) {
        al.requestChange("icon", icon);
    }
}
