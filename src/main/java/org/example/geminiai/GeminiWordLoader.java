
package org.example.geminiai;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.net.URIBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class GeminiWordLoader {
    private static String GEMINI_PROMPT_URL = "https://generativelanguage.googleapis.com/v1/models/gemini-pro:generateContent";
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String APPLICATION_JSON = "application/json";
    private static final String TOKEN = "AIzaSyDdR6X-Y-7-W-7CxDzp3H5Vl7FOOVJq7TQ";
    private HttpClient httpClient = HttpClient.newBuilder().build();

    // { "contents":[
    //      { "parts":[{"text": "Write a story about a magic backpack"}]}
    //    ]
    //  }
    public List<String> load(int listSize, int wordSize) {

        String jsonPost = "";
        URI uri = null;
        String prompt = """
                {
                  "contents":[
                     {
                       "role": "user",
                       "parts":[{"text": "Retorne uma lista de %s palavras com %s letras uma por linha, crie de forma randomica"}]
                     }
                  ]
                }
                """.formatted(listSize, wordSize);
        jsonPost = prompt;
        try {
            uri = new URIBuilder(GEMINI_PROMPT_URL)
                    .addParameter("key", TOKEN)
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        HttpRequest request = HttpRequest.newBuilder(uri)
                .header(HEADER_CONTENT_TYPE, APPLICATION_JSON)
                .POST(HttpRequest.BodyPublishers.ofString(jsonPost))
                .build();

        HttpResponse<String> response = null;
        try {
            response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Resposta:");
        System.out.println(response.body());
        return new ArrayList<>();
    }
}
