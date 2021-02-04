package com.felipeheld.grades;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.TestInstance.Lifecycle;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.felipeheld.grades.api.request.CreateNotaRequest;
import com.felipeheld.grades.api.request.UpdateNotaRequest;
import com.felipeheld.grades.repository.base.NotaBaseRepository;

@ActiveProfiles("Integration")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
public class IntegrationTests {

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private NotaBaseRepository notaBaseRepository;    

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private final String ENDPOINT = "/api/v1/notas";

    @BeforeAll
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(WRITE_DATES_AS_TIMESTAMPS);
    }

    @AfterEach
    void cleanup() {
        notaBaseRepository.deleteAll();
    }

    @Test
    @DisplayName("Given nota create request, when issued post, then nota created.")
    public void notaShouldBeCreated() throws Exception {
        // Given
        var request = new CreateNotaRequest("John Stuart Mill", "Filosofia", "A", true);

        // When-Then
        mockMvc.perform(post(ENDPOINT).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))).andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Given created nota, when updated nota, then nota is updated.")
    public void notaShouldBeCreatedThenUpdatedThenSearched() throws Exception {
        final String UPDATE_NOTA = "B";
        final Boolean UPDATE_APROVACAO = true;

        var createRequest = new CreateNotaRequest("Rincewind", "Química", "D", false);
        var updateRequest = new UpdateNotaRequest(UPDATE_NOTA, UPDATE_APROVACAO);

        String[] location = mockMvc
                .perform(post(ENDPOINT).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andReturn().getResponse().getHeader("location").split("/");
        String id = location[location.length - 1];

        mockMvc.perform(patch(ENDPOINT + "/{id}", id).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest))).andExpect(status().isNoContent());

        mockMvc.perform(get(ENDPOINT + "/{id}", id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nota").value(UPDATE_NOTA))
                .andExpect(jsonPath("$.aprovado").value(UPDATE_APROVACAO));
    }

    @Test
    @DisplayName("Given created notas, when searched, then retrieved as per params.")
    public void notasShouldBeCreatedAndRetrieved() throws Exception {
        var createRequestList = List.of(
            new CreateNotaRequest("Worf", "Educação Física", "A", true),
            new CreateNotaRequest("William T. Riker", "Física", "B", true),
            new CreateNotaRequest("Jean-Luc Picard", "Retórica", "A", true),
            new CreateNotaRequest("Wesley Crusher", "Física", "A", true)
        );
        
        for (var request : createRequestList) 
            mockMvc.perform(post(ENDPOINT).contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));
        
        mockMvc.perform(get(ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("estudante", "William T. Riker"))
                .andExpect(jsonPath("[0].estudante").value("William T. Riker"));

        mockMvc.perform(get(ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("disciplina", "Física"))
                .andExpect(jsonPath("[*].estudante", containsInAnyOrder("William T. Riker", "Wesley Crusher")));

        mockMvc.perform(get(ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("nota", "A"))
                .andExpect(jsonPath("[*].estudante", containsInAnyOrder("Worf", "Jean-Luc Picard", "Wesley Crusher")));

        mockMvc.perform(get(ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("aprovado", "true"))
                .andExpect(jsonPath("*", hasSize(createRequestList.size())));
    }
}
