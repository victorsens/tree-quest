package com.swisscom.treequest.service;

import static com.swisscom.treequest.domain.BrickId.IN_BOTH_TREES;
import static com.swisscom.treequest.domain.BrickId.ONLY_IN_INITIAL;
import static com.swisscom.treequest.domain.BrickId.ONLY_IN_NEW;
import static com.swisscom.treequest.domain.QuestTreeOperations.CREATE;
import static com.swisscom.treequest.domain.QuestTreeOperations.DELETE;
import static com.swisscom.treequest.domain.QuestTreeOperations.NO_ACTION;
import static com.swisscom.treequest.domain.QuestTreeOperations.UPDATE;
import static java.util.Collections.sort;

import com.swisscom.treequest.domain.QuestTree;
import com.swisscom.treequest.domain.QuestTreeAttribute;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

@Component
public class TreeQuestMerger {


    public QuestTree mergeTree(@NotNull final QuestTree originalTree, @NotNull final QuestTree newTree) {
      newTree.getChildren().forEach(newChild -> {
        final QuestTree mergedChild = originalTree.scanById(newChild.getId());
        if(mergedChild == null) {
          addChildToTree(originalTree, newChild.clone());
        } else {
          updateNode(mergedChild, newChild.clone());
        }

      });

      originalTree.getChildren().forEach(originalChild -> {
        final QuestTree newChild = newTree.scanById(originalChild.getId());
        if(newChild == null) {
          deleteTree(originalChild);
        }
      });

      return originalTree;
    }

    private void addChildToTree(final QuestTree mergedTree, final QuestTree newChild) {
      mergedTree.getChildren().add(newChild);
      sort(mergedTree.getChildren());
      setCreateAttributeInAllTree(newChild);
    }

    private void setCreateAttributeInAllTree(final QuestTree tree) {
      tree.setBrickId(ONLY_IN_NEW);
      tree.setOperation(CREATE);
      tree.getAttributes().values().forEach(att -> att.setOperation(CREATE));
      tree.getChildren().forEach(this::setCreateAttributeInAllTree);
    }

    private void deleteTree(QuestTree tree) {
      tree.setOperation(DELETE);
      tree.setBrickId(ONLY_IN_INITIAL);
      tree.getAttributes().values().forEach(att -> att.setOperation(DELETE));
      tree.getChildren().forEach(this::deleteTree);
    }

    private void updateNode(final QuestTree merged, final QuestTree newOne) {
      merged.setType(newOne.getType().orElse(null));
      merged.setOperation(UPDATE);
      merged.setRelations(newOne.getRelations());
      merged.setBrickId(IN_BOTH_TREES);
      mergeAttributes(merged.getAttributes(), newOne.getAttributes());
      mergeTree(merged, newOne);
    }

    private void mergeAttributes(Map<String, QuestTreeAttribute> mergedAttributes, Map<String, QuestTreeAttribute> newAttributes) {
      //Setting NO_ACTION operation
      mergedAttributes.values().stream()
          .filter(att -> newAttributes.containsKey(att.getName()))
          .filter(att -> newAttributes.get(att.getName()).getValue().equals(att.getValue()))
          .forEach(att -> att.setOperation(NO_ACTION));

      //update value and set UPDATE operation
      mergedAttributes.values().stream()
          .filter(att -> newAttributes.containsKey(att.getName()))
          .filter(att -> !newAttributes.get(att.getName()).getValue().equals(att.getValue()))
          .forEach(att -> {
            att.setOperation(UPDATE);
            att.setValue(newAttributes.get(att.getName()).getValue());
          });

      // set DELETE operation.
      mergedAttributes.values().stream()
          .filter(att -> !newAttributes.containsKey(att.getName()))
          .forEach(att -> att.setOperation(DELETE));

      //Add new attributes to the merged tree and set teh CREATE operation.
      mergedAttributes.putAll(newAttributes.values().stream()
          .filter(newAtt ->  !mergedAttributes.containsKey(newAtt.getName()))
          .map(att -> QuestTreeAttribute.builder()
              .name(att.getName())
              .value(att.getValue())
              .operation(CREATE)
              .build())
          .collect(Collectors.toMap(QuestTreeAttribute::getName, att -> att)));
    }
}
