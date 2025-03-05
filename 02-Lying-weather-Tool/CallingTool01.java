///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS dev.langchain4j:langchain4j:1.0.0-beta1
//DEPS dev.langchain4j:langchain4j-open-ai:1.0.0-beta1
//SOURCES LyingWeatherTool.java

import java.util.List;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.agent.tool.ToolSpecifications;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiTokenizer;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.TokenWindowChatMemory;

import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_O_MINI;


/** ------------------------------------------------------- 
 ** We can add a tool specification
 ** The LLM understand it and sends back a 
 ** tool execution request
 ** ------------------------------------------------------- */
public class CallingTool01 {

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
    response = model.generate(chatMemory.messages(),toolSpecifications);
    AiMessage aiMessage01 = response.content();
    System.out.println(aiMessage01);

    System.out.println("------------------------------------");

  }
}
