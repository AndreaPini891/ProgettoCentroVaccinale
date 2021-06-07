package centrivaccinali.web;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * This class creates an object tasked with communicating with the server.
 *
 *       @author SEDE COMO
 *       @author Samuele Barella - mat.740688
 *       @author Lorenzo Pengue - mat.740727
 *       @author Andrea Pini - mat.740675
 *
 */
public class ServerJSONHandler {

    /**
     * Request parameters
     */
    private final String baseUrl = "http://dev.meliver.it:3000/";
    private String endpoint;
    private JSONObject data;
    private WebMethods method;
    private URI url;
    private static String jwt = "";

    /**
     * Response parameters
     */
    private CompletableFuture<JSONObject> response;
    private Integer responseCode;

    /**
     * Instantiates a new Server json handler.
     */
    public ServerJSONHandler() {
        data = new JSONObject("{}");
    }

    /**
     * Get full uri for the current request.
     *
     * @return the uri
     */
    public URI getURI(){
        return URI.create(baseUrl + endpoint);
    }

    /**
     * Get the response code for the current request.
     *
     * @return the response code
     */
    public int getResponseCode(){
        return responseCode;
    }

    /**
     * Sets method.
     *
     * @param method the method
     * @return the server json handler
     */
    public ServerJSONHandler setMethod(WebMethods method) {this.method = method; return this;}

    /**
     * Set endpoint server json handler.
     *
     * @param endpoint the endpoint
     * @return the server json handler
     */
    public ServerJSONHandler setEndpoint(String endpoint){this.endpoint = endpoint;  return this;}

    /**
     * Set jwt server json handler.
     *
     * @param jwt the json web token
     * @return the server json handler
     */
    public ServerJSONHandler setJWT(String jwt){this.jwt = jwt;  return this;}

    /**
     * Set data server json handler.
     *
     * @param json the json
     * @return the server json handler
     */
    public ServerJSONHandler setData(JSONObject json){this.data = json; return this;}

    /**
     * Make web request and returns a json array as completable future.
     *
     * @return completable future
     */
    public CompletableFuture<JSONArray> makeRequest() throws IOException, InterruptedException {

        return CompletableFuture.supplyAsync(new Supplier<JSONArray>() {
            @Override
            public JSONArray get() {
                url = URI.create(baseUrl + endpoint);

                var request = HttpRequest.newBuilder()
                        .uri(url)
                        .header("Content-Type", "application/json");

                if(jwt != null)
                    request.header("Authorization", "Bearer "+jwt);

                HttpRequest.BodyPublisher p = HttpRequest.BodyPublishers.ofString(data.toString());
                request.method(method.name(), p);

                var client = HttpClient.newHttpClient();
                try {
                    HttpResponse<String> t = client.send(request.build(), HttpResponse.BodyHandlers.ofString());

                    responseCode = t.statusCode();

                    if(t.statusCode() != 200)
                        throw new ServerStatusException(t.statusCode() + " on " + endpoint);
                    return new JSONArray(t.body());

                } catch (IOException | InterruptedException | ServerStatusException | JSONException e) {
                    e.printStackTrace();
                    return new JSONArray("[{}]");
                }
            }
        });
    }

}
