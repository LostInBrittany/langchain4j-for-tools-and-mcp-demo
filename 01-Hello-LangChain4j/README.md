# Hello World with LangChain4j

This folder contains basic examples demonstrating how to connect to Large Language Models (LLMs) using [LangChain4j](https://docs.langchain4j.dev/) and [JBang](https://www.jbang.dev/).

## Examples

### 1. HelloLangChain4j01.java

A simple example showing how to send a single message to an LLM and receive a response.

```bash
jbang HelloLangChain4j01.java
```

### 2. HelloLangChain4j02.java

Demonstrates that LLM interactions are stateless by default - the model does not remember previous inputs or context between separate calls.

```bash
jbang HelloLangChain4j02.java
```

### 3. HelloLangChain4j03.java

Shows one approach to maintain context: manually sending the full conversation history to the LLM with each request, allowing it to reference previous exchanges.

```bash
jbang HelloLangChain4j03.java
```

### 4. HelloLangChain4j04.java

Demonstrates using LangChain4j's built-in memory capabilities to automatically maintain conversation context without having to manually track and send the entire message history.

```bash
jbang HelloLangChain4j04.java
```

## Requirements

- JBang installed ([installation guide](https://www.jbang.dev/documentation/guide/latest/installation.html))
- OpenAI API key set as an environment variable:
  ```bash
  export OPENAI_API_KEY=your-api-key
  ```

## Key Concepts

- **Stateless Interactions**: By default, LLM calls are independent and stateless
- **Conversation Context**: Methods to maintain context across multiple interactions
- **Chat Memory**: Using LangChain4j's memory abstractions to simplify context management
