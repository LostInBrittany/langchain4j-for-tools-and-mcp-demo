# Weather MCP Server

This folder demonstrates how to implement a Model Context Protocol (MCP) server using Quarkus MCP and the real weather tool functionality from the previous examples.

## What is MCP?

The Model Context Protocol (MCP) is a standardized way for tools and LLMs to communicate. It allows:

1. Tools to expose their functionality to any MCP-compatible LLM
2. LLMs to discover and use tools without being tied to specific implementations
3. A consistent interface for tool specifications and invocations

## Components

### McpWeatherServer.java

Implements an MCP server that:

1. Uses Quarkus MCP Server STDIO (`io.quarkiverse.mcp:quarkus-mcp-server-stdio`) to handle the MCP protocol
2. Exposes a weather tool that can be discovered and used by any MCP-compatible LLM
3. Fetches real weather data from Open-Meteo APIs just like the previous example

Key differences from the previous example:

```java
@Tool(name = "Get Weather", description = "A tool to get the current weather a given city and country code")
public String getWeather(
    @ToolArg(description = "The city") String city,
    @ToolArg(description = "The country code") String countryCode) {
    // Implementation
}
```

Note the use of MCP-specific annotations:
- `@Tool` - Defines a tool with a name and description
- `@ToolArg` - Provides descriptions for each parameter

### jbang-wrapper.sh

A shell script that addresses a Mac-specific environment issue when running JBang applications from Claude Desktop.

On macOS, Claude Desktop doesn't use the same PATH environment variables as your terminal, which means it can't directly find and execute JBang. The wrapper script solves this by:

1. Sourcing your shell profile files (`.zshenv` and `.zprofile`) to load your complete environment
2. Explicitly using the full path to your JBang installation
3. Logging debug information to help troubleshoot any issues

This ensures that when you run commands through Claude Desktop on Mac, they have access to the same tools and environment as your regular terminal sessions.

**Note:** This wrapper is specifically needed for [Claude Desktop](https://claude.ai/desktop) on Mac. Other AI assistants and tools (like [Block's Goose](https://github.com/block/goose)) may not require this workaround as they might handle environment variables differently.

## Running the MCP Server

To run the MCP server:

```bash
./jbang-wrapper.sh McpWeatherServer.java
```

This will start the MCP server in STDIO mode, allowing it to communicate with MCP-compatible LLMs through standard input/output.

## Connecting to the MCP Server

MCP servers can be connected to various LLM providers that support the protocol. Some options include:

1. OpenAI's API with MCP support
2. Local LLMs that implement MCP
3. LangChain4j's MCP client capabilities

## Key Concepts

- **Model Context Protocol (MCP)**: A standardized protocol for LLM-tool communication
- **Quarkus MCP**: A Java implementation of the MCP server specification
- **Tool Discovery**: How LLMs can discover available tools through the MCP protocol
- **Cross-Platform Compatibility**: Using MCP to make tools available to different LLM providers
- **STDIO Communication**: Using standard input/output for tool-LLM communication

## Requirements

- JBang installed ([installation guide](https://www.jbang.dev/documentation/guide/latest/installation.html))
- Java 17+ installed
- Internet connection to access the Open-Meteo APIs
