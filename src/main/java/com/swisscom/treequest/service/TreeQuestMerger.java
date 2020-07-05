package com.swisscom.treequest.service;

import static com.swisscom.treequest.domain.BrickId.IN_BOTH_TREES;
import static com.swisscom.treequest.domain.BrickId.ONLY_IN_INITIAL;
import static com.swisscom.treequest.domain.BrickId.ONLY_IN_NEW;
import static com.swisscom.treequest.domain.BrickId.ROOT;
import static com.swisscom.treequest.domain.QuestTreeOperations.CREATE;
import static com.swisscom.treequest.domain.QuestTreeOperations.DELETE;
import static com.swisscom.treequest.domain.QuestTreeOperations.NO_ACTION;
import static com.swisscom.treequest.domain.QuestTreeOperations.UPDATE;
import static java.util.Collections.sort;

import com.swisscom.treequest.domain.QuestTree;
import com.swisscom.treequest.domain.QuestTreeAttribute;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class TreeQuestMerger { //TODO think best package to set it.

    public QuestTree mergeTree(QuestTree originalTree, final QuestTree newTree) {
      final QuestTree mergedTree =  originalTree.clone();
      mergedTree.setOperation(NO_ACTION);
      mergedTree.setBrickId(ROOT);
      mergedTree.getAttributes().values().forEach(att -> att.setOperation(NO_ACTION));

      newTree.getChildren().forEach(newChild -> {
        final QuestTree mergedChild = mergedTree.scanById(newChild.getId());
        if(mergedChild == null) {
          addToTree(mergedTree, newChild.clone());
        } else {
          updateNode(mergedChild, newChild.clone());
        }

      });

      mergedTree.getChildren().forEach(originalChild -> {
        final QuestTree newChild = newTree.scanById(originalChild.getId());
        if(newChild == null) {
          originalChild.setOperation(DELETE);
          originalChild.setBrickId(ONLY_IN_INITIAL);
          originalChild.getAttributes().values().forEach(att -> att.setOperation(DELETE));
        }
      });

      return mergedTree;
    }

    private void addToTree(final QuestTree mergedChild, final QuestTree newChild) {
      newChild.setBrickId(ONLY_IN_NEW);
      newChild.setOperation(CREATE);
      newChild.getAttributes().values().forEach(att -> att.setOperation(CREATE));
      mergedChild.getChildren().add(newChild);
      sort(mergedChild.getChildren());
    }

    private void updateNode(final QuestTree merged, final QuestTree newOne) {
      merged.setType(newOne.getType().orElse(null));
      merged.setOperation(UPDATE);
      merged.setRelations(newOne.getRelations());
      merged.setBrickId(IN_BOTH_TREES);
//      merged.setAttributes(newOne.getAttributes()); //TODO merge it.. not just change.
      mergeAttributes(merged.getAttributes(),newOne.getAttributes());
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
