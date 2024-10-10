<script setup>
import {ref} from 'vue';
import {ElMessage} from 'element-plus';
import {post} from '@/net/index.js';

const city = ref('');
const keyword = ref('');
const pageNum = ref(1);
const locations = ref([]);

const search = () => {
  if (!city.value || !keyword.value) {
    ElMessage.warning('请填写城市和关键词');
    return;
  }

  post('/api/poi/search', {city: city.value, keyword: keyword.value, pageNum: pageNum.value}, (response) => {
    if (response) {
      locations.value = response;
    } else {
      ElMessage.error(`搜索失败: ${response.message}`);
    }
  });
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
        </el-row>
      </el-header>
      <el-main>
        <el-table :data="locations" border style="width: 100%">
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
.el-container {
  height: 100vh;
}

.el-header {
  background-color: #f5f5f5;
  padding: 20px;
}

.el-main {
  padding: 20px;
  overflow: auto;
}
</style>