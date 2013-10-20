import java.util.GregorianCalendar;
import java.util.Scanner;

public class Main {
    private static NotifyDisplay notifier;
    private static GoogleDriveClient driveClient;
    private static ITKBilling billing;
    private static int lensesDifference;
    private static int ITKDifference;

    public static void main(String[] args) throws Exception {

        billing = new ITKBilling();
        notifier = new NotifyDisplay();
        driveClient = new GoogleDriveClient();
        checkITK();
        checkLenses();
        notifier.addNotification("Lenses", "Remain "+lensesDifference+" days");
        notifier.addNotification("ITK Internet", "Remain "+ITKDifference+" UAH");
        while(true){
            notifier.showAllNotifications();
            Thread.sleep(1000L*60*60);
        }

    }

    private static void checkLenses() throws Exception {
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

    private static void checkITK() throws Exception{
        Scanner input = new Scanner(billing.getInputStream());
        String deposit=null;
        while(input.hasNextLine()){
            String cur = input.nextLine();
            if(cur.contains("Депозит:")){
                deposit = cur;
                break;
            }
        }
        deposit = deposit.substring(50, deposit.length()-10);
        double currentDeposit = Double.parseDouble(deposit);
        ITKDifference = (int)Math.floor(currentDeposit);
    }

}
