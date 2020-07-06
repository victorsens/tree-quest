package com.swisscom.treequest.service;

import static com.swisscom.treequest.domain.QuestTree.builder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
import org.mockito.Mock;
import org.mockito.Mockito;

class TreeQuestServiceTest {

  @InjectMocks
  private TreeQuestServiceImpl treeQuestService;

  @Mock
  private TreeQuestMerger treeQuestMerger;

  @BeforeEach
  void setUp() {
    initMocks(this);
    Mockito.when(treeQuestMerger.mergeTree(any(), any())).thenReturn(builder().build());
  }

  @Test
  void shouldMergeTrees() {
    QuestTree initialTree = readTreeFromFile("initial_tree.json");
    QuestTree newTree = readTreeFromFile("new_tree.json");
    treeQuestService.addInitialQuestTree(initialTree);
    treeQuestService.addNewQuestTree(newTree);
    QuestTree mergedTree = treeQuestService.mergeQuestTrees();
    Assertions.assertNotNull(mergedTree);
  }

  @Test
  void shouldValidateNullTree() {
    IllegalStateException exception =
        assertThrows(IllegalStateException.class, () -> treeQuestService.mergeQuestTrees());
    assertEquals(exception.getMessage(),"initialTree and newTree should be defined first to call this method.");
  }

  @Test
  void shouldCleanTrees() {
    QuestTree initialTree = readTreeFromFile("initial_tree.json");
    QuestTree newTree = readTreeFromFile("new_tree.json");
    treeQuestService.addInitialQuestTree(initialTree);
    treeQuestService.addNewQuestTree(newTree);
    QuestTree mergedTree = treeQuestService.mergeQuestTrees();
    Assertions.assertNotNull(mergedTree);

    treeQuestService.cleanTrees();

    IllegalStateException exception =
        assertThrows(IllegalStateException.class, () -> treeQuestService.mergeQuestTrees());
    assertEquals(exception.getMessage(),"initialTree and newTree should be defined first to call this method.");

  }


  @SneakyThrows
  private QuestTree readTreeFromFile(String fileName) {
    ClassLoader classLoader = TreeQuestMergerTest.class.getClassLoader();
    InputStream inputStream = classLoader.getResourceAsStream(fileName);
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(inputStream, QuestTreeDto.class).toDomain();
  }



}