package dev.glitchedcoder.hangman.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import dev.glitchedcoder.hangman.Hangman;
import dev.glitchedcoder.hangman.json.Config;
import dev.glitchedcoder.hangman.json.Words;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
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
import java.util.Random;

public final class ApiRequest {

    private static final int OK_STATUS = 200;
    private static final Config CONFIG = Config.getConfig();
    private static final Random RANDOM = Hangman.getRandom();

    private ApiRequest() {
    }

    /**
     * Checks for an {@link #OK_STATUS} from the API.
     * <br />
     * The timeout is configured for {@code 1000ms} in case the site is down.
     *
     * @return True if the API is available, false otherwise.
     */
    public static boolean checkApi() {
        // set timeout to 1000ms or 1 second
        RequestConfig config = RequestConfig.custom()
                .setConnectionRequestTimeout(1000)
                .setConnectTimeout(1000)
                .setSocketTimeout(1000)
                .build();
        // build with the custom request config
        try (CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(config).build()) {
            HttpGet get = new HttpGet(Constants.API_URL);
            CloseableHttpResponse response = client.execute(get);
            StatusLine status = response.getStatusLine();
            response.close();
            // check for OK_STATUS
            return status.getStatusCode() == OK_STATUS;
        } catch (IOException e) {
            // return false if any I/O exception is thrown
            // also accounts for timeout
            return false;
        }
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

    /**
     * Requests a {@link Words} from the API.
     * <br />
     * This method communicates with the {@link Constants#API_URL API}
     * over the web, so an internet connection is required in order
     * for this method to successfully execute/return a valid {@link Words}.
     * <br />
     * In very rare circumstances, it may be possible for this
     * method to return {@code null} even when the stored
     * {@link Config#getApiKey() API key} is valid. This is due to
     * a 1-second ratelimit by the API. This is also why it is best
     * to use your own API key, as to not ratelimit others.
     *
     * @param length The length of the word to request.
     * @return A {@link Words} from the API, or rarely {@code null}.
     */
    public static String requestWord(int length) {
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
            JsonElement element = JsonParser.parseReader(reader);
            String word = element.getAsJsonObject().get("word").getAsString();
            // clean up & return the parsed response
            EntityUtils.consume(response.getEntity());
            response.close();
            return word;
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets a random word length between
     * {@link Constants#MIN_WORD_LENGTH} and
     * {@link Constants#MAX_WORD_LENGTH}.
     *
     * @return A random word length.
     */
    public static int randomWordLength() {
        return RANDOM.nextInt(Constants.MAX_WORD_LENGTH - Constants.MIN_WORD_LENGTH) + Constants.MIN_WORD_LENGTH;
    }
}