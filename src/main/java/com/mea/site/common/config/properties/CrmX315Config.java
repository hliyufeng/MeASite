package com.mea.site.common.config.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by lenovo on 2018/2/26.
 */
@Component
@ConfigurationProperties(prefix = "glad.config")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrmX315Config {

    private  String webViewPrefix = "/templates/";

    private  String webViewSuffix = ".html";

}
