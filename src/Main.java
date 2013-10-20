import java.util.GregorianCalendar;
import java.util.Scanner;

public class Main {
    private static NotifyDisplay notifier;
    private static GoogleDriveClient driveClient;
    private static int lensesDifference;
    private static int internetDifference;

    public static void main(String[] args) throws Exception {
        notifier = new NotifyDisplay();
        driveClient = new GoogleDriveClient();


    }

    private static void chekLenses() throws Exception {
        Scanner input = new Scanner(driveClient.getInputStream());
        String last = null;
        while (input.hasNextLine()) {
            last = input.nextLine();
        }
        String[] lastDateString = last.split(";");
        String[] dates = lastDateString[0].trim().split("-");
        // months are 0-based
        GregorianCalendar lastDate = new GregorianCalendar(Integer.parseInt(dates[2]),
                Integer.parseInt(dates[1]) - 1, Integer.parseInt(dates[0]));

        GregorianCalendar currentDate = new GregorianCalendar();

        long difference = currentDate.getTimeInMillis() - lastDate.getTimeInMillis();
        difference /= 1000 * 60 * 60 * 24;
        lensesDifference = (int) (Long.parseLong(lastDateString[1]) - difference);
    }


}
