///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS dev.langchain4j:langchain4j-open-ai:1.0.0-beta1

import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.output.Response;

import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_O_MINI;

import java.util.List;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.message.ChatMessage;

/** ------------------------------------------------------- 
 ** One way to make it remember: sending the whole history    
 ** ------------------------------------------------------- */
public class HelloLangChain4j03 {
  public static void main(String... args) {
    String apiKey = System.getenv("OPENAI_API_KEY");
    OpenAiChatModel model = OpenAiChatModel.builder()
        .apiKey(apiKey)
        .modelName(GPT_4_O_MINI)
        .build();

    List<ChatMessage> userMessages;
    Response<AiMessage> response;

    System.out.println("------------------------------------");

    UserMessage message01 = UserMessage.from(
        "I am doing a demo at Sevilla JUG. Can you introduce yourself and say hello?");
    userMessages = List.of(message01);
    response = model.generate(userMessages);
    AiMessage aiMessage01 = response.content();
    System.out.println(aiMessage01.text());

    System.out.println("------------------------------------");

    UserMessage message02 = UserMessage.from(
        "Where am I today? What am I doing?");
    userMessages = List.of(message01, aiMessage01, message02);
    response = model.generate(userMessages);
    AiMessage aiMessage02 = response.content();
    System.out.println(aiMessage02.text());

    System.out.println("------------------------------------");
  }
}
