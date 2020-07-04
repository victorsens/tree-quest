package com.swisscom.treequest.controller;

import com.swisscom.treequest.dto.QuestTreeDto;
import com.swisscom.treequest.service.TreeQuestService;
import io.swagger.annotations.Api;
import java.net.URI;
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
@Api("Tree guest operations") //TODO To find a best description to this API.
@Slf4j
public class TreeQuestController {

  protected static final String VERSION_PATH = "/v1/";
  private static final String RETRIEVE_URL =  "retrieve-tree";
  private static final String ADD_TREE_URL =  "add-tree";
  private static final String CLEAN_TREE_URL =  "delete-tree";

  private final TreeQuestService treeGuestService;


  public TreeQuestController(final TreeQuestService treeGuestService) {
    this.treeGuestService = treeGuestService;
  }

  @SneakyThrows
  @PostMapping(ADD_TREE_URL)
  public ResponseEntity<QuestTreeDto> addTree(@RequestBody QuestTreeDto questTree) {
    log.info("add-tree endpoint started: {}", questTree);
    treeGuestService.addQuestTree(questTree.toDomain());
    return ResponseEntity.created(new URI(VERSION_PATH + RETRIEVE_URL)).body(questTree); //TODO  think in a way to be not necessary concat teh Strings
  }

  @DeleteMapping(CLEAN_TREE_URL)
  public void cleanTree() {
    log.info("clean-tree endpoint started ");
    treeGuestService.clenaTree();
  }

  @GetMapping(path = RETRIEVE_URL)
  @ResponseBody
  public ResponseEntity<QuestTreeDto> retrieveTree() {
    log.info("retrieve-tree endpoint started");
    QuestTreeDto dto = new QuestTreeDto(treeGuestService.retrieveQuestTree());
    return ResponseEntity.ok(dto);
  }

}
