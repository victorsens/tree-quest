package com.swisscom.treequest.controller;

import static com.swisscom.treequest.domain.QuestTreeOperations.DELETE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swisscom.treequest.dto.QuestTreeDto;
import java.io.InputStream;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class TreeQuestControllerTest {
  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @BeforeEach
  void shouldAddTree() {
    QuestTreeDto initialTree = readTreeFromFile("initial_tree.json");
    ResponseEntity<String> resourceAdd = restTemplate.postForEntity("http://localhost:" + port + "/v1/add-tree", initialTree, String.class);
    assertEquals(resourceAdd.getStatusCode(), HttpStatus.CREATED);

    QuestTreeDto newTree = readTreeFromFile("new_tree.json");
    resourceAdd = restTemplate.postForEntity("http://localhost:" + port + "/v1/add-tree", newTree, String.class);
    assertEquals(resourceAdd.getStatusCode(), HttpStatus.CREATED);
  }



  @Test
  void shouldMergeTreeOrdered() {
    ResponseEntity<QuestTreeDto> resourceRetrieve  = restTemplate.getForEntity("http://localhost:" + port + "/v1/retrieve-tree", QuestTreeDto.class);
    assertEquals(resourceRetrieve.getStatusCode(), HttpStatus.OK);
    QuestTreeDto orderTree = resourceRetrieve.getBody();
    assert orderTree != null;
    assertEquals(orderTree.getChildren().get(0).getId(), "id-1");
    assertEquals(orderTree.getChildren().get(1).getId(), "id-2");
    assertEquals(orderTree.getChildren().get(2).getId(), "id-3");
  }

  @Test
  void shouldSetOperationToDelete() {

    ResponseEntity<QuestTreeDto> resourceRetrieve  = restTemplate.getForEntity("http://localhost:" + port + "/v1/retrieve-tree", QuestTreeDto.class);
    assertEquals(resourceRetrieve.getStatusCode(), HttpStatus.OK);
    QuestTreeDto orderTree = resourceRetrieve.getBody();
    assert orderTree != null;
    assertEquals(orderTree.getChildren().get(0).getOperation(), DELETE.toString());
    assertEquals(orderTree.getChildren().get(0).getAttributes().get(0).get("operation"), DELETE.toString());

  }

  @SneakyThrows
  private QuestTreeDto readTreeFromFile(String fileName) {
    ClassLoader classLoader = TreeQuestControllerTest.class.getClassLoader();
    InputStream inputStream = classLoader.getResourceAsStream(fileName);
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(inputStream, QuestTreeDto.class);
  }

}