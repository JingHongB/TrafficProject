package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.dto.PoiDTO;
import com.example.model.entity.Poi;
import com.example.model.vo.PoiSearchVO;
import com.example.model.vo.PoiVO;

import java.util.List;

public interface PoiService extends IService<Poi> {
    /**
     * 根据城市、关键词和页数，通过高德API进行搜索。
     *
     * @param city    搜索的城市
     * @param keyword 搜索的关键词
     * @param pageNum 页数
     * @return 搜索结果
     */
    List<Poi> searchPoi(String city, String keyword, int pageNum);

    /**
     * 将Poi转换为PoiSearchVO
     *
     * @param poiList poi列表
     * @return PoiSearchVO列表
     */
    List<PoiSearchVO> PoiConvertToPoiSearchVO(List<Poi> poiList);

    /**
     * 将Poi转换为PoiVO
     *
     * @param poiList poi列表
     * @return PoiVO列表
     */
    List<PoiVO> PoiConvertToPoiVO(List<Poi> poiList);

    /**
     * 重置所有工厂状态
     */
    void resetAllFactoryStatus();


    /**
     * 新增一个Poi
     *
     * @param poiDTO poiDTO
     */
    void addPoi(PoiDTO poiDTO);


    /**
     * 更新一个Poi
     *
     * @param poiDTO poiDTO
     */
    void updatePoi(PoiDTO poiDTO);
}
