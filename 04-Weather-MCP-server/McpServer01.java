///usr/bin/env jbang --fresh "$0" "$@" ; exit $?
//DEPS io.quarkus:quarkus-bom:3.5.0@pom
//DEPS io.quarkus:quarkus-resteasy-reactive
//DEPS io.quarkus:quarkus-resteasy-reactive-jackson
//DEPS io.quarkus:quarkus-jackson
//DEPS com.squareup.okhttp3:okhttp:3.14.9
//DEPS com.fasterxml.jackson.core:jackson-databind:2.16.0




import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.stream.StreamSupport;
import java.io.IOException;
import java.util.Map;
import org.jboss.logging.Logger;


@Path("/mcp")
public class McpServer01 {

    private static final Logger LOGGER = Logger.getLogger(McpServer01.class);

    private static final String WEATHER_API_URL = "https://api.open-meteo.com/v1/forecast?latitude=%s&longitude=%s&current_weather=true";
    private static final String GEO_API_URL = "https://geocoding-api.open-meteo.com/v1/search?name=%s&format=json";

    private final okhttp3.OkHttpClient client = new okhttp3.OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GET
    @Path("/manifest")
    @Produces(MediaType.APPLICATION_JSON)
    public String getManifest() {
        LOGGER.info("getManifest()");
        return """
        {
            "version": "1.0",
            "functions": [
                {
                    "name": "getWeather",
                    "description": "Fetches current weather for a city.",
                    "parameters": {
                        "city": { "type": "string", "required": true },
                        "countryCode": { "type": "string", "required": true }
                    },
                    "returns": {
                        "type": "object",
                        "properties": {
                            "temperature": { "type": "number" },
                            "windspeed": { "type": "number" },
                            "condition": { "type": "string" }
                        }
                    }
                }
            ]
        }""";
    }

    @POST
    @Path("/execute")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response executeFunction(Map<String, Object> request) {
        String function = (String) request.get("function");
        Map<String, String> params = (Map<String, String>) request.get("parameters");

        LOGGER.info(String.format("executeFunction() - function %s, params %s", function, params));

        if ("getWeather".equals(function)) {
            String city = params.get("city");
            String countryCode = params.get("countryCode");

            if (city == null || countryCode == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("error", "Missing parameters")).build();
            }

            String weather = getWeather(city, countryCode);
            return Response.ok(Map.of("result", weather)).build();
        }

        return Response.status(Response.Status.NOT_FOUND).entity(Map.of("error", "Function not found")).build();
    }

    private String getWeather(String city, String countryCode) {
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
            LOGGER.info(String.format("getCoordinates(%s,%s), url %s",city, countryCode,url));
            okhttp3.Request request = new okhttp3.Request.Builder().url(url).build();
            okhttp3.Response response = client.newCall(request).execute();

            if (response.isSuccessful() && response.body() != null) {
                JsonNode results = objectMapper.readTree(response.body().string()).get("results");

                if (results != null && results.isArray()) {
                    return StreamSupport.stream(results.spliterator(), false)
                            .filter(node -> countryCode.toLowerCase().equals(
                                node.get("country_code").asText().toLowerCase()))
                            .findFirst()
                            .map(node -> new double[]{            
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
