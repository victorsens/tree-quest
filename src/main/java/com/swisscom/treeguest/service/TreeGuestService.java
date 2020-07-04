package com.swisscom.treeguest.service;


import com.swisscom.treeguest.domain.GuestTree;

public interface TreeGuestService {

  void addGuestTree(GuestTree guestTree);

  GuestTree retrieveGuestTree();

}
