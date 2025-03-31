# Lying Weather Tool

This folder demonstrates how to expose Java functions as tools that LLMs can invoke dynamically using [LangChain4j](https://docs.langchain4j.dev/).

## Components

### LyingWeatherTool.java

A simple class that defines a fake weather tool that always returns the same response regardless of the input city:

```java
@Tool("A tool to get the current weather in a city")
public static String getWeather(String city) {
  return "The weather in " + city + " is sunny and hot.";
}
```

### CallingTool01.java

Demonstrates how to define a tool specification for the Lying Weather Tool and provide it to the LLM. The example shows how the LLM understands the tool's purpose and requests its execution.

```bash
jbang CallingTool01.java
```

### CallingTool02.java

Shows the manual process of executing a tool request and sending the response back to the LLM. This example highlights why manual execution can be cumbersome and why automated solutions are preferable.

```bash
jbang CallingTool02.java
```

### CallingTool03.java

Introduces LangChain4j's AI Services concept, which simplifies tool integration by handling tool calls transparently. This approach eliminates the need for manual tool execution handling.

```bash
jbang CallingTool03.java
```

## Requirements

- JBang installed ([installation guide](https://www.jbang.dev/documentation/guide/latest/installation.html))
- OpenAI API key set as an environment variable:
  ```bash
  export OPENAI_API_KEY=your-api-key
  ```

## Key Concepts

- **Tool Annotations**: Using `@Tool` to expose Java methods to LLMs
- **Tool Specifications**: Defining metadata about tools for LLMs to understand their purpose
- **Tool Execution**: Manual vs. automated approaches to handling tool calls
- **AI Services**: Simplifying LLM and tool integration with higher-level abstractions
