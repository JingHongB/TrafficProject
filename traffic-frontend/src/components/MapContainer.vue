<script setup>
import {onMounted, onUnmounted} from "vue";
import AMapLoader from "@amap/amap-jsapi-loader";
import {get} from "@/net/index.js";

let map = null;

onMounted(() => {
  window._AMapSecurityConfig = {
    securityJsCode: "「f96a19158f9aca263369484056411b7a」",
  };
  AMapLoader.load({
    key: "11ced7e83a18ebe67c3e5347c1e052ba", // 申请好的Web端开发者Key，首次调用 load 时必填
    version: "2.0", // 指定要加载的 JSAPI 的版本，缺省时默认为 1.4.15
    plugins: ["AMap.Scale"], //需要使用的的插件列表，如比例尺'AMap.Scale'，支持添加多个如：['...','...']
  })
      .then((AMap) => {
        map = new AMap.Map("container", {
          // 设置地图容器id
          viewMode: "3D", // 是否为3D地图模式
          zoom: 10, // 初始化地图级别
          center: [104.066541, 30.572269], // 初始化地图中心点位置
        });
        loadLumberyardData(AMap);
      })
      .catch((e) => {
        console.log(e);
      });
});

onUnmounted(() => {
  map?.destroy();
});

function loadLumberyardData(AMap) {
  get("/api/lumberyard", (response) => {
      addMarkers(response, AMap);
  });
}

function addMarkers(lumberyards, AMap) {
  lumberyards.forEach((lumberyard) => {
      new AMap.Marker({
        position: [parseFloat(lumberyard.longitude), parseFloat(lumberyard.latitude)], // 确保经纬度是数字类型
        map: map,
        title: lumberyard.name,
      });
  });
}

</script>

<template>
  <div id="container"></div>
</template>

<style scoped>
#container {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  width: 100%;
  height: 100%;
}

</style>