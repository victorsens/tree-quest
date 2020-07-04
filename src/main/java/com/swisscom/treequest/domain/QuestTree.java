package com.swisscom.treequest.domain;

import static java.util.Collections.emptyList;

import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class QuestTree {

  @Builder.Default
  private String id = "";

  private QuestTreeType type;

  private QuestTreeOperations operations;

  @Builder.Default
  private List<QuestTree> children = emptyList();

  @Builder.Default
  private List<Map<String,String>> attributes = emptyList();

  @Builder.Default
  private List<QuestTree> relations = emptyList();

  public QuestTree scanById(String id) {
    if (this.id.equals(id)) {
      return this;
    } else {
      for (QuestTree child : children) {
        if (child.id.equals(id)) { //TODO think how to create a full recursive method. (this one is just for the first level of children)
          return child;
        }
      }
    }
    return null;
  }
}
