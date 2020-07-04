package com.swisscom.treequest.service;


import com.swisscom.treequest.domain.QuestTree;

public interface TreeQuestService {

  void addQuestTree(QuestTree questTree);

  QuestTree retrieveQuestTree();

}
