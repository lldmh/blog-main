package com.mh.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author dmh
 * @Date 2023/7/26 16:44
 * @Version 1.0
 * @introduce
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public class TagByIdDto {
    private Long id;
    //标签名
    private String name;
    //备注
    private String remark;
}
