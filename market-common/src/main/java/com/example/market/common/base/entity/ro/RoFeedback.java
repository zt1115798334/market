package com.example.market.common.base.entity.ro;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/07/12 15:14
 * description:
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class RoFeedback{
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     *
     */
    private Short feedbackType;
    /**
     *
     */
    private String content;
    /**
     * 联系方式
     */
    private String contactMode;

    /**
     * 图片集合
     */
    @ApiModelProperty(value = "图片集合")
    private List<RoImagePath> topicImgList;

    public RoFeedback(Short feedbackType, String content, String contactMode) {
        this.feedbackType = feedbackType;
        this.content = content;
        this.contactMode = contactMode;
    }
}
