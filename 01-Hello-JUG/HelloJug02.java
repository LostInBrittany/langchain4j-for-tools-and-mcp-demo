///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS dev.langchain4j:langchain4j-open-ai:1.0.0-beta1

import dev.langchain4j.model.openai.OpenAiChatModel;
import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_O_MINI;

/** ------------------------------------------------------- 
 ** By construction, each message is fully independant,     
 ** stateless                           
 ** ------------------------------------------------------- */
public class HelloJug02 {
  public static void main(String... args) {
    String apiKey = System.getenv("OPENAI_API_KEY");
    OpenAiChatModel model = OpenAiChatModel.builder()
      .apiKey(apiKey)
      .modelName(GPT_4_O_MINI)
      .build();

    System.out.println("------------------------------------");

    String response = model.chat(
      "I am doing a demo at Madrid JUG. Can you introduce yourself and say hello?");
    System.out.println(response);

    System.out.println("------------------------------------");

    response = model.chat(
      "Where am I today? What am I doing?");
    System.out.println(response);

    System.out.println("------------------------------------");
  }
}
