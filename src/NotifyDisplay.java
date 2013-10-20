import java.util.ArrayList;

public class NotifyDisplay {
    private static final String path = "/usr/bin/notify-send";
    ArrayList<String[]> notificationQueue;
    NotifyDisplay(){
        notificationQueue = new ArrayList<String[]>();
    }
    private void show(String title, String message) throws Exception {
        String[] cmd = {path, "-t", "10000", title, message};
        Runtime.getRuntime().exec(cmd);
    }
    public void addNotification(String title, String message){
        notificationQueue.add(new String[]{title, message});
    }
    public void showAllNotifications() throws Exception{
        for(String[] cur : notificationQueue){
            show(cur[0], cur[1]);
            Thread.sleep(15*1000L);
        }
    }
}
