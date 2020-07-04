package com.swisscom.treequest.dto;

import static com.swisscom.treequest.domain.QuestTreeType.ROOT;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

import com.swisscom.treequest.domain.QuestTree;
import com.swisscom.treequest.domain.QuestTreeType;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestTreeDto {

  private String id;
  private String type;

  @Builder.Default
  private List<QuestTreeDto> children = emptyList();

  @Builder.Default
  private List<Map<String,String>> attributes = emptyList();

  @Builder.Default
  private List<String> relations = emptyList();

  public QuestTreeDto(QuestTree questTree) {
    this.id = questTree.getId();
    this.type = questTree.getType().toString();
    this.attributes = questTree.getAttributes();
    this.relations = questTree.getRelations().stream().map(QuestTree::getId).collect(toList());
    this.children = questTree.getChildren().stream().map(QuestTreeDto::new).collect(toList());
  }

  public QuestTree toDomain() {
    final QuestTree questTree = QuestTree.builder()
        .id(id)
        .type(QuestTreeType.valueOf(type))
        .attributes(attributes)
        .children(children.stream().map(QuestTreeDto::toDomain).collect(toList()))
        .build();

    if(questTree.getType() == ROOT) {
      addRelations(questTree, this);
    }

    return questTree;
  }
  private void addRelations(QuestTree rootTree, QuestTreeDto dto) {
    QuestTree subTree = rootTree.scanById(dto.getId());
    final List<QuestTree> relations = dto.relations.stream().map(rootTree::scanById)
        .collect(toList());
    subTree.setRelations(relations);
    dto.children.forEach(childDto -> addRelations(rootTree,childDto));
  }

}
