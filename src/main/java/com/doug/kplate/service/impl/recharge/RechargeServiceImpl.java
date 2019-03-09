package com.doug.kplate.service.impl.recharge;

import com.doug.kplate.dao.recharge.RechargeDao;
import com.doug.kplate.dto.recharge.RechargeDto;
import com.doug.kplate.service.recharge.RechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RechargeServiceImpl implements RechargeService {
    @Autowired
    private RechargeDao rechargeDao;
    @Override
    public List<RechargeDto> queryList(Map<String, Object> map) {
        return this.rechargeDao.selectList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return this.rechargeDao.selectTotal(map);
    }
}
