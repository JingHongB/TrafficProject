package com.example.mapper;

import com.example.model.vo.PathVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PathMapper {

    /**
     * 获得路径起点经纬度
     * @return
     */
    @Select("select longitude,latitude from vehicle where id = 1;")
    PathVO getOrigin();

    /**
     * 获得路径终点经纬度
     * @return
     */
    @Select("select longitude,latitude from lumberyard where id = 1;")
    PathVO getDestination();
}
