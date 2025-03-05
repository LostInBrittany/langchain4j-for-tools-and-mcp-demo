///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS dev.langchain4j:langchain4j:1.0.0-beta1
//DEPS dev.langchain4j:langchain4j-open-ai:1.0.0-beta1
//SOURCES LyingWeatherTool.java

import java.util.List;
import java.io.IOException;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.agent.tool.ToolSpecifications;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiTokenizer;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.service.tool.ToolExecutionResult;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.TokenWindowChatMemory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_O_MINI;

/** ------------------------------------------------------- 
 ** We can manually execute the tool execution request
 ** and send it back to the LLM... 
 ** but it needs a lot of manual work
 ** ------------------------------------------------------- */
public class CallingTool02 {

  public static void main(String... args) {

    ChatMemory chatMemory = TokenWindowChatMemory.withMaxTokens(300, new OpenAiTokenizer(GPT_4_O_MINI));
    Response<AiMessage> response;

    String apiKey = System.getenv("OPENAI_API_KEY");

    OpenAiChatModel model = OpenAiChatModel.builder()
        .apiKey(apiKey)
        .modelName(GPT_4_O_MINI)
        .build();

    System.out.println("------------------------------------");

    List<ToolSpecification> toolSpecifications = ToolSpecifications.toolSpecificationsFrom(LyingWeatherTool.class);

    UserMessage message01 = UserMessage.from(
        "What will the weather be like in Madrid tomorrow?");
    chatMemory.add(message01);
    response = model.generate(chatMemory.messages(), toolSpecifications);
    AiMessage aiMessage01 = response.content();
    System.out.println(aiMessage01);


    System.out.println("------------------------------------");

    if (aiMessage01.hasToolExecutionRequests() && aiMessage01.toolExecutionRequests().size() == 1) {

      ToolExecutionRequest weatherRequest = aiMessage01.toolExecutionRequests().get(0);

      if (weatherRequest.name().equals("getWeather")) {

        String city = "";
        String argumentsJson = weatherRequest.arguments(); // Raw JSON string
        ObjectMapper objectMapper = new ObjectMapper();
        try {
          JsonNode jsonNode = objectMapper.readTree(argumentsJson); // Parse JSON
          city = jsonNode.get("city").asText(); // Extract argument
        } catch (IOException e) {
          System.out.println("Error parsing tool arguments");
          System.exit(0);
        }
        ToolExecutionResultMessage weatherResponse = 
          ToolExecutionResultMessage.from(weatherRequest,LyingWeatherTool.getWeather(city));
        chatMemory.add(aiMessage01);
        chatMemory.add(weatherResponse);
        response = model.generate(chatMemory.messages(), toolSpecifications);
        AiMessage aiMessage02 = response.content();
        System.out.println(aiMessage02);
        System.out.println("------------------------------------");    
      }

    }
  }

}
