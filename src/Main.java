import java.io.File;
import java.util.GregorianCalendar;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        Scanner keyScanner = new Scanner(new File("keys.txt"));
        String client_id = keyScanner.nextLine();
        String client_secret = keyScanner.nextLine();
        String redirect_URI = keyScanner.nextLine();

        GoogleDriveClient driveClient = new GoogleDriveClient(client_id, client_secret, redirect_URI);

        NotifyDisplay notification = new NotifyDisplay();
        Scanner input = new Scanner(driveClient.getInputStream());
        String last = null;
        while (input.hasNextLine()) {
            last = input.nextLine();
        }
        String[] lastDateString = last.split(";");
        String[] dates = lastDateString[0].trim().split("-");
        // months are 0-based
        GregorianCalendar lastDate = new GregorianCalendar(Integer.parseInt(dates[2]), Integer.parseInt(dates[1]) - 1, Integer.parseInt(dates[0]));
        GregorianCalendar currentDate = new GregorianCalendar();

        long difference = currentDate.getTimeInMillis() - lastDate.getTimeInMillis();
        difference /= 1000 * 60 * 60 * 24;
        difference = Long.parseLong(lastDateString[1]) - difference;

        notification.show("Linses", "Remain " + difference + " days");

    }


}
