package com.ruoyi.generator.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

@Data
@TableName("ai_knowledge")
public class AiKnowledge extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 文档标题 */
    private String title;

    /** 文档内容 */
    private String content;

    /** 关联文件URL */
    private String fileUrl;

    /** 分类（如policy/enterprise） */
    private String category;

    // 无需重复定义createTime/updateTime，父类已包含
}