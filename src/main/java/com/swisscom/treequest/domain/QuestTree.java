package com.swisscom.treequest.domain;

import static com.swisscom.treequest.domain.BrickId.IN_BOTH_TREES;
import static com.swisscom.treequest.domain.BrickId.ONLY_IN_NEW;
import static com.swisscom.treequest.domain.BrickId.ROOT;
import static com.swisscom.treequest.domain.QuestTreeOperations.CREATE;
import static com.swisscom.treequest.domain.QuestTreeOperations.DELETE;
import static com.swisscom.treequest.domain.QuestTreeOperations.NO_ACTION;
import static com.swisscom.treequest.domain.QuestTreeOperations.UPDATE;
import static java.util.Collections.emptyList;
import static java.util.Collections.sort;
import static java.util.Optional.ofNullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class QuestTree implements Comparable<QuestTree> {

  @Builder.Default
  private String id = "";

  private BrickId brickId;

  private QuestTreeType type;

  private QuestTreeOperations operation;

  @Builder.Default
  private List<QuestTree> children =  emptyList();

  @Builder.Default
  private List<Map<String,String>> attributes = emptyList(); //TODO create a class to represent it.

  @Builder.Default
  private List<QuestTree> relations = emptyList();

  public QuestTree mergeTree(QuestTree newTree) {
    this.operation = NO_ACTION; //TODO think to clone, instead to update the same object
    this.brickId = ROOT;
    newTree.children.forEach(newChild -> {
      final QuestTree originalChild = this.scanById(newChild.getId());
      if(originalChild == null) {
       addToTree(newChild);
      } else {
        updateNode(originalChild, newChild);
      }

    });

    this.children.forEach(originalChild -> {
      final QuestTree newChild = newTree.scanById(originalChild.getId());
      if(newChild == null) {
        originalChild.setOperation(DELETE);
        originalChild.getAttributes().forEach(stringStringMap -> stringStringMap.put("operation", DELETE.toString()));
      }
    });

    return this;
  }

  private void addToTree(QuestTree newChild) {
    newChild.brickId = ONLY_IN_NEW;
    newChild.operation = CREATE;
    this.children.add(newChild);
    sort(this.children);
  }

  private void updateNode(QuestTree original, QuestTree newOne) {
    original.setType(newOne.type);
    original.operation = UPDATE;
    original.relations = newOne.getRelations();
    original.brickId = IN_BOTH_TREES;
    original.attributes = newOne.getAttributes(); //TODO merge it.. not just change.
  }

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

  @Override
  public int compareTo(QuestTree newTree) {
    return this.id.compareTo(newTree.id);
  }
}
