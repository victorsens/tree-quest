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
import org.springframework.stereotype.Component;

@Component
public class TreeQuestMerger { //TODO think best package to set it.

    public QuestTree mergeTree(QuestTree originalTree, final QuestTree newTree) {
      final QuestTree mergedTree =  originalTree.clone();
      mergedTree.setOperation(NO_ACTION);
      mergedTree.setBrickId(ROOT);
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
          originalChild.getAttributes().forEach(stringStringMap -> stringStringMap.put("operation", DELETE.toString()));
        }
      });

      return mergedTree;
    }

    private void addToTree(final QuestTree mergedChild, final QuestTree newChild) {
      newChild.setBrickId(ONLY_IN_NEW);
      newChild.setOperation(CREATE);
      newChild.getAttributes().forEach(stringStringMap -> stringStringMap.put("operation", CREATE.toString()));
      mergedChild.getChildren().add(newChild);
      sort(mergedChild.getChildren());
    }

    private void updateNode(final QuestTree original, final QuestTree newOne) {
      original.setType(newOne.getType().orElse(null));
      original.setOperation(UPDATE);
      original.setRelations(newOne.getRelations());
      original.setBrickId(IN_BOTH_TREES);
      original.setAttributes(newOne.getAttributes()); //TODO merge it.. not just change.
    }
}
