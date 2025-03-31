///usr/bin/env jbang --fresh "$0" "$@" ; exit $?
//DEPS dev.langchain4j:langchain4j:1.0.0-beta1
//DEPS dev.langchain4j:langchain4j-open-ai:1.0.0-beta1
//DEPS io.quarkiverse.mcp:quarkus-mcp-server-stdio:1.0.0.Beta4
//DEPS com.squareup.okhttp3:okhttp:3.14.9
//DEPS com.fasterxml.jackson.core:jackson-databind:2.16.0

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.stream.StreamSupport;
import java.io.IOException;
import org.jboss.logging.Logger;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;

public class McpWeatherServer {

  private static final Logger LOGGER = Logger.getLogger(McpWeatherServer.class);

  private static final String WEATHER_API_URL = "https://api.open-meteo.com/v1/forecast?latitude=%s&longitude=%s&current_weather=true";
  private static final String GEO_API_URL = "https://geocoding-api.open-meteo.com/v1/search?name=%s&format=json";

  private final okhttp3.OkHttpClient client = new okhttp3.OkHttpClient();
  private final ObjectMapper objectMapper = new ObjectMapper();



  @Tool(description = "A tool to get the current weather a given city and country code")
  public String getWeather(
      @ToolArg(description = "The city") String city,
      @ToolArg(description = "The country code") String countryCode) {
    double[] coords = getCoordinates(city, countryCode);
    if (coords == null) {
      return "Could not find location for " + city + ", " + countryCode;
    }

    String url = String.format(WEATHER_API_URL, coords[0], coords[1]);

    try {
      okhttp3.Request request = new okhttp3.Request.Builder().url(url).build();
      okhttp3.Response response = client.newCall(request).execute();
      if (response.isSuccessful() && response.body() != null) {
        JsonNode weatherData = objectMapper.readTree(response.body().string()).get("current_weather");
        if (weatherData != null) {
          double temperature = weatherData.get("temperature").asDouble();
          double windspeed = weatherData.get("windspeed").asDouble();
          int weatherCode = weatherData.get("weathercode").asInt();
          return String.format("Temperature: %.1f°C, Windspeed: %.1f km/h, Condition: %s",
              temperature, windspeed, getWeatherDescription(weatherCode));
        }
      }
    } catch (IOException e) {
      return "Error retrieving weather data: " + e.getMessage();
    }
    return "Weather data unavailable.";
  }

  private double[] getCoordinates(String city, String countryCode) {
    try {
      String url = String.format(GEO_API_URL, city);
      LOGGER.info(String.format("getCoordinates(%s,%s), url %s", city, countryCode, url));
      okhttp3.Request request = new okhttp3.Request.Builder().url(url).build();
      okhttp3.Response response = client.newCall(request).execute();

      if (response.isSuccessful() && response.body() != null) {
        JsonNode results = objectMapper.readTree(response.body().string()).get("results");

        if (results != null && results.isArray()) {
          return StreamSupport.stream(results.spliterator(), false)
              .filter(node -> countryCode.toLowerCase().equals(
                  node.get("country_code").asText().toLowerCase()))
              .findFirst()
              .map(node -> new double[] {
                  node.get("latitude").asDouble(),
                  node.get("longitude").asDouble()
              }).orElse(null);
        }
      }
    } catch (IOException e) {
      return null;
    }
    return null;
  }

  private String getWeatherDescription(int code) {
    return switch (code) {
      case 0 -> "Clear sky";
      case 1, 2, 3 -> "Partly cloudy";
      case 45, 48 -> "Foggy";
      case 51, 53, 55 -> "Drizzle";
      case 61, 63, 65 -> "Rainy";
      case 71, 73, 75 -> "Snowy";
      case 95, 96, 99 -> "Stormy";
      default -> "Unknown conditions";
    };
  }
}
