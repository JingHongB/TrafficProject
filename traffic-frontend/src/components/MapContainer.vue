<script setup>
import {onMounted, onUnmounted, ref} from "vue";
import AMapLoader from "@amap/amap-jsapi-loader";
import {get} from "@/net/index.js";
import {ElMessage} from "element-plus";

let map = null;
const vehicles = ref([]);
let vehicleMarkers = [];

//初始化地图
onMounted(() => {
  window._AMapSecurityConfig = {
    securityJsCode: "f96a19158f9aca263369484056411b7a",//安全密匙
  };
  AMapLoader.load({
    key: "11ced7e83a18ebe67c3e5347c1e052ba", // Web端开发者Key
    version: "2.0", // 指定要加载的 JSAPI 的版本
    plugins: ["AMap.Scale", "AMap.Driving", "AMap.MoveAnimation"], //需要使用的的插件列表，如比例尺'AMap.Scale'，支持添加多个如：['...','...']
  })
      .then((AMap) => {
        map = new AMap.Map("container", {
          // 设置地图容器id
          viewMode: "3D", // 是否为3D地图模式
          zoom: 7, // 初始化地图级别
          center: [104.066541, 30.572269], // 初始化地图中心点位置
        });
        loadPoiData(AMap);
      })
      .catch((e) => {
        console.log(e);
      });
});

onUnmounted(() => {
  map?.destroy();
});

// 向后端请求poi数据
function loadPoiData(AMap) {
  get("/api/poi/get", (response) => {
    if (response) {
      addMarkers(response.lumberyard, AMap, "./src/assets/lumberyard.png", "lumberyard");
      addMarkers(response.furnitureFactory, AMap, "./src/assets/furnitureFactory.png", "furnitureFactory");
    } else {
      ElMessage.error("获取到的数据为空");
    }
  });
}

//绘制poi点
function addMarkers(poiList, AMap, iconUrl, type) {
  const icon = new AMap.Icon({
    image: iconUrl,
    size: new AMap.Size(30, 30), // 设置图标大小（宽度和高度，单位为像素）
    imageSize: new AMap.Size(30, 30) // 设置图标的实际显示大小
  });
  poiList.forEach((poi) => {
    if (poi.longitude && poi.latitude) {
      // 创建标记
      const marker = new AMap.Marker({
        position: [parseFloat(poi.longitude), parseFloat(poi.latitude)],
        map: map,
        title: poi.name,
        icon: icon,
        offset: new AMap.Pixel(-15, -30), // 调整图标偏移量
      });
      // 确定类型名称
      let typeName;
      switch (type) {
        case "lumberyard":
          typeName = "伐木场";
          break;
        case "furnitureFactory":
          typeName = "家具厂";
          break;
      }
      // 为每个标记添加点击事件，显示信息窗口
      marker.on("click", () => {
        const infoWindow = new AMap.InfoWindow({
          //信息窗口显示内容
          content: `
            <div>
              <h3>${poi.name}</h3>
              <p>地址: ${poi.address}</p>
              <p>类型: ${typeName}</p>
            </div>
          `,
          offset: new AMap.Pixel(0, -30), // 调整信息窗口的偏移量
        });
        infoWindow.open(map, marker.getPosition());
      });
    } else {
      ElMessage.error(`POI ${poi.name} 的经纬度信息不完整`);
    }
  });
}

//初始化车辆
function initializeVehicles() {
  get("/api/vehicle/init", (response) => {
    if (response) {
      vehicles.value = response;
      addVehicleMarkers(response);
      ElMessage.success("车辆初始化成功");
    } else {
      ElMessage.error("车辆初始化失败");
    }
  });
}

//绘制车辆点
function addVehicleMarkers(vehicleList) {
  // 清除之前的车辆标记
  vehicleMarkers.forEach((marker) => marker.setMap(null));
  vehicleMarkers = [];

  // 添加新的车辆标记
  vehicleList.forEach((vehicle) => {
    if (vehicle.longitude && vehicle.latitude) {
      const marker = new AMap.Marker({
        position: [parseFloat(vehicle.longitude), parseFloat(vehicle.latitude)], // 确保经纬度是数字类型
        map: map,
        title: vehicle.name,
        icon: new AMap.Icon({
          image: "./src/assets/vehicle.png",
          size: new AMap.Size(30, 30),
          imageSize: new AMap.Size(30, 30),
        }),
        offset: new AMap.Pixel(-15, -30),
      });
      vehicleMarkers.push(marker); // 保存车辆标记到数组中
      //moveVehicleToTarget(marker, [104.066541, 30.572269], AMap);
    } else {
      ElMessage.error(`车辆 ${vehicle.name} 的经纬度信息不完整`);
    }
  });
}

//TODO 车辆的移动
function moveVehicleToTarget(marker, targetPosition, AMap) {
  // 创建驾车路径规划对象
  const driving = new AMap.Driving({
    map: null,
  });

  // 设置路径规划的起点和终点
  driving.search(
      marker.getPosition(), // 起点
      targetPosition, // 终点
      (status, result) => {
        if (status === 'complete' && result.routes && result.routes.length) {
          // 获取路径规划的路径点
          const path = [];
          result.routes[0].steps.forEach(step => {
            path.push(...step.path); // 将每个步骤的路径点推入到 path 数组中
          });

          // 验证路径点的有效性
          const validPath = path.filter(point => {
            return point && !isNaN(point.lng) && !isNaN(point.lat);
          });

          // 确保路径点是高德支持的格式：经纬度坐标数组
          if (validPath.length > 0) {
            console.log('规划的路径点:', validPath);
            // 使用 AMap 的 moveAlong 方法让标记沿着路径移动
            marker.moveAlong(validPath, 100); // 每 100ms 移动一次
          } else {
            console.error('路径规划点为空或无效，无法移动车辆');
          }
        } else {
          console.error('路径规划失败：', result);
        }
      }
  );
}

</script>

<template>
  <div id="container">
    <div class="button-container">
      <el-button type="primary" @click="initializeVehicles">初始化车辆</el-button>
    </div>
  </div>
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

.button-container {
  position: fixed;
  top: 20px;
  left: 20px;
  z-index: 1000;
  display: flex;
  flex-direction: column;
  gap: 10px;
}
</style>