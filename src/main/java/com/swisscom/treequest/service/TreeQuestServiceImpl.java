package com.swisscom.treequest.service;


import com.swisscom.treequest.domain.QuestTree;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TreeQuestServiceImpl implements TreeQuestService {

  private QuestTree rootTree; //TODO use storage?

  private final TreeQuestMerger treeQuestMerger;

  public TreeQuestServiceImpl(TreeQuestMerger treeQuestMerger) {
    this.treeQuestMerger = treeQuestMerger;
  }


  @Override
  public void addQuestTree(final QuestTree questTree) {
    log.info("start to add merge the trees {}", questTree);
    rootTree = rootTree == null ? questTree : treeQuestMerger.mergeTree(rootTree, questTree);
  }

  @Override
  public QuestTree retrieveQuestTree() {
    return rootTree;
  }

  @Override
  public void clenaTree() {
    rootTree = null;
  }
}
