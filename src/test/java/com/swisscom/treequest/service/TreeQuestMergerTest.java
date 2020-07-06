package com.swisscom.treequest.service;

import static com.swisscom.treequest.domain.BrickId.IN_BOTH_TREES;
import static com.swisscom.treequest.domain.BrickId.ONLY_IN_INITIAL;
import static com.swisscom.treequest.domain.QuestTreeOperations.CREATE;
import static com.swisscom.treequest.domain.QuestTreeOperations.DELETE;
import static com.swisscom.treequest.domain.QuestTreeOperations.UPDATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swisscom.treequest.domain.QuestTree;
import com.swisscom.treequest.dto.QuestTreeDto;
import java.io.InputStream;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

class TreeQuestMergerTest {

  @InjectMocks
  private TreeQuestMerger treeQuestMerger;

  @BeforeEach
  void setUp() {
    initMocks(this);
  }

  @Test
  void shouldMergeTree() {
    QuestTree initialTree = readTreeFromFile("initial_tree.json");
    QuestTree newTree = readTreeFromFile("new_tree.json");
    QuestTree questTree = treeQuestMerger.mergeTree(initialTree, newTree);
    Assertions.assertNotNull(questTree);
  }

  @Test
  void shouldMergeTreeOrdered() {
    QuestTree initialTree = readTreeFromFile("initial_tree.json");
    QuestTree newTree = readTreeFromFile("new_tree.json");
    QuestTree orderTree = treeQuestMerger.mergeTree(initialTree, newTree);

    assert orderTree != null;
    assertEquals(orderTree.getChildren().get(0).getId(), "id-1");
    assertEquals(orderTree.getChildren().get(1).getId(), "id-2");
    assertEquals(orderTree.getChildren().get(2).getId(), "id-3");
  }

  @Test
  void shouldSetOperationToDelete() {
    QuestTree initialTree = readTreeFromFile("initial_tree.json");
    QuestTree newTree = readTreeFromFile("new_tree.json");
    QuestTree orderTree = treeQuestMerger.mergeTree(initialTree, newTree);

    assert orderTree != null;
    assertEquals(orderTree.getChildren().get(0).getOperation().orElse(null), DELETE);
    assertEquals(orderTree.getChildren().get(0).getAttributes().get("ServiceId").getOperation(), DELETE);
  }

  @Test
  void shouldSetOperationToCreate() {
    QuestTree initialTree = readTreeFromFile("initial_tree.json");
    QuestTree newTree = readTreeFromFile("new_tree.json");
    QuestTree orderTree = treeQuestMerger.mergeTree(initialTree, newTree);

    assert orderTree != null;
    assertEquals(orderTree.getChildren().get(2).getOperation().orElse(null), CREATE);
    assertEquals(orderTree.getChildren().get(2).getAttributes().get("ServiceId").getOperation(), CREATE);
  }

  @Test
  void shouldMergeAttributesInBothLists() {
    QuestTree initialTree = readTreeFromFile("initial_tree.json");
    QuestTree newTree = readTreeFromFile("new_tree.json");
    QuestTree orderTree = treeQuestMerger.mergeTree(initialTree, newTree);

    assert orderTree != null;
    assertEquals(orderTree.getChildren().get(1).getBrickId().orElse(null), IN_BOTH_TREES);
    assertEquals(orderTree.getChildren().get(1).getAttributes().get("ValueChange").getOperation(), UPDATE);
    assertEquals(orderTree.getChildren().get(1).getAttributes().get("OnlyInInitial").getOperation(), DELETE);
    assertEquals(orderTree.getChildren().get(1).getAttributes().get("OnlyInNew").getOperation(), CREATE);
  }

  @Test
  void shouldSetBrickIdToInitial() {
    QuestTree initialTree = readTreeFromFile("initial_tree.json");
    QuestTree newTree = readTreeFromFile("new_tree.json");
    QuestTree orderTree = treeQuestMerger.mergeTree(initialTree, newTree);
    assert orderTree != null;
    assertEquals(orderTree.getChildren().get(0).getBrickId().orElse(null), ONLY_IN_INITIAL);
  }

  @SneakyThrows
  private QuestTree readTreeFromFile(String fileName) {
    ClassLoader classLoader = TreeQuestMergerTest.class.getClassLoader();
    InputStream inputStream = classLoader.getResourceAsStream(fileName);
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(inputStream, QuestTreeDto.class).toDomain();
  }
}