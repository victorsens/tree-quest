package com.swisscom.treequest.service;


import static com.swisscom.treequest.domain.QuestTreeType.ROOT;

import com.swisscom.treequest.domain.QuestTree;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TreeQuestServiceImpl implements TreeQuestService {

  private QuestTree questTree; //TODO use storage?

  public TreeQuestServiceImpl() {
    questTree = QuestTree.builder()
        .id("rootId")
        .type(ROOT)
        .build();
  }

  @Override
  public void addQuestTree(QuestTree questTree) {
    log.info("start to add merge the trees {}", questTree);
    this.questTree = questTree;
  }

  @Override
  public QuestTree retrieveQuestTree() {
    return questTree;
  }
}
