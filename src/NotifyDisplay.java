public class NotifyDisplay {
    private static final String path = "/usr/bin/notify-send";

    public void show(String title, String message) throws Exception {
        String[] cmd = {path, "-t", "10000", title, message};
        Runtime.getRuntime().exec(cmd);
    }
}
