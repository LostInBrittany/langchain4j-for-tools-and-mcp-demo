# Real Weather Tool

This folder demonstrates how to create a functional weather information tool that integrates with external APIs and can be used by LLMs through [LangChain4j](https://docs.langchain4j.dev/).

## Components

### RealWeatherTool.java

Implements a genuine weather information tool that:

1. Uses the [Open-Meteo Geocoding API](https://open-meteo.com/en/docs/geocoding-api) to convert city names and country codes into geographic coordinates
2. Fetches current weather data from the [Open-Meteo Forecast API](https://open-meteo.com/en/docs) using those coordinates
3. Parses and formats the weather information into a human-readable response

The tool is exposed to LLMs using the `@Tool` annotation:

```java
@Tool("A tool to get the current weather a given city and country code")
public static String getWeather(String city, String countryCode) {
    // Implementation that fetches real weather data
}
```

### CallingTool01.java

Demonstrates how to use LangChain4j's AI Services to seamlessly integrate the Real Weather Tool with an LLM. The AI Service handles tool invocation transparently, allowing the LLM to:

1. Understand when to use the weather tool based on user queries
2. Call the tool with appropriate parameters
3. Incorporate the real weather data into its responses

```bash
jbang CallingTool01.java
```

## Requirements

- JBang installed ([installation guide](https://www.jbang.dev/documentation/guide/latest/installation.html))
- OpenAI API key set as an environment variable:
  ```bash
  export OPENAI_API_KEY=your-api-key
  ```
- Internet connection to access the Open-Meteo APIs

## Key Concepts

- **Real API Integration**: Connecting LLMs to external data sources and services
- **HTTP Clients**: Using OkHttp to make API requests
- **JSON Parsing**: Processing API responses with Jackson
- **Error Handling**: Gracefully managing API failures and edge cases
- **Tool Parameters**: Working with multiple parameters in tool definitions
