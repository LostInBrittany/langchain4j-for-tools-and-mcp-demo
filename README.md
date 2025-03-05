# LangChain4j for Tools and MCP - Demo Repository

This repository contains all the code and examples from my talk:  
**MCP: using Java and Quarkus to bridge LLMs with your applications and data**, presented at:

- **2024-03-05** - [Madrid JUG](https://www.meetup.com/madridjug/events/306387233/) (Madrid, Spain) - [Slides](./slides/)

This repository showcases a series of demos illustrating the integration of **Large Language Models (LLMs) with Java** using [LangChain4j](https://github.com/langchain4j/langchain4j) and the **Model Context Protocol (MCP)**.

Currently, the demos use the [OpenAI API](https://openai.com/api/) as the LLM provider, but they can be adapted to work with any other LLM, including **self-hosted or local models**.

## Demos Included

### 1️. Hello World with LangChain4j

#### Description
A set of Java examples demonstrating how to connect to an LLM using [LangChain4j](https://docs.langchain4j.dev/) and [JBang](https://www.jbang.dev/).

#### Code Location
[`01-Hello-LangChain4j`](./01-Hello-LangChain4j/)

#### Demos
1. `HelloLangChain4j01.java`: Sends a message to the LLM and receives a response.
2. `HelloLangChain4j02.java`: Demonstrates that **LLM interactions are stateless**—the model does not remember previous inputs.
3. `HelloLangChain4j03.java`: Sends the full conversation history to the LLM, allowing it to maintain context.
4. `HelloLangChain4j04.java`: Uses **LangChain4j’s built-in memory** to avoid manually sending the entire message history.

📌 All these demos **use JBang** to keep execution **simple and portable**.

### 2️. Lying Weather Tool

#### Description
A set of Java examples demonstrating **how to expose Java functions as tools** that LLMs can invoke dynamically.  
Like the previous examples, these demos are based on [LangChain4j](https://docs.langchain4j.dev/) and use [JBang](https://www.jbang.dev/) for easy execution.

#### Code Location
[`02-Lying-weather-Tool`](./02-Lying-weather-Tool/)

#### Demos
This section's demos use `LyingWeatherTool.java`, a class where we define a (fake) tool that we expose to the LLM.  
The **Lying Weather Tool** contains a single function:

```java
@Tool
public static String getWeather(String city) {
    return "The weather in " + city + " is sunny and hot.";
}
```

- The function is registered as a tool with the description:  
  **_"A tool to get the current weather in a city."_**  
- However, the function **always returns the same (fake) weather response**, no matter the city.

#### Tool Execution Demos
1. **`CallingTool01.java`**:  
   - Defines a tool specification for the **Lying Weather Tool** and provides it to the LLM.  
   - The LLM understands the tool and requests an execution.

2. **`CallingTool02.java`**:  
   - Manually executes the tool request and sends the response back to the LLM.  
   - The LLM integrates the result into a natural language response.  
   - **Demonstrates why manual execution is cumbersome**—this is where **automated execution & MCP** come in!

## Getting Started

### Prerequisites
- **Java 17+**: Ensure you have Java 17 or higher installed.
- **JBang**: A tool to run Java applications with minimal setup. Install it from [https://www.jbang.dev/](https://www.jbang.dev/).

### Running the Demos

1. **Clone the Repository**:

   ```sh
   git clone https://github.com/LostInBrittany/langchain4j-for-tools-and-mcp-demo.git
   cd langchain4j-for-tools-and-mcp-demo
   ```

2. **Execute any of the demos using JBang**:

   ```sh
   jbang ./01-Hello-LangChain4j/HelloLangChain4j01.java
   ```

   *Replace `./01-Hello-LangChain4j/HelloLangChain4j01.java` with the appropriate script for other demos.*

## Resources & Further Reading
- 📘 **LangChain4j Documentation**: [https://github.com/langchain4j/langchain4j](https://github.com/langchain4j/langchain4j)
- 🛠 **JBang Documentation**: [https://www.jbang.dev/documentation/](https://www.jbang.dev/documentation/)

## License
📝 This repository is licensed under the **MIT License**. See the [LICENSE](./LICENSE) file for details.
