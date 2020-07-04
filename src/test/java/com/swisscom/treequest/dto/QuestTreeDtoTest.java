package com.swisscom.treequest.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swisscom.treequest.domain.QuestTree;
import java.io.InputStream;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

class QuestTreeDtoTest {

  @Test
  void shouldConvertDtoToDomain() {
    QuestTreeDto dto = readTreeFromFile();
    QuestTree questTree = dto.toDomain();
    assertNotNull(questTree);
  }

  @Test
  void shouldConvertChildren() {
    QuestTreeDto dto = readTreeFromFile();
    QuestTree questTree = dto.toDomain();
    assertNotNull(questTree);
    assertEquals(questTree.getChildren().size(), 2);
  }

  @Test
  void shouldConvertRelations() {
    QuestTreeDto dto = readTreeFromFile();
    QuestTree questTree = dto.toDomain();
    assertNotNull(questTree);
    assertEquals(questTree.getChildren().get(0).getRelations().get(0), questTree.getChildren().get(1));
  }

  @SneakyThrows
  private QuestTreeDto readTreeFromFile() {
    ClassLoader classLoader = QuestTreeDtoTest.class.getClassLoader();
    InputStream inputStream = classLoader.getResourceAsStream("initial_tree.json");
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(inputStream, QuestTreeDto.class);
  }

}