package com.swisscom.treequest.domain;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestTreeAttribute {

  @NotBlank(message = "Name is mandatory")
  private String name;

  @NotBlank(message = "value is mandatory")
  private String value;
  private QuestTreeOperations operation;

}
