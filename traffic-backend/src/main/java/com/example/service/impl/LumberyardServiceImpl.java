package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.LumberyardMapper;
import com.example.model.entity.Lumberyard;
import com.example.service.LumberyardService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LumberyardServiceImpl extends ServiceImpl<LumberyardMapper, Lumberyard> implements LumberyardService {
    /**
     * 获取所有伐木场信息。
     *
     * @return 伐木场列表
     */
    @Override
    public List<Lumberyard> getAllLumberyards() {
        return list();
    }
}