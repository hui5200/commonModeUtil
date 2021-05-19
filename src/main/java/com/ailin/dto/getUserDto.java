package com.ailin.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@ApiModel(value = "DemoEditDto", description = "保存对象")
public class getUserDto {

    @ApiModelProperty(value = "授权", example = "token", required = true)
    public String token;

    @ApiModelProperty(value = "姓名", example = "小张", required = true)
    public String name;

    @ApiModelProperty(value = "性别", example = "1", required = true)
    public Integer sex;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }
}
