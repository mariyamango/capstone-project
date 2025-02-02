package com.example.backend.service;

import com.example.backend.dto.JokeResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ChucksJokeServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ChucksJokeService chucksJokeService;

    @Test
    void testGetJokeSuccess() {
        //GIVEN
        JokeResponse jokeResponse = new JokeResponse("Why don't programmers like nature? It has too many bugs.");
        when(restTemplate.getForObject("https://geek-jokes.sameerkumar.website/api?format=json", JokeResponse.class))
                .thenReturn(jokeResponse);
        //WHEN
        String joke = chucksJokeService.getJoke();
        //THEN
        assertEquals("Why don't programmers like nature? It has too many bugs.", joke);
    }

    @Test
    void testGetJokeFailure() {
        //GIVEN
        when(restTemplate.getForObject("https://geek-jokes.sameerkumar.website/api?format=json", JokeResponse.class))
                .thenThrow(new RuntimeException("API unavailable"));
        //WHEN
        String joke = chucksJokeService.getJoke();
        //THEN
        assertEquals("Failed to retrieve a joke.", joke);
    }
}