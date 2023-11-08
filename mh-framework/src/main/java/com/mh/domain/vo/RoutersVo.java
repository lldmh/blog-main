package com.mh.domain.vo;

import com.mh.domain.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author dmh
 * @Date 2023/7/25 16:09
 * @Version 1.0
 * @introduce  后台动态路由vo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoutersVo {
    private List<MenuVo> menus;
}
