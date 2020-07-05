package com.swisscom.treequest.service;


import com.swisscom.treequest.domain.QuestTree;

public interface TreeQuestService {

  void addInitialQuestTree(QuestTree questTree);

  QuestTree mergeQuestTrees();

  void cleanTrees();

  void addNewQuestTree(QuestTree toDomain);
}
