import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Launcher {

    private static Logger log = Logger.getLogger(Launcher.class.getName());

    public static void main(String[] args) {
        try {

            deleteAllBranches();

            for (int i = 1; i <= 500; i++) {
                addBranch(i);
            }

            fetchAllBranches();

            for (int i = 1; i < 5000; i++) {
                System.out.println("Woohoo");
                incrementRandomBranchCounter();
            }

            fetchRankings();
        } catch (MalformedURLException e) {
            log.log(Level.SEVERE, "Malformed URL", e);
            e.printStackTrace();
        } catch (IOException e) {
            log.log(Level.SEVERE, "File IO Exception", e);
            e.printStackTrace();
        }
    }

    private static void incrementRandomBranchCounter() throws IOException {
        Random r = new Random();
        int branchId = r.nextInt(500);

        log.info("incrementing branch couter for branchId: " + branchId);

        URL url = new URL("http://localhost:8080/api/updateBranchCounter/" + branchId);
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setDoOutput(true);
        httpCon.setRequestMethod("PUT");
        OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());
        out.write("Resource content");
        out.close();
        httpCon.getInputStream();
    }

    private static void deleteAllBranches() throws IOException {
        log.warning("deleting all branches");

        URL url = new URL("http://localhost:8080/api/deleteAll");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");
        conn.setRequestProperty("Accept", "application/json");

        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

        String output;
        while ((output = br.readLine()) != null) {
            System.out.println(output);
        }

        conn.disconnect();

        log.warning("deleted all branches");
    }

    private static void fetchRankings() throws IOException {
        log.info("fetching rankings");

        URL url = new URL("http://localhost:8080/api/branchRankings");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

        String output;
        while ((output = br.readLine()) != null) {
            System.out.println(output);
        }

        conn.disconnect();
    }

    private static void fetchAllBranches() throws IOException {
        log.info("fetching all branches");
        URL url = new URL("http://localhost:8080/api/fetchAll");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

        String output;
        while ((output = br.readLine()) != null) {
            System.out.println(output);
        }

        conn.disconnect();
    }

    private static void addBranch(Integer branchId) throws IOException {
        log.info("adding new branch with branchId: " + branchId);

        URL url = new URL("http://localhost:8080/api/branch/addBranch");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");

        String input = "{ \"branchName\" : \"Branch Number " + branchId + "\" }";

        OutputStream os = conn.getOutputStream();
        os.write(input.getBytes());
        os.flush();

        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

        String output;
        System.out.println("Output from Server .... \n");
        while ((output = br.readLine()) != null) {
            System.out.println(output);
        }

        conn.disconnect();

    }
}
