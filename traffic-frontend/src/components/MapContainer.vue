<script setup>
import {onMounted, onUnmounted, ref} from "vue";
import AMapLoader from "@amap/amap-jsapi-loader";
import {get, post} from "@/net/index.js";
import {ElMessage} from "element-plus";

let map = null;
const goodsList = ref([]);
const taskList = ref([]);
let vehicleMarkers = [];
let routePolylines = [];


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

//销毁地图
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
  post("/api/vehicle/init", null, (response) => {
    if (response) {
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
        title: `${vehicle.id}`,
        icon: new AMap.Icon({
          image: "./src/assets/vehicle.png",
          size: new AMap.Size(30, 30),
          imageSize: new AMap.Size(30, 30),
        }),
        offset: new AMap.Pixel(-15, -30),
      });
      vehicleMarkers.push(marker); // 保存车辆标记到数组中
    } else {
      ElMessage.error(`车辆 ${vehicle.name} 的经纬度信息不完整`);
    }
  });
}

//创建货物
const createGoods = () => {
  post('/api/transport/goods/create', null, () => {
        ElMessage.success('货物创建成功');
      }
  );
};

//获取货物列表
const getGoodsList = () => {
  get('/api/transport/goods', (data) => {
        goodsList.value = data;
        ElMessage.success('成功获取货物列表');
      }
  );
};

//创建任务
const createTask = () => {
  post('/api/transport/task/create', null, () => {
        ElMessage.success('委托创建成功');
      }
  );
};

//获取任务列表
const getTaskList = () => {
  get('/api/transport/task', (data) => {
        taskList.value = data;
        ElMessage.success('成功获取委托列表');
      }
  );
};

// 分配委托
function assignTask() {
  post('/api/transport/assign', null, () => {
    ElMessage.success('委托分配成功');
  });
}

// 获取路径数据并展示在地图上
function getPath() {
  get('/api/transport/getPath', (response) => {
    if (response) {
      // 假设返回的路径数据是JSON格式，并且包含路径坐标点
      const pathData = JSON.parse(response);
      displayPathOnMap(pathData);
    } else {
      ElMessage.error("获取路径数据失败");
    }
  });
}
function displayPathOnMap(pathData) {
  try {
    // 假设pathData包含路径坐标点
    const path = pathData.route.paths[0].steps.map(step => {
      return step.polyline.split(';').map(coord => {
        const [lng, lat] = coord.split(',');
        return new AMap.LngLat(lng, lat);
      });
    }).flat();

    // 检查是否存在之前的路线覆盖物，如果有则移除
    routePolylines.forEach(polyline => {
      map.remove(polyline);
    });

    // 创建新的路线覆盖物并添加到地图上
    const polyline = new AMap.Polyline({
      path: path,
      strokeColor: "#3366FF",
      strokeWeight: 5,
      strokeOpacity: 0.9
    });
    map.add(polyline);
    routePolylines.push(polyline);

    // 调整地图视野以适应路径
    map.setFitView(polyline);
  } catch (error) {
    console.error('Failed to display path on map:', error);
    ElMessage.error("展示路径失败");
  }
}

</script>

<template>
  <div id="container">
    <div class="button-container">
      <el-button type="primary" @click="initializeVehicles">初始化车辆</el-button>
      <el-button type="primary" @click="createGoods">创建货物</el-button>
      <el-button type="success" @click="getGoodsList">获取货物列表</el-button>
      <el-button type="primary" @click="createTask">创建委托</el-button>
      <el-button type="success" @click="getTaskList">获取委托列表</el-button>
      <el-button type="success" @click="assignTask">分配委托</el-button>
      <el-button type="success" @click="getPath">展示行驶路线</el-button>
    </div>
    <div>
      <el-container class="fixed-sidebar">
        <el-main>
          <!-- 货物列表展示 -->
          <el-card v-if="goodsList.length" class="box-card" header="货物列表">
            <el-table :data="goodsList" style="width: 100%">
              <el-table-column prop="id" label="ID" width="50"></el-table-column>
              <el-table-column prop="type" label="类型" width="150"></el-table-column>
              <el-table-column prop="startPoint" label="起点" width="150"></el-table-column>
              <el-table-column prop="endPoint" label="终点" width="150"></el-table-column>
              <el-table-column prop="status" label="状态" width="100"></el-table-column>
            </el-table>
          </el-card>
          <!-- 委托列表展示 -->
          <el-card v-if="taskList.length" class="box-card" header="委托列表">
            <el-table :data="taskList" style="width: 100%">
              <el-table-column prop="id" label="ID" width="50"></el-table-column>
              <el-table-column prop="goodsId" label="货物ID" width="100"></el-table-column>
              <el-table-column prop="vehicleId" label="车辆ID" width="100"></el-table-column>
              <el-table-column prop="startPoint" label="起始地点" width="150"></el-table-column>
              <el-table-column prop="endPoint" label="目的地" width="150"></el-table-column>
              <el-table-column prop="status" label="状态" width="100"></el-table-column>
            </el-table>
          </el-card>
        </el-main>
      </el-container>
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

.fixed-sidebar {
  position: fixed; /* 使其固定位置 */
  top: 20px; /* 距离页面顶部的距离，可以根据需要调整 */
  right: 20px; /* 距离页面右侧的距离 */
  width: 350px; /* 侧边栏的宽度 */
  max-height: 90vh; /* 最大高度为视口高度的 90% */
  overflow-y: auto; /* 当内容超出高度时，滚动显示 */
  background: #fff; /* 背景颜色 */
  padding: 10px; /* 内边距 */
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1); /* 添加阴影效果 */
  z-index: 1000; /* 确保侧边栏在最上层 */
}

</style>