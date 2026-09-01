package com.email.email_writer.Service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.email.email_writer.DTO.EmailRequest;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;


@Service
public class EmailgeneratorService {

    private final WebClient webClient;
    public EmailgeneratorService( WebClient.Builder webClientBuilder )
    {
        this.webClient = webClientBuilder.build();
    }

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public String generateEmail(EmailRequest emailRequest)
    {
        System.out.println("hello generateEmail");

        //Build the prompt 
        String prompt= buildPrompt(emailRequest);
        
        //craft a request
        Map<String,Object> requestBody = Map.of(
            "contents" , new Object[] {
                Map.of("parts", new Object[] {
                 Map.of("text" , prompt)
                
                }     
              )
            }
        );


        // DO request and get Response
            System.out.println("BEFORE GEMINI API CALL");
            
            String response = webClient.post()
                    .uri(geminiApiUrl)
                    .header("x-goog-api-key", geminiApiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            System.out.println("After GEMINI API CALL");
        
        //Extract response and Return 
        System.out.println("hello returningg respons");
        return extractResponseContent(response);

    }

    private String extractResponseContent(String response)
    { 
      
            try{
                System.out.println("hello extractResponseContent");

                ObjectMapper mapper =new ObjectMapper();
                JsonNode rootNode = mapper.readTree(response);
                return rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();

            } catch(Exception e){

                return " Error processing request : " + e.getMessage();
             }

    }
    private String buildPrompt(EmailRequest emailRequest)
    {
            System.out.println("hello buildPrompt");

            StringBuilder prompt = new StringBuilder();
            prompt.append("""
            
            You are an AI email assistant.

            Your task is to write a natural and appropriate reply
            to the original email.

            Follow these rules:
            1. Reply directly to the original email.
            2. Use the requested tone.
            3. Follow the requested reply length.
            4. Do not generate a subject line.
            5. Do not invent facts, names, dates, or commitments.
            6. Never assume information that is not present in the original email.
            7. Never claim to check calendars, schedules, systems, or availability unless that information is explicitly provided.
            8. Do not use placeholders such as [Name], [Your Name],[Company], etc.
            9. Return only the email reply.
            10. Do not explain your reasoning.

            """);


            if(emailRequest.getTone() != null && !emailRequest.getTone().isEmpty())
            {
                prompt.append("Use a ")
                      .append(emailRequest.getTone())
                      .append(" tone. ");
            }

            if(emailRequest.getLength() != null && !emailRequest.getLength().isEmpty())
            {
                prompt.append("Reply Length : ")
                      .append(emailRequest.getLength())
                      .append("\n");
            }

            prompt.append(" \nOriginal Email : \n").append(emailRequest.getEmailcontent());
            
            System.out.println("returning from buildPrompt");

            return prompt.toString();
    }

}
