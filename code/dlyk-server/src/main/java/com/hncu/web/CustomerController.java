package com.hncu.web;

import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageInfo;
import com.hncu.constant.Constants;
import com.hncu.model.TCustomer;
import com.hncu.query.CustomerQuery;
import com.hncu.result.CustomerExcel;
import com.hncu.result.R;
import com.hncu.service.CustomerService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author caimeisahng
 * @Date 2024/8/5 10:51
 * @Version 1.0
 */

@RestController
public class CustomerController {

    @Resource

    private CustomerService customerService;


    @PostMapping(value = "/api/clue/customer")
    public R ConvertCustomer(@RequestBody CustomerQuery customerQuery , @RequestHeader(value = "Authorization") String token){
        customerQuery.setToken(token);
        Boolean convert = customerService.convertCustomer(customerQuery);
        return convert ? R.OK() : R.FAIL();
    }

    /**
     * 客户列表分页查询
     *
     * @param current
     * @return
     */
    @GetMapping(value = "/api/customers")
    public R cluePage(@RequestParam(value = "current", required = false) Integer current) {
        if (current == null) {
            current = 1;
        }

        PageInfo<TCustomer> pageInfo = customerService.getCustomerByPage(current);
        return R.OK(pageInfo);
    }

    /**
     * 导出Excel
     * @param response
     * @throws IOException
     */
    @GetMapping(value = "/api/exportExcel")
    public void exportExcel(HttpServletResponse response ,@RequestParam(value = "ids" ,required = false) String ids) throws IOException {
        //让浏览器弹出下载框，后端需要设置响应头信息
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");

        List<String> idList = StringUtils.hasText(ids) ? Arrays.asList(ids.split(",")) : new ArrayList<>();

        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(Constants.EXCel_FILE_NAME + System.currentTimeMillis(), StandardCharsets.UTF_8) +  ".xlsx");

        List<CustomerExcel> datalist = customerService.getCustomerExcel(idList);

        EasyExcel.write(response.getOutputStream(), CustomerExcel.class)
                .sheet("模板")
                .doWrite(datalist);
    }

}
