package ${package.Controller};


import org.springframework.web.bind.annotation.RequestMapping;

<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>

import io.swagger.v3.oas.annotations.tags.Tag;
/**
 * ${table.comment!}相关接口
 * @author:${author}
 * @date:${date}
 **/
@Tag(name = "${entity}",description = "${table.comment!}相关接口")
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if cfg.apiVersion?? && cfg.apiVersion !=''>/api/${cfg.apiVersion}</#if><#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
<#if kotlin>
class ${table.controllerName} {
<#else>
public class ${table.controllerName} {
</#if>
}