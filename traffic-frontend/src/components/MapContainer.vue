<script setup>
import {ref, onMounted, onUnmounted} from "vue";
import AMapLoader from "@amap/amap-jsapi-loader";
import {get, post} from "@/net/index.js";
import {ElMessage, ElButton, ElDivider} from 'element-plus';
import {useRouter} from "vue-router"

let map = null;
const taskList = ref([]);
const carList = ref([]);
const poiList = ref([]);
let CarMarkers = [];
let PoiMarkers = [];
const selectedPoiTypes = ref([]);  // 筛选的POI类型数组
const poiTypes = ["林场", "加工厂", "制造厂", "家具城", "铁矿", "锻造厂", "板材厂", "五金店"]; // POI 类型列表


const showLeftPanel = ref(true)

const router = useRouter()  // 用于路由跳转

const selectedCarId = ref(null); // 用户选择的车辆 ID
const selectedPoiId = ref(null); // 用户选择的 POI ID

//绘制车辆点
function addVehicleMarkers(CarList) {
  // 清除之前的车辆标记
  CarMarkers.forEach((marker) => marker.setMap(null));
  CarMarkers = [];

  // 添加新的车辆标记
  CarList.forEach((car) => {
    if (car.longitude && car.latitude) {
      const marker = new AMap.Marker({
        //position: [parseFloat(car.longitude), parseFloat(car.latitude)], // 确保经纬度是数字类型
        position: [parseFloat(car.longitude), parseFloat(car.latitude)],
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

      CarMarkers.push({id: car.id, marker}); // 保存车辆标记到数组中
    } else {
      ElMessage.error(`车辆 ${car.name} 的经纬度信息不完整`);
    }
  });
}

let taskInterval = null; // 用于存储定时任务的引用
const isTaskRunning = ref(false); // 标记定时任务是否正在运行
// 向后端请求要进行路径规划的委托列表
function fetchTaskList() {
  get("/api/task/getUnplanned", (data) => {
    if (data) {
      taskList.value = data;
      data.forEach((task) => {
        if (task.status === '待取货') {
          moveCarToPoi(task.carId, task.startPoiId, task.id, '待运货');
        } else if (task.status === '待运货') {
          moveCarToPoi(task.carId, task.endPoiId, task.id, '已完成');
        }
      });
    } else {
      ElMessage.error("获取委托列表数据失败");
    }
  });
}

// 路径规划结束后更新委托状态
function updateTaskStatus(taskId) {
  post(`/api/task/updateStatus?taskId=${taskId}`, null, () => {
    // 成功后的回调
  });
}

// 启动定时任务
function startTaskInterval() {
  if (isTaskRunning.value) {
    ElMessage.warning("定时任务已经在运行中！");
    return;
  }
  isTaskRunning.value = true;
  // 立即请求一次数据
  fetchTaskList();
  // 设置定时任务
  taskInterval = setInterval(fetchTaskList, 5000);
  ElMessage.success("定时任务已启动！");
}

// 停止定时任务
function stopTaskInterval() {
  if (!isTaskRunning.value) {
    ElMessage.warning("定时任务未启动！");
    return;
  }
  isTaskRunning.value = false;
  clearInterval(taskInterval);
  taskInterval = null;
  ElMessage.success("定时任务已停止！");
}

// 调用车辆移动方法
function moveSelectedCar() {
  if (selectedCarId.value && selectedPoiId.value) {
    moveCarToPoi(selectedCarId.value, selectedPoiId.value);
  } else {
    ElMessage.warning("请先选择车辆和目标 POI");
  }
}

// 车辆移动到指定POI点的函数
function moveCarToPoi(carId, poiId, taskId, nextStatus) {
  const car = carList.value.find(car => car.id === carId);
  const poi = poiList.value.find(poi => poi.id === poiId);

  if (car && poi) {
    const startLngLat = [car.longitude, car.latitude];
    const endLngLat = [poi.longitude, poi.latitude];

    // 获取路径规划信息
    getRoad(AMap, map, startLngLat, endLngLat).then(result => {
      const AtRoad = pathToAt(result.routes[0].steps);
      ReRoad(AMap, map, AtRoad, car, false, taskId, nextStatus);
    }).catch(error => {
      ElMessage.error("路径规划失败：" + error.message);
    });
  } else {
    ElMessage.error("车辆或POI信息不完整");
  }
}

// 调用高德API获取路径规划信息
function getRoad(AMap, map, startLngLat, endLngLat) {
  return new Promise((resolve, reject) => {
    AMap.plugin("AMap.Driving", function () {
      var driving = new AMap.Driving({
        policy: 0, //驾车路线规划策略，0是速度优先的策略
      });
      var opts = {};
      driving.search(startLngLat, endLngLat, opts, function (status, result) {
        if (status === 'complete') {
          resolve(result);
        } else {
          reject(new Error('Failed to get driving route'));
        }
      });
    });
  });
}

// 利用路径规划信息生成经纬度路径
function pathToAt(steps) {
  const AtRoad = [];
  steps.forEach(step => {
    step.path.forEach(st => {
      AtRoad.push([st.lng, st.lat]);
    });
  });
  return AtRoad;
}

// 调用高德API进行轨迹回放，并动态更新显示的路线
function ReRoad(AMap, map, AtRoad, car, isGoods, taskId, nextStatus) {
  return new Promise((resolve, reject) => {
    const marker = new AMap.Marker({
      map: map,
      position: AtRoad[0],
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

    var polyline = new AMap.Polyline({
      map: map,
      path: AtRoad,
      showDir: true,
      strokeColor: "#28F",
      strokeOpacity: 1,
      strokeWeight: 6,
    });

    /*var passedPolyline = new AMap.Polyline({
      map: map,
      strokeColor: "#AF5",
      strokeWeight: 6,
    });*/

    marker.on('moving', function (e) {
      //passedPolyline.setPath(e.passedPath);
      // 更新显示的路线为从当前车辆位置到终点的路线
      polyline.setPath(AtRoad.slice(e.passedPath.length));
    });

    marker.moveAlong(AtRoad, {
      duration: 5,
      autoRotation: false,
    });

    // 监听标记移动结束事件
    marker.on('moveend', function () {
      // 确保标记已经移动到路径的最后一个点
      if (marker.getPosition().lng === AtRoad[AtRoad.length - 1][0] &&
          marker.getPosition().lat === AtRoad[AtRoad.length - 1][1]) {
        // 标记移动结束，调用函数
        CarMarkers.push({id: car.id, marker});
        updateTaskStatus(taskId);
      }
    });
    removeVehicleMarker(car.id);

    resolve("动画播放完成");
  });
}

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
  if (taskInterval) {
    clearInterval(taskInterval);
  }
  map?.destroy();
});

// 向后端请求poi数据
function loadPoiData(AMap) {
  get("/api/poi/list", (data) => {
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


// 向后端请求车辆数据
function loadCarData() {
  get("/api/car/list", (data) => {
    data.forEach((car, index) => {
      car.shortId = index + 1;
    });
    carList.value = data;
    addVehicleMarkers(carList.value);
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

// 跳转到 admin 页面
const goToAdminPage = () => {
  router.push('/admin');
};

function removeVehicleMarker(carId) {
  const markerIndex = CarMarkers.findIndex((markerObj) => markerObj.id === carId);
  if (markerIndex !== -1) {
    // 获取标记对象
    const markerObj = CarMarkers[markerIndex];
    // 从地图上移除标记
    markerObj.marker.setMap(null);
    // 从数组中移除记录
    CarMarkers.splice(markerIndex, 1);
    ElMessage.success(`车辆 ${carId} 的标记已删除`);
  } else {
    ElMessage.error(`未找到车辆 ${carId} 的标记`);
  }
}

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
          <el-button type="primary" @click="resetData">数据初始化</el-button>
          <el-button type="info" @click="goToAdminPage">前往管理页面</el-button>
          <div class="move-container">
            <h3 class="text-lg font-bold mb-2">车辆移动</h3>
            <el-select v-model="selectedCarId" placeholder="选择车辆">
              <el-option v-for="car in carList" :key="car.id" :label="`车辆 ${car.id}`" :value="car.id"></el-option>
            </el-select>
            <el-select v-model="selectedPoiId" placeholder="选择目标 POI">
              <el-option v-for="poi in poiList" :key="poi.id" :label="poi.name" :value="poi.id"></el-option>
            </el-select>
            <el-button type="primary" @click="moveSelectedCar">开始移动</el-button>
            <div>
              <el-button type="primary" @click="startTaskInterval" v-if="!isTaskRunning">
                启动定时任务
              </el-button>
              <el-button type="danger" @click="stopTaskInterval" v-if="isTaskRunning">
                停止定时任务
              </el-button>
            </div>
          </div>

        </div>
        <el-select v-model="selectedPoiTypes" multiple placeholder="请选择POI类型" @change="filterPoiMarkers"
                   style="margin-top: 20px;">
          <el-option v-for="type in poiTypes" :key="type" :label="type" :value="type"></el-option>
        </el-select>
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

.left-panel {
  position: absolute;
  top: 0;
  height: 100%;
  z-index: 2;
  background-color: rgb(255, 255, 255);
  transition: width 0.3s ease;
  min-width: 60px;
  box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1);
}

.left-panel {
  left: 0;
  width: 250px;
}

.panel-content {
  padding: 20px;
  height: 100%;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  justify-content: flex-start; /* 确保内容从上方开始布局 */
}

.panel-toggle {
  position: absolute;
  top: 10px; /* 放置在面板的上方 */
  z-index: 3;
}

.left-panel .panel-toggle {
  left: 10px; /* 左侧面板的折叠按钮 */
}

.left-panel.collapsed {
  width: 0;
  padding: 0;
  overflow: hidden;
  background-color: transparent;
}

.left-toggle {
  background-color: rgba(10, 85, 239, 0.9);
  border: none;
}

.button-container {
  display: flex;
  flex-direction: column;
  gap: 20px; /* 增加按钮之间的间距 */
  align-items: stretch; /* 让按钮在容器内拉伸，保持一致的宽度 */
}

.panel-content h2 {
  margin-bottom: 0;
}

.button-container el-button {
  width: 180px; /* 设置所有按钮的固定宽度 */
}

</style>
