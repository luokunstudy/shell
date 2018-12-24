package com.lk.shell.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties
@Component
public class ProjectUrlConfig {
    public class ProiectUrlConfig{
        public String wechatMpAuthorize;

        public String wechatOpenAuthorize;

        public String sell;
        public String openid;

        public String getWechatMpAuthorize() {
            return wechatMpAuthorize;
        }

        public void setWechatMpAuthorize(String wechatMpAuthorize) {
            this.wechatMpAuthorize = wechatMpAuthorize;
        }

        public String getWechatOpenAuthorize() {
            return wechatOpenAuthorize;
        }

        public void setWechatOpenAuthorize(String wechatOpenAuthorize) {
            this.wechatOpenAuthorize = wechatOpenAuthorize;
        }

        public String getSell() {
            return sell;
        }

        public void setSell(String sell) {
            this.sell = sell;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }
    }
}
