package com.hncu.service;

import com.hncu.result.NameValue;
import com.hncu.result.SummaryData;

import java.util.List;

/**
 * @Author caimeisahng
 * @Date 2024/8/17 9:47
 * @Version 1.0
 */
public interface StatisticService {
    SummaryData loadSummaryData();

    List<NameValue> loadSaleFunnelData();

    List<NameValue> loadSourcePieData();
}
