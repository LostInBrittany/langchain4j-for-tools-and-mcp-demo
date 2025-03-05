//DEPS dev.langchain4j:langchain4j:1.0.0-beta1

import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.ToolExecutionRequest;

public class LyingWeatherTool {
  @Tool("A tool to get the current weather in a city")
  public static String getWeather(String city) {
    return "The weather in " + city + " is sunny and hot.";
  }
}
