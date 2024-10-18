<script setup>
import {ref} from 'vue';
import {ElMessage} from 'element-plus';
import {get, post} from '@/net/index.js';

const city = ref('');
const keyword = ref('');
const pageNum = ref(1);
const locations = ref([]);

const search = () => {
  if (!city.value || !keyword.value) {
    ElMessage.warning('请填写城市和关键词');
    return;
  }

  get(`/api/poi/search?city=${city.value}&keyword=${keyword.value}&pageNum=${pageNum.value}`, (response) => {
    if (response) {
      locations.value = response;
    } else {
      ElMessage.error(`搜索失败: ${response.message}`);
    }
  });
};

const exportTxt = () => {
  if (locations.value.length === 0) {
    ElMessage.warning('没有数据可导出');
    return;
  }
  let content = 'id,type_id,name,address,longitude,latitude\n';
  locations.value.forEach(location => {
    content += `${location.id},${location.typeId},${location.name},${location.address},${location.longitude},${location.latitude}\n`;
  });
  const blob = new Blob([content], {type: 'text/plain;charset=utf-8'});
  const url = window.URL.createObjectURL(blob);
  const link = document.createElement('a');
  link.href = url;
  link.setAttribute('download', 'exported_data.txt');
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
};
</script>

<template>
  <div>
    <el-container>
      <el-header>
        <el-row :gutter="20">
          <el-col :span="6">
            <el-input v-model="city" placeholder="请输入城市"></el-input>
          </el-col>
          <el-col :span="6">
            <el-input v-model="keyword" placeholder="请输入关键词"></el-input>
          </el-col>
          <el-col :span="4">
            <el-input-number v-model="pageNum" :min="1" label="页码"></el-input-number>
          </el-col>
          <el-col :span="4">
            <el-button type="primary" @click="search">搜索</el-button>
          </el-col>
          <el-col :span="4">
            <el-button type="success" @click="exportTxt">导出为TXT</el-button>
          </el-col>
        </el-row>
      </el-header>
      <el-main>
        <el-table :data="locations" border style="width: 100%">
          <el-table-column prop="id" label="ID" width="280"></el-table-column>
          <el-table-column prop="name" label="名称" width="280"></el-table-column>
          <el-table-column prop="address" label="地址" width="350"></el-table-column>
          <el-table-column prop="longitude" label="经度" width="150"></el-table-column>
          <el-table-column prop="latitude" label="纬度" width="100"></el-table-column>
        </el-table>
      </el-main>
    </el-container>
  </div>
</template>

<style scoped>
</style>