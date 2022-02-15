package dev.glitchedcoder.hangman.util;

import com.google.gson.Gson;
import dev.glitchedcoder.hangman.json.Config;
import dev.glitchedcoder.hangman.json.Word;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

public final class ApiRequest {

    private static final int OK_STATUS = 200;
    private static final Config CONFIG = Config.getConfig();

    private ApiRequest() {
    }

    /**
     * Checks if the currently stored
     * {@link Config#getApiKey() API key} is valid.
     * <br />
     * This method communicates with the {@link Constants#API_URL API}
     * over the web, so an internet connection is required in order
     * for this method to successfully execute/return a valid flag.
     * <br />
     * In very rare circumstances, it may be possible for this
     * method to return {@code false} even when the stored
     * {@link Config#getApiKey() API key} is valid. This is due to
     * a 1-second ratelimit by the API. This is also why it is best
     * to use your own API key, as to not ratelimit others.
     *
     * @return True if the stored API key is valid, false otherwise.
     */
    public static boolean checkApiKey() {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            // build URI
            URIBuilder builder = new URIBuilder(Constants.API_URL);
            builder.addParameter("key", CONFIG.getApiKey());
            // create & execute request
            HttpGet get = new HttpGet(builder.build());
            CloseableHttpResponse response = client.execute(get);
            // get response
            HttpEntity entity = response.getEntity();
            StatusLine status = response.getStatusLine();
            // clean up & return true/false based on status code
            EntityUtils.consume(entity);
            response.close();
            return status.getStatusCode() == OK_STATUS;
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Word requestWord(int length) {
        Gson gson = Constants.GSON;
        length = Validator.constrain(length, Constants.MIN_WORD_LENGTH, Constants.MAX_WORD_LENGTH);
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            // build URI
            URIBuilder builder = new URIBuilder(Constants.API_URL);
            builder.addParameter("key", CONFIG.getApiKey());
            builder.addParameter("length", String.valueOf(length));
            // create & execute request
            HttpGet get = new HttpGet(builder.build());
            CloseableHttpResponse response = client.execute(get);
            // get the content from the reponse & parse using GSON
            InputStream in = response.getEntity().getContent();
            InputStreamReader reader = new InputStreamReader(in);
            Word word = gson.fromJson(reader, Word.class);
            // clean up & return the parsed response
            EntityUtils.consume(response.getEntity());
            response.close();
            return word;
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}