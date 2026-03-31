package com.restaurant.java.service;

import com.restaurant.java.dao.StatisticDao;
import com.restaurant.java.entity.Menu_Item;

import java.time.LocalDateTime;
import java.util.List;

public class IStatisticServiceImpl implements IStatisticService {
    private static IStatisticServiceImpl instance;
    private IStatisticServiceImpl() {}
    public static IStatisticServiceImpl getInstance() {
        if (instance == null) {
            instance = new IStatisticServiceImpl();
        }
        return instance;
    }

    @Override
    public void revenueByMonth() {
        System.out.printf("Doanh thu của tháng %d : %.2f\n", LocalDateTime.now().getMonthValue(), StatisticDao.getInstance().revenueByMonth(LocalDateTime.now().getMonthValue(),LocalDateTime.now().getYear()));
    }

    @Override
    public void bestSelling() {
        StatisticDao.getInstance().bestSeller();
    }
}
