package com.swisscom.treeguest.service;


import static com.swisscom.treeguest.domain.GuestTreeType.ROOT;

import com.swisscom.treeguest.domain.GuestTree;
import org.springframework.stereotype.Service;

@Service
public class TreeGuestServiceImpl implements TreeGuestService {

  private GuestTree guestTree; //TODO use storage?

  public TreeGuestServiceImpl() {
    guestTree = GuestTree.builder()
        .id("rootId")
        .type(ROOT)
        .build();
  }

  @Override
  public void addGuestTree(GuestTree guestTree) {

  }

  @Override
  public GuestTree retrieveGuestTree() {
    return guestTree;
  }
}
