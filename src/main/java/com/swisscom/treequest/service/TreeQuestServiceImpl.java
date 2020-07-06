package com.swisscom.treequest.service;


import static com.swisscom.treequest.domain.BrickId.ROOT;
import static com.swisscom.treequest.domain.QuestTreeOperations.NO_ACTION;

import com.swisscom.treequest.domain.QuestTree;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TreeQuestServiceImpl implements TreeQuestService {

  private QuestTree initialTree;
  private QuestTree newTree;

  private final TreeQuestMerger treeQuestMerger;

  public TreeQuestServiceImpl(TreeQuestMerger treeQuestMerger) {
    this.treeQuestMerger = treeQuestMerger;
  }

  @Override
  public void addInitialQuestTree(@Valid @NotNull final QuestTree questTree) {
    log.info("Add initial tree {}", questTree);
    initialTree = questTree;
  }

  @Override
  public void addNewQuestTree(@Valid @NotNull QuestTree questTree) {
    log.info("Add new tree {}", questTree);
    newTree = questTree;
  }

  @Override
  public QuestTree mergeQuestTrees() {
    log.info("start to merge the trees");
    if(initialTree != null && newTree != null) {
      final QuestTree mergedTree = initialTree.clone();
      mergedTree.setOperation(NO_ACTION);
      mergedTree.setBrickId(ROOT);
      mergedTree.getAttributes().values().forEach(att -> att.setOperation(NO_ACTION));
      return treeQuestMerger.mergeTree(mergedTree, newTree);
    } else {
      throw new IllegalStateException("initialTree and newTree should be defined first to call this method.");
    }

  }

  @Override
  public void cleanTrees() {
    initialTree = null;
    newTree  = null;
  }


}
