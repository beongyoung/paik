package com.paik.app.http;

public class NotificationModel {
    public String to;
    public Notification nofification = new Notification();
    public static class Notification{
        public String title;
        public String text;
    }

}
