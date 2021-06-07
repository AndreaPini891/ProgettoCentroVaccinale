package centrivaccinali.web;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * The type Server json handler test.
 *
 *       @author SEDE COMO
 *       @author Samuele Barella - mat.740688
 *       @author Lorenzo Pengue -
 *       @author Andrea Pini - mat.740675
 *
 */
class ServerJSONHandlerTest {

    /**
     * Make request.
     */
    @DisplayName("Test makeRequest")
    @Test
     void makeRequest() throws IOException, InterruptedException, ServerStatusException {

        ServerJSONHandler s = new ServerJSONHandler();

        login(s);

        CompletableFuture<JSONArray> json = s
                .setMethod(WebMethods.GET)
                .setEndpoint("EventiAvversi")
                .makeRequest();

        System.out.println(json.join());

    }

    void login(ServerJSONHandler s) {

        try {
            JSONObject jsonobj = new JSONObject("{\'userName\': \'test\', \'password\': \'test\'}");

            CompletableFuture<JSONArray> json = s
                    .setMethod(WebMethods.POST)
                    .setEndpoint("rpc/login_cittadino")
                    .setData(jsonobj)
                    .makeRequest();

            //System.out.println("started: " + json);

            String token = (String) json.join().getJSONObject(0).get("token");

            s.setJWT(token);

            System.out.println("completed: " + json);

        } catch (IOException | InterruptedException | ServerStatusException e) {
            e.printStackTrace();
        }

    }
}