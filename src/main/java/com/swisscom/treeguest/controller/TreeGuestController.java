package com.swisscom.treeguest.controller;

import com.swisscom.treeguest.domain.GuestTree;
import com.swisscom.treeguest.service.TreeGuestService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/")
@Api("Tree guest operations") //TODO To find a best description to this API.
@Slf4j
public class TreeGuestController {

  private final TreeGuestService treeGuestService;

  public TreeGuestController(final TreeGuestService treeGuestService) {
    this.treeGuestService = treeGuestService;
  }

  @RequestMapping(path = "add-tree", method = RequestMethod.POST)
  public void addTree() {
    //TODO do it.
  }

  @RequestMapping(path = "get-tree", method = RequestMethod.GET)
  @ResponseBody
  public ResponseEntity<GuestTree> getTree() { //TODO think instead the entity use a DTO
    return ResponseEntity.ok(treeGuestService.retrieveGuestTree());
  }

}
