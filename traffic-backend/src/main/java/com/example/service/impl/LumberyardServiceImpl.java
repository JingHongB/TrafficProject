package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.LumberyardMapper;
import com.example.model.BaseLocationEntity;
import com.example.model.entity.Lumberyard;
import com.example.service.LumberyardService;
import com.example.utils.PoiUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LumberyardServiceImpl extends ServiceImpl<LumberyardMapper, Lumberyard> implements LumberyardService {
    /**
     * 获取所有伐木场信息，并转换为 BaseLocationEntity 类型。
     *
     * @return 伐木场列表
     */
    @Override
    public List<BaseLocationEntity> getAllLumberyards() {
        List<Lumberyard> lumberyards = list();
        return PoiUtils.PoiConvert(lumberyards, BaseLocationEntity.class);
    }
}