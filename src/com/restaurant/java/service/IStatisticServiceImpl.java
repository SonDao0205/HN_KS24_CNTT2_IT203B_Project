package com.restaurant.java.service;

import com.restaurant.java.entity.Menu_Item;

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

    }

    @Override
    public List<Menu_Item> bestSelling() {
        return List.of();
    }
}
