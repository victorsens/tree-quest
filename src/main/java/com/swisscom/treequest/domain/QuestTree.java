package com.swisscom.treequest.domain;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;

@Builder
@Data
public class QuestTree implements Comparable<QuestTree>, Cloneable {

  @Builder.Default
  private String id = "";

  private BrickId brickId;

  private QuestTreeType type;

  private QuestTreeOperations operation;

  @Builder.Default
  private List<QuestTree> children =  emptyList();

  @Builder.Default
  private Map<String, QuestTreeAttribute> attributes = emptyMap();

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

  public Optional<BrickId> getBrickId() {
    return ofNullable(brickId);
  }

  public Optional<QuestTreeType> getType() {
    return ofNullable(type);
  }

  public Optional<QuestTreeOperations> getOperation() {
    return ofNullable(operation);
  }

  @SneakyThrows
  @Override
  public QuestTree clone()  {
    super.clone();
    return QuestTree.builder()
        .id(id)
        .brickId(brickId)
        .type(type)
        .operation(operation)
        .children(children.stream().map(QuestTree::clone).collect(toList()))
        .relations(relations.stream().map(QuestTree::clone).collect(toList()))
        .attributes(attributes.values().stream().map(att -> QuestTreeAttribute
            .builder()
            .name(att.getName())
            .value(att.getValue())
            .operation(att.getOperation())
            .build()).collect(toMap(QuestTreeAttribute::getName, att -> att)))
        .build();
  }

  @Override
  public int compareTo(QuestTree newTree) {
    return this.id.compareTo(newTree.id);
  }
}
