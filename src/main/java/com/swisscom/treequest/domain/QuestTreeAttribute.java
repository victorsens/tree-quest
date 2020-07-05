package com.swisscom.treequest.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestTreeAttribute {

  private String name;
  private String value;
  private QuestTreeOperations operation;

}
