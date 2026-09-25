package com.bytebites.controller;

import java.util.List;

import com.bytebites.dao.DietitianDao;
import com.bytebites.model.Dietitian;

public class DietitianController {

    private DietitianDao dietitianDao =
            new DietitianDao();

    public List<Dietitian> getApprovedDietitians() {

        return dietitianDao.getApprovedDietitians();
    }
}
