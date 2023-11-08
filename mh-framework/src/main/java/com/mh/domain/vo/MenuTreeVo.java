package com.mh.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Author dmh
 * @Date 2023/7/31 18:39
 * @Version 1.0
 * @introduce
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class MenuTreeVo {

    private Long id;

    private String label;

    private Long parentId;

    private List<MenuTreeVo> children;
}
