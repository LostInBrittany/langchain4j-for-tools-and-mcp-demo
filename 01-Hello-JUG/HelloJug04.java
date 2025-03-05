///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS dev.langchain4j:langchain4j:1.0.0-beta1
//DEPS dev.langchain4j:langchain4j-open-ai:1.0.0-beta1

import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_O_MINI;


import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiTokenizer;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.TokenWindowChatMemory;

/** ------------------------------------------------------- 
 ** Another way: memory
 ** History keeps all messages between the user and AI intact
 ** Memory keeps some information, which is presented to the 
 ** LLM to make it behave as if it "remembers" the conversation
 ** ------------------------------------------------------- */
public class HelloJug04 {
  public static void main(String... args) {
    String apiKey = System.getenv("OPENAI_API_KEY");
    OpenAiChatModel model = OpenAiChatModel.builder()
        .apiKey(apiKey)
        .modelName(GPT_4_O_MINI)
        .build();


    ChatMemory chatMemory = TokenWindowChatMemory.withMaxTokens(300, new OpenAiTokenizer(GPT_4_O_MINI));
    Response<AiMessage> response;

    System.out.println("------------------------------------");

    UserMessage message01 = UserMessage.from(
        "I am doing a demo at Madrid JUG. Can you introduce yourself and say hello?");
    chatMemory.add(message01);
    response = model.generate(chatMemory.messages());
    AiMessage aiMessage01 = response.content();
    System.out.println(aiMessage01.text());
    chatMemory.add(aiMessage01);

    System.out.println("------------------------------------");

    UserMessage message02 = UserMessage.from(
        "Where am I today? What am I doing?");
    chatMemory.add(message02);
    response = model.generate(chatMemory.messages());
    AiMessage aiMessage02 = response.content();
    System.out.println(aiMessage02.text());

    System.out.println("------------------------------------");
  }
}
