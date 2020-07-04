package com.swisscom.treequest.controller;

import com.swisscom.treequest.dto.QuestTreeDto;
import com.swisscom.treequest.service.TreeQuestService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/")
@Api("Tree guest operations") //TODO To find a best description to this API.
@Slf4j
public class TreeQuestController {

  private final TreeQuestService treeGuestService;

  public TreeQuestController(final TreeQuestService treeGuestService) {
    this.treeGuestService = treeGuestService;
  }

  @RequestMapping(path = "add-tree", method = RequestMethod.POST)
  public void addTree(@RequestBody QuestTreeDto questTree) {
    log.info("add-tree endpoint started: {}", questTree);
    treeGuestService.addQuestTree(questTree.toDomain());
  }

  @RequestMapping(path = "retrieve-tree", method = RequestMethod.GET)
  @ResponseBody
  public ResponseEntity<QuestTreeDto> retrieveTree() {
    log.info("retrieve-tree endpoint started");
    QuestTreeDto dto = new QuestTreeDto(treeGuestService.retrieveQuestTree());
    return ResponseEntity.ok(dto);
  }

}
