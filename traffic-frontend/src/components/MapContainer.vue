<script setup>
import {ref, onMounted, onUnmounted} from "vue";
import AMapLoader from "@amap/amap-jsapi-loader";
import {get, post} from "@/net/index.js";
import {ElMessage, ElButton, ElTable, ElTableColumn, ElTabs, ElTabPane, ElDivider, ElEmpty} from 'element-plus'

let map = null;
const goodsList = ref([]);
const taskList = ref([]);
const carList = ref([]);
const poiList = ref([]);
let CarMarkers = [];
let PoiMarkers = [];
const selectedPoiTypes = ref([]);  // 筛选的POI类型数组
const poiTypes = ["林场", "加工厂", "制造厂", "家具城", "铁矿", "锻造厂", "板材厂", "五金店"]; // POI 类型列表


const showLeftPanel = ref(true)
const showRightPanel = ref(true)

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
        map = new AMap.Map("map-container", {
          // 设置地图容器id
          viewMode: "3D", // 是否为3D地图模式
          zoom: 7, // 初始化地图级别
          center: [104.066541, 30.572269], // 初始化地图中心点位置
        });
        loadPoiData(AMap);
        loadCarData();
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
  get("/api/poi/get", (data) => {
    if (data) {
      poiList.value = data;
      addMarkers(poiList.value, AMap);
    } else {
      ElMessage.error("获取到的数据为空");
    }
  });
}

//绘制poi点
function addMarkers(poiList, AMap) {
  // 清除之前的 POI 标记
  PoiMarkers.forEach((marker) => marker.setMap(null));
  PoiMarkers = [];

  poiList.forEach((poi) => {
    if (poi.longitude && poi.latitude) {
      // 创建标记
      const marker = new AMap.Marker({
        position: [parseFloat(poi.longitude), parseFloat(poi.latitude)],
        map: map,
        title: poi.name,
        icon: new AMap.Icon({
          image: getIconUrl(poi.typeName), // 动态获取不同类型的图标
          size: new AMap.Size(30, 30), // 设置图标大小（宽度和高度，单位为像素）
          imageSize: new AMap.Size(30, 30), // 设置图标的实际显示大小
        }),
        offset: new AMap.Pixel(-15, -30), // 调整图标偏移量
      });

      // 为每个标记添加点击事件，显示信息窗口
      marker.on("click", () => {
        const infoWindow = new AMap.InfoWindow({
          //信息窗口显示内容
          content: `
            <div>
              <h3>地点信息</h3>
              <p>名称: ${poi.name}</p>
              <p>地址: ${poi.address}</p>
              <p>类型: ${poi.typeName}</p>
              <p>状态: ${poi.status}</p>
            </div>
          `,
          offset: new AMap.Pixel(0, -30), // 调整信息窗口的偏移量
        });
        infoWindow.open(map, marker.getPosition());
      });
      PoiMarkers.push(marker); // 保存标记
    } else {
      ElMessage.error(`POI ${poi.name} 的经纬度信息不完整`);
    }
  });
}

// 根据typeName获取不同类型的图标
function getIconUrl(typeName) {
  switch (typeName) {
    case "林场":
      return "./src/assets/lumberyard.png";
    case "加工厂":
      return "./src/assets/furnitureFactory.png";
    case "制造厂":
      return "./src/assets/woodManufacturingPlant.png";
    case "家具城":
      return "./src/assets/furnitureCity.png";
    case "铁矿":
      return "./src/assets/steel.png";
    case "锻造厂":
      return "./src/assets/forgeFactory.png";
    case "板材厂":
      return "./src/assets/plateFactory.png";
    case "五金店":
      return "./src/assets/hardwareStores.png";
    default:
      return "./src/assets/defaultIcon.png"; // 默认图标
  }
}

//刷新车辆
function initializeCars() {
  post("/api/car/init", null, () => {
    ElMessage.success("车辆初始化成功");
  });
}

// 向后端请求车辆数据
function loadCarData() {
  get("/api/car/get", (data) => {
    data.forEach((car, index) => {
      car.shortId = index + 1;
    });
    carList.value = data;
    addVehicleMarkers(data);
  });
}

//绘制车辆点
function addVehicleMarkers(CarList) {
  // 清除之前的车辆标记
  CarMarkers.forEach((marker) => marker.setMap(null));
  CarMarkers = [];

  // 添加新的车辆标记
  CarList.forEach((car) => {
    if (car.longitude && car.latitude) {
      const marker = new AMap.Marker({
        position: [parseFloat(car.longitude), parseFloat(car.latitude)], // 确保经纬度是数字类型
        map: map,
        title: `${car.id}`,
        icon: new AMap.Icon({
          image: "./src/assets/car.png",
          size: new AMap.Size(30, 30),
          imageSize: new AMap.Size(30, 30),
        }),
        offset: new AMap.Pixel(-15, -30),
      });
      marker.on("click", () => {
        const infoWindow = new AMap.InfoWindow({
          //信息窗口显示内容
          content: `
            <div>
              <h3>车辆信息</h3>
              <p>ID: ${car.shortId}</p>
              <p>类型: ${car.typeName}</p>
              <p>载重: ${car.loadCapacity}吨</p>
              <p>状态: ${car.status}</p>
            </div>
          `,
          offset: new AMap.Pixel(0, -30), // 调整信息窗口的偏移量
        });
        infoWindow.open(map, marker.getPosition());
      });

      CarMarkers.push(marker); // 保存车辆标记到数组中
    } else {
      ElMessage.error(`车辆 ${car.name} 的经纬度信息不完整`);
    }
  });
}

//创建货物
const createGoods = () => {
  post('/api/transport/goods/create', null, () => {
    ElMessage.success('货物创建成功');
    getGoodsList(); // 创建成功后刷新货物列表
  });
};

//获取货物列表
const getGoodsList = () => {
  get('/api/transport/goods', (data) => {
    data.forEach((goods, index) => {
      goods.shortId = index + 1;
    });
    goodsList.value = data;
    ElMessage.success('成功获取货物列表');
  });
};

//创建任务
const createTask = () => {
  post('/api/transport/task/create', null, () => {
    ElMessage.success('委托创建成功');
    getTaskList(); // 创建成功后刷新委托列表
  });
};

//获取任务列表
const getTaskList = () => {
  get('/api/transport/task', (data) => {
    data.forEach((task, index) => {
      task.shortId = index + 1;
    });
    data.forEach((task) => {
      carList.value.forEach((car) => {
        if (task.carId === car.id) {
          task.shortCarId = car.shortId;
        }
      });
    });
    taskList.value = data;
    ElMessage.success('成功获取委托列表');
  });
};

// 分配委托
function assignTask() {
  post('/api/transport/assign', null, () => {
    ElMessage.success('委托分配成功');
    getTaskList(); // 分配成功后刷新委托列表
  });
}

// 选择不同类型的 POI 点进行展示
function filterPoiMarkers() {
  const filteredPois = poiList.value.filter((poi) => {
    return selectedPoiTypes.value.length === 0 || selectedPoiTypes.value.includes(poi.typeName);
  });
  addMarkers(filteredPois, AMap);
}

//初始化数据
const resetData = () => {
  post('/api/data/reset', null, () => {
    ElMessage.success('数据初始化成功');
  }, () => {
    ElMessage.error('数据初始化失败');
  });
};

</script>

<template>
  <div id="container">
    <!-- 地图容器 -->
    <div id="map-container"></div>

    <!-- 左侧功能面板 -->
    <div class="left-panel" :class="{ 'collapsed': !showLeftPanel }">
      <el-button
          class="panel-toggle left-toggle"
          type="primary"
          :icon="showLeftPanel ? 'el-icon-arrow-left' : 'el-icon-arrow-right'"
          circle
          @click="showLeftPanel = !showLeftPanel"
      ></el-button>
      <div v-show="showLeftPanel" class="panel-content">
        <h2 class="text-xl font-bold mb-4">功能操作</h2>
        <el-divider></el-divider>
        <div class="button-container">
          <el-button type="primary" @click="createGoods">创建货物</el-button>
          <el-button type="success" @click="getGoodsList">获取货物列表</el-button>
          <el-button type="primary" @click="createTask">创建委托</el-button>
          <el-button type="success" @click="getTaskList">获取委托列表</el-button>
          <el-button type="warning" @click="assignTask">分配委托</el-button>
          <el-button type="info" @click="initializeCars">初始化车辆</el-button>
          <el-button type="primary" @click="resetData">数据初始化</el-button>
        </div>
        <el-select v-model="selectedPoiTypes" multiple placeholder="请选择POI类型" @change="filterPoiMarkers">
          <el-option v-for="type in poiTypes" :key="type" :label="type" :value="type"></el-option>
        </el-select>
      </div>
    </div>

    <!-- 右侧数据显示面板 -->
    <div class="right-panel" :class="{ 'collapsed': !showRightPanel }">
      <el-button
          class="panel-toggle right-toggle"
          type="primary"
          :icon="showRightPanel ? 'el-icon-arrow-right' : 'el-icon-arrow-left'"
          circle
          @click="showRightPanel = !showRightPanel"
      ></el-button>
      <div v-show="showRightPanel" class="panel-content">
        <el-tabs>
          <el-tab-pane label="货物列表">
            <el-table v-if="goodsList.length" :data="goodsList" style="width: 100%">
              <el-table-column prop="shortId" label="ID" width="50"></el-table-column>
              <el-table-column prop="type" label="类型" width="150"></el-table-column>
              <el-table-column prop="owner" label="货主" width="150"></el-table-column>
              <el-table-column prop="weight" label="重量" width="150"></el-table-column>
              <el-table-column prop="status" label="状态" width="100"></el-table-column>
            </el-table>
            <el-empty v-else description="暂无货物数据"></el-empty>
          </el-tab-pane>
          <el-tab-pane label="委托列表">
            <el-table v-if="taskList.length" :data="taskList" style="width: 100%">
              <el-table-column prop="shortId" label="ID" width="50"></el-table-column>
              <el-table-column prop="goods" label="货物" width="100"></el-table-column>
              <el-table-column
                  prop="CarId"
                  label="车辆ID"
                  width="100"
                  :formatter="(row) => row.shortCarId ? row.shortCarId : '暂无'"
              ></el-table-column>
              <el-table-column prop="startPoint" label="起点" width="150"></el-table-column>
              <el-table-column prop="endPoint" label="终点" width="150"></el-table-column>
              <el-table-column
                  prop="distance"
                  label="距离"
                  width="150"
                  :formatter="(row) => row.distance ? `${row.distance} km` : '0 km'"
              ></el-table-column>
              <el-table-column prop="status" label="状态" width="100"></el-table-column>
            </el-table>
            <el-empty v-else description="暂无委托数据"></el-empty>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
  </div>
</template>

<style scoped>
#container {
  position: relative;
  width: 100%;
  height: 100vh;
  display: flex;
}

#map-container {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 1;
}

.left-panel, .right-panel {
  position: absolute;
  top: 0;
  height: 100%;
  z-index: 2;
  background-color: rgba(255, 255, 255, 0.9);
  transition: width 0.3s ease; /* 只设置宽度的过渡 */
  min-width: 60px; /* 设置最小宽度 */
}

.left-panel {
  left: 0;
  width: 250px;
}

.right-panel {
  right: 0;
  width: 300px;
}


.panel-content {
  padding: 20px;
  height: 100%;
  overflow-y: auto;
}

.panel-toggle {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  z-index: 3;
}

.left-toggle {
  right: -20px;
}

.right-toggle {
  left: -20px;
}

.collapsed {
  width: 0;
  padding: 0;
  overflow: hidden;
}

.button-container {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

</style>