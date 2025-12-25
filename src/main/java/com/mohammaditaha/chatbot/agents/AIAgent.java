package com.mohammaditaha.chatbot.agents;

import com.mohammaditaha.chatbot.tools.AITools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class AIAgent {
    private ChatClient chatClient;

    public AIAgent(ChatClient.Builder builder ,
                   ChatMemory memory ,
                   AITools tools) {
        this.chatClient = builder
                .defaultSystem("""
                      
                           Vous un assistant qui se charge de repondre aux questions de l'utilisateur en fonction du contexte fourni .
                           Si aucun contexte n'est fourni , repond avec JE NE SAIS PAS 
                        """)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(memory).build())
                .defaultTools()
                .build();
    }

    public Flux<String> askAgent(String query) {
        return chatClient.prompt()
                .user(query)
                .stream().content();
    }
}