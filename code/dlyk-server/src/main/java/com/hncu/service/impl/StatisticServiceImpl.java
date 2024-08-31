package com.hncu.service.impl;

import com.hncu.manager.StatisticManager;
import com.hncu.result.NameValue;
import com.hncu.result.SummaryData;
import com.hncu.service.StatisticService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author caimeisahng
 * @Date 2024/8/17 9:47
 * @Version 1.0
 */

@Service
public class StatisticServiceImpl implements StatisticService {

    @Resource
    private StatisticManager statisticManager;

    @Override
    public SummaryData loadSummaryData() {
        return statisticManager.loadSummaryData();
    }

    @Override
    public List<NameValue> loadSaleFunnelData() {
        return statisticManager.loadSaleFunnelData();
    }

    @Override
    public List<NameValue> loadSourcePieData() {
        return statisticManager.loadSourcePieData();
    }
}
