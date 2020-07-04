package com.swisscom.treeguest.domain;

import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GuestTree {

  private String id;
  private GuestTreeType type;
  private GuestTreeOperations operations;
  private List<GuestTree> children;
  private Map<String,String> attributes;
  private List<GuestTree> relations;

}
