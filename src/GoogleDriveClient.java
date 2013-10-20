import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class GoogleDriveClient {
    private final Drive service;

    public GoogleDriveClient() throws Exception {

        Scanner keyScanner = new Scanner(new java.io.File("keys.txt"));
        String client_id = keyScanner.nextLine();
        String client_secret = keyScanner.nextLine();
        String redirect_URI = keyScanner.nextLine();

        HttpTransport httpTransport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, client_id, client_secret, Arrays.asList(DriveScopes.DRIVE))
                .setAccessType("online")
                .setApprovalPrompt("auto").build();

        String url = flow.newAuthorizationUrl().setRedirectUri(redirect_URI).build();
        System.out.println("Please open the following URL in your browser then type the authorization code:");
        System.out.println("  " + url);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String code = br.readLine();

        GoogleTokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirect_URI).execute();
        GoogleCredential credential = new GoogleCredential().setFromTokenResponse(response);

        //Create a new authorized API client
        service = new Drive.Builder(httpTransport, jsonFactory, credential).build();

    }

    private List<File> retrieveAllFiles(Drive service) throws IOException {
        List<File> result = new ArrayList<File>();
        Drive.Files.List request = service.files().list();
        do {
            try {
                FileList files = request.execute();

                result.addAll(files.getItems());
                request.setPageToken(files.getNextPageToken());
            } catch (IOException e) {
                System.out.println("An error occurred: " + e);
                request.setPageToken(null);
            }
        } while (request.getPageToken() != null && request.getPageToken().length() > 0);
        return result;
    }

    private InputStream downloadFile(Drive service, File file) {
        try {
            HttpResponse resp =
                    service.getRequestFactory().buildGetRequest(new GenericUrl(file.getExportLinks().get("text/plain")))
                            .execute();
            return resp.getContent();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    // return InputStream of file with dates
    public InputStream getInputStream() throws Exception {
        List<File> cur = retrieveAllFiles(service);
        File neededFile = null;
        for (File i : cur) {
            if (i.getTitle().equals("linses")) {
                neededFile = i;
            }
        }
        return downloadFile(service, neededFile);
    }
}
