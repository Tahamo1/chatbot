package com.mohammaditaha.chatbot.agents;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class AIAgent {
    private ChatClient chatClient;

    public AIAgent(ChatClient.Builder builder ,
                   ChatMemory memory, ToolCallbackProvider tools) {
        Arrays.Streams(tools.getToolCallbacks()).forEach(toolCallback -> {
            System.out.println("---------------");
            System.out.println(toolCallback.getToolDefinition());
                    });
        this.chatClient = builder
                .defaultSystem("""
                      
                           Vous un assistant qui se charge de repondre aux questions de l'utilisateur en fonction du contexte fourni .
                           Si aucun contexte n'est fourni , repond avec JE NE SAIS PAS 
                        """)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(memory).build())
                .defaultToolCallbacks(tools)
                .build();
    }

    public Flux<String> askAgent(String query) {
        return chatClient.prompt()
                .user(query)
                .stream().content();
    }
}