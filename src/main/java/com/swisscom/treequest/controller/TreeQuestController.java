package com.swisscom.treequest.controller;

import com.swisscom.treequest.dto.QuestTreeDto;
import com.swisscom.treequest.service.TreeQuestService;
import io.swagger.annotations.Api;
import java.net.URI;
import javax.validation.Valid;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = TreeQuestController.VERSION_PATH)
@Api("Project Assignment TCSP")
@Slf4j
public class TreeQuestController {

  protected static final String VERSION_PATH = "/v1/";
  private static final String MERGE_TREES_URL =  "merge-trees";
  private static final String ADD_INITIAL_TREE_URL =  "add-initial-tree";
  private static final String ADD_NEW_TREE_URL = "add-new-tree";
  private static final String CLEAN_TREE_URL =  "clean-tree";

  private final TreeQuestService treeGuestService;


  public TreeQuestController(final TreeQuestService treeGuestService) {
    this.treeGuestService = treeGuestService;
  }

  @SneakyThrows
  @PostMapping(ADD_INITIAL_TREE_URL)
  public ResponseEntity<QuestTreeDto> addInitialTree(@Valid @RequestBody QuestTreeDto questTree) {
    log.info("add-tree endpoint started: {}", questTree);
    treeGuestService.addInitialQuestTree(questTree.toDomain());
    return ResponseEntity.created(new URI(VERSION_PATH + MERGE_TREES_URL)).body(questTree); //TODO  think in a way to be not necessary concat teh Strings
  }

  @SneakyThrows
  @PostMapping(ADD_NEW_TREE_URL)
  public ResponseEntity<QuestTreeDto> addNewTree(@Valid @RequestBody QuestTreeDto questTree) {
    log.info("add-tree endpoint started: {}", questTree);
    treeGuestService.addNewQuestTree(questTree.toDomain());
    return ResponseEntity.created(new URI(VERSION_PATH + MERGE_TREES_URL)).body(questTree); //TODO  think in a way to be not necessary concat teh Strings
  }

  @DeleteMapping(CLEAN_TREE_URL)
  public void cleanTree() {
    log.info("clean-tree endpoint started ");
    treeGuestService.cleanTrees();
  }

  @GetMapping(path = MERGE_TREES_URL)
  @ResponseBody
  public ResponseEntity<QuestTreeDto> mergeTrees() {
    log.info("retrieve-tree endpoint started");
    QuestTreeDto dto = new QuestTreeDto(treeGuestService.mergeQuestTrees());
    return ResponseEntity.ok(dto);
  }

}
