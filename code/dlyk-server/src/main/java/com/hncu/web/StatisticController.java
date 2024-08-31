package com.hncu.web;

import com.hncu.result.NameValue;
import com.hncu.result.R;
import com.hncu.result.SummaryData;
import com.hncu.service.StatisticService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author caimeisahng
 * @Date 2024/8/17 9:44
 * @Version 1.0
 * 数据统计Controller
 */


@RestController
public class StatisticController {
    @Resource
    private StatisticService statisticService;

    @GetMapping(value = "/api/summary/data")
    public R summaryData(){
        SummaryData summaryData = statisticService.loadSummaryData();
        return R.OK(summaryData);
    }

    /**
     *
     * @return
     * data: [
     *    { value: 20, name: '成交' },
     *    { value: 60, name: '交易' },
     *    { value: 80, name: '客户' },
     *    { value: 100, name: '线索' }
     * ]
     */

    @GetMapping(value = "/api/saleFunnel/data")
    public R SaleFunnelData(){
        List<NameValue> nameValueList = statisticService.loadSaleFunnelData();
        return R.OK(nameValueList);
    }

    /**
     *
     * @return
     * [
     *                   { value: 1048, name: 'Search Engine' },
     *                   { value: 735, name: 'Direct' },
     *                   { value: 580, name: 'Email' },
     *                   { value: 484, name: 'Union Ads' },
     *                   { value: 300, name: 'Video Ads' }
     * ],
     */
    @GetMapping(value = "/api/sourcePie/data")
    public R SourcePieData(){
        List<NameValue> nameValueList = statisticService.loadSourcePieData();
        return R.OK(nameValueList);
    }

}
