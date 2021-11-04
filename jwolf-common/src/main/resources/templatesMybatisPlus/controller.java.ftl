package ${package.Controller};

<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>

import com.jwolf.common.base.entity.BasePageSearch;
import com.jwolf.common.base.entity.ResultEntity;

import com.jwolf.service.${package.ModuleName}.api.entity.${entity};
import com.jwolf.service.${package.ModuleName}.service.I${entity}Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
/**
 * ${table.comment!} 前端控制器
 *
 * @author ${author}
 * @since ${date}
 */
<#if restControllerStyle>
@Api(tags = {"${table.comment!}"})
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
    <#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
    <#else>
public class ${table.controllerName} {
    </#if>

    @Autowired
    private ${table.serviceName} ${(table.serviceName?substring(1))?uncap_first};

    @ApiOperation(value = "分页查询")
    @GetMapping("/page")
    public ResultEntity<Page<${entity}>> getPageList(BasePageSearch search){
        return ResultEntity.success(${(table.serviceName?substring(1))?uncap_first}.page(search.getPage()));
    }


    @ApiOperation(value = "根据id查询")
    @GetMapping("/detail}")
    public ResultEntity<${entity}> getById(Long id){
       return ResultEntity.success(${(table.serviceName?substring(1))?uncap_first}.getById(id));
    }


    @ApiOperation(value = "新增")
    @PostMapping("/add")
    public ResultEntity add(@RequestBody ${entity} ${entity?uncap_first}){
        boolean isOK = ${(table.serviceName?substring(1))?uncap_first}.save(${entity?uncap_first});
        return isOK?ResultEntity.success():ResultEntity.fail("新增失败,请重试");
    }


    @ApiOperation(value = "批量删除")
    @DeleteMapping("/del")
    public ResultEntity delete(@RequestParam("ids") List<String> ids){
        boolean isOK = ${(table.serviceName?substring(1))?uncap_first}.removeByIds(ids);
        return isOK?ResultEntity.success():ResultEntity.fail("删除失败,请重试");
    }


    @ApiOperation(value = "更新")
    @PutMapping("/update")
    public ResultEntity update(@RequestBody ${entity} ${entity?uncap_first}){
        boolean isOK = ${(table.serviceName?substring(1))?uncap_first}.updateById(${entity?uncap_first});
        return isOK?ResultEntity.success():ResultEntity.fail("更新失败,请重试");
     }

}
</#if>