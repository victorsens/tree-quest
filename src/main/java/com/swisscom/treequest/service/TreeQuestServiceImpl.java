package com.swisscom.treequest.service;


import static com.swisscom.treequest.domain.BrickId.ROOT;
import static com.swisscom.treequest.domain.QuestTreeOperations.NO_ACTION;

import com.swisscom.treequest.domain.QuestTree;
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
  public void addInitialQuestTree(final QuestTree questTree) {
    log.info("Add initial tree {}", questTree);
    initialTree = questTree;
  }

  @Override
  public void addNewQuestTree(QuestTree questTree) {
    log.info("Add new tree {}", questTree);
    newTree = questTree;
  }

  @Override
  public QuestTree mergeQuestTrees() {
    log.info("start to merge the trees");
    final QuestTree mergedTree = initialTree.clone();
    mergedTree.setOperation(NO_ACTION);
    mergedTree.setBrickId(ROOT);
    mergedTree.getAttributes().values().forEach(att -> att.setOperation(NO_ACTION));
    return treeQuestMerger.mergeTree(mergedTree, newTree);
  }

  @Override
  public void cleanTrees() {
    initialTree = null;
    newTree  = null;
  }


}
