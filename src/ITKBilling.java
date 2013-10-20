import javax.net.ssl.*;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class ITKBilling {
    String user;
    String passwd;

    ITKBilling() throws Exception {
        // trustmanager that trust all certificates
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };
        HostnameVerifier verifier = new HostnameVerifier() {
            public boolean verify(String string, SSLSession sSLSession) {
                return true;
            }
        };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(verifier);

        Scanner input = new Scanner(new File("ITKpasswd.txt"));
        user = input.nextLine();
        passwd = input.nextLine();

    }

    public InputStream getInputStream() throws Exception {
        String request = "https://stat.itk.sumy.ua/index.cgi?user=" + user + "&passwd=" + passwd;
        URL url = new URL(request);
        URLConnection connection = url.openConnection();
        return connection.getInputStream();
    }
}
