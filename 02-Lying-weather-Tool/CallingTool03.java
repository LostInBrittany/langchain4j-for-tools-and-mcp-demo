///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS dev.langchain4j:langchain4j:1.0.0-beta1
//DEPS dev.langchain4j:langchain4j-open-ai:1.0.0-beta1
//SOURCES LyingWeatherTool.java

import static java.lang.System.*;

import java.util.List;
import java.io.IOException;


import dev.langchain4j.memory.chat.TokenWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.agent.tool.ToolSpecifications;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.openai.OpenAiTokenizer;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.service.tool.ToolExecutionResult;
import dev.langchain4j.memory.ChatMemory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_O_MINI;

/** ------------------------------------------------------- 
 ** There is a higher level API in LangCahin4j : AiService
 ** It can use tools transparently
 ** ------------------------------------------------------- */
public class CallingTool03 {

  public static void main(String... args) {

    interface Assistant {
        String chat(String userMessage);
    }

    String apiKey = System.getenv("OPENAI_API_KEY");
    ChatMemory chatMemory = TokenWindowChatMemory.withMaxTokens(300, new OpenAiTokenizer(GPT_4_O_MINI));

    OpenAiChatModel model = OpenAiChatModel.builder()
        .apiKey(apiKey)
        .modelName(GPT_4_O_MINI)
        // https://docs.langchain4j.dev/integrations/language-models/open-ai#structured-outputs-for-tools
        .strictTools(true) 
        .build();

    Assistant assistant = AiServices.builder(Assistant.class)
        .chatLanguageModel(model)
        .tools(new LyingWeatherTool())
        .chatMemory(chatMemory)
        .build();    

    System.out.println("------------------------------------");
    String question = "What will the weather be like in Madrid tomorrow?";
    String response = assistant.chat(question);
    System.out.println(response);
    System.out.println("------------------------------------");
  }
}
