package gluon.projects.utilities;

import gluon.projects.exceptions.TechnicalException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RestApiUtility {

    private RestApiUtility() {
        throw new IllegalStateException("Utility class");
    }

    public static String sendRestApiRequest(String apiUrl) {
        HttpRequest httpRequest = HttpRequest
                .newBuilder()
                .uri(URI.create(apiUrl))
                .build();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> httpResponse = null;

        try {
            httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            throw new TechnicalException(e);
        }
        return httpResponse.body();
    }

}
