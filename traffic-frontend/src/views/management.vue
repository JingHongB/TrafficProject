<script setup>
import {ElMessageBox, ElMessage} from 'element-plus';
import {ref, computed, watch, onMounted} from 'vue'
import {List, Box, Van, Location} from '@element-plus/icons-vue'
import {get, post} from "@/net/index.js";
import { useRouter } from 'vue-router';

// 当前激活的模块
const activeModule = ref('task')
const dialogVisible = ref(false)
const dialogType = ref('add')
ref({
  taskId: '',
  dateRange: []
});

const formData = ref({})

const router = useRouter();  // 用于路由跳转

const typeLoadMap = {
  1: 25,
  2: 15,
  3: 10,
}

// 动态设置载重量
watch(
    () => formData.value.typeId,
    (newType) => {
      formData.value.loadCapacity = typeLoadMap[newType] || 0;
    }
);

//存放数据
const taskList = ref([])
const goodsList = ref([])
const carList = ref([])
const poiList = ref([])

//初始化
onMounted(() => {
  if (activeModule.value === 'task') {
    getTaskList(); // 初始加载委托数据
  }
});

// 计算属性
const moduleTitle = computed(() => {
  const titles = {
    task: '委托',
    goods: '货物',
    car: '车辆',
    poi: '地点'
  }
  return titles[activeModule.value]
})

const dialogTitle = computed(() => {
  return formData.value.id ? '编辑' + moduleTitle.value : '新增' + moduleTitle.value
})

// 车辆相关方法
const getCarList = () => {
  get("/api/car/list", (data) => {
    data.forEach((car, index) => {
      car.shortId = index + 1;
    });
    carList.value = data;
  });
}

const deleteCar = (id) => {
  post(`/api/car/delete/${id}`, null, () => {
    getCarList();
  });
}

const addCar = () => {
  post("/api/car/add", {
    typeId: formData.value.typeId,
    longitude: formData.value.longitude,
    latitude: formData.value.latitude,
  }, () => {
    ElMessage.success("新增车辆成功");
    dialogVisible.value = false; // 关闭对话框
    getCarList(); // 刷新车辆列表
  });
};

const editCar = () => {
  post(`/api/car/update`, {
    id: formData.value.id,
    typeId: formData.value.typeId,
    longitude: formData.value.longitude,
    latitude: formData.value.latitude
  }, () => {
    ElMessage.success("编辑车辆成功");
    dialogVisible.value = false; // 关闭对话框
    getCarList(); // 刷新车辆列表
  });
};

// 货物相关方法
const getGoodsList = () => {
  get("/api/goods/list", (data) => {
    data.forEach((goods, index) => {
      goods.shortId = index + 1;
    });
    goodsList.value = data;
  });
}

const deleteGoods = (id) => {
  post(`/api/goods/delete/${id}`, null, () => {
    getGoodsList();
  });
}

// Poi相关方法
const getPoiList = () => {
  get("/api/poi/list", (data) => {
    data.forEach((poi, index) => {
      poi.shortId = index + 1;
    });
    poiList.value = data;
  });
}

const deletePoi = (id) => {
  post(`/api/poi/delete/${id}`, null, () => {
    getPoiList();
  });
}

// 委托相关方法
const getTaskList = () => {
  get("/api/task/list", (data) => {
    data.forEach((task, index) => {
      task.shortId = index + 1;
    });
    taskList.value = data;
  });
}

const deleteTask = (id) => {
  post(`/api/task/delete/${id}`, null, () => {
    getTaskList();
  });
}

// 监听 activeModule 的变化，当切换到不同页面时获取不同数据
watch(activeModule, (newValue) => {
  if (newValue === 'car') {
    getCarList(); // 触发获取车辆数据的请求
  } else if (newValue === 'goods') {
    getGoodsList();
  } else if (newValue === 'poi') {
    getPoiList();
  } else if (newValue === 'task') {
    getTaskList();
  }
});

const handleAdd = () => {
  formData.value = {}
  dialogType.value = 'add'
  dialogVisible.value = true
}

const handleEdit = (row) => {
  formData.value = {...row}
  dialogType.value = 'edit'
  dialogVisible.value = true
}

//删除操作
const handleDelete = (row) => {
  ElMessageBox.confirm(
      '确认删除该记录吗？',
      '警告',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
  )
      .then(() => {
        if (activeModule.value === 'task') {
          deleteTask(row.id)
        } else if (activeModule.value === 'goods') {
          deleteGoods(row.id)
        } else if (activeModule.value === 'car') {
          deleteCar(row.id)
        } else if (activeModule.value === 'poi') {
          deletePoi(row.id)
        }
        ElMessage({
          type: 'success',
          message: '删除成功',
        })
      })
      .catch(() => {
        ElMessage({
          type: 'info',
          message: '已取消删除',
        })
      })
}

const handleSave = () => {
  if (activeModule.value === "car") {
    if (dialogType.value === 'edit') {
      editCar(); // 调用编辑车辆方法
    } else {
      addCar(); // 调用新增车辆方法
    }
  }
  dialogVisible.value = false
}
const handleClose = () => {
  dialogVisible.value = false
}

const getStatusType = (status) => {
  const types = {
    pending: '',
    in_progress: 'warning',
    completed: 'success',
    cancelled: 'danger'
  }
  return types[status] || ''
}

// 跳转到 index 页面
const goToIndexPage = () => {
  router.push('/index');
};
</script>

<template>
  <el-container class="dashboard">
    <el-aside width="200px">
      <div class="logo-container">
        <h3 class="text-white">运输管理系统</h3>
      </div>
      <el-menu
          default-active="1"
          class="el-menu-vertical"
          background-color="#545c64"
          text-color="#fff"
      >
        <el-menu-item index="1" @click="activeModule = 'task'">
          <el-icon>
            <List/>
          </el-icon>
          <span>委托管理</span>
        </el-menu-item>
        <el-menu-item index="2" @click="activeModule = 'goods'">
          <el-icon>
            <Box/>
          </el-icon>
          <span>货物管理</span>
        </el-menu-item>
        <el-menu-item index="3" @click="activeModule = 'car'">
          <el-icon>
            <Van/>
          </el-icon>
          <span>车辆管理</span>
        </el-menu-item>
        <el-menu-item index="4" @click="activeModule = 'poi'">
          <el-icon>
            <Location/>
          </el-icon>
          <span>地点管理</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header>
        <div class="header-content">
          <h2>{{ moduleTitle }}</h2>
          <div class="header-right">
            <!-- 新增跳转按钮 -->
            <el-button @click="goToIndexPage" type="primary">
              跳转地图界面
            </el-button>
            <el-button type="primary" @click="handleAdd">
              新增{{ moduleTitle }}
            </el-button>
          </div>
        </div>
      </el-header>

      <el-main>
        <!-- 委托管理模块 -->
        <div v-if="activeModule === 'task'">

          <el-table :data="taskList" style="width: 100%">
            <el-table-column prop="shortId" label="ID" width="120" align="center"/>
            <el-table-column prop="goods" label="货物类型" width="150" align="center"/>
            <el-table-column prop="startPoint" label="起点" width="230" align="center" :show-overflow-tooltip="true" />
            <el-table-column prop="endPoint" label="终点" width="230" align="center" :show-overflow-tooltip="true" />
            <el-table-column prop="distance" label="距离(km)" width="150" align="center"/>
            <el-table-column prop="status" label="状态" width="120" align="center">
              <template #default="scope">
                <el-tag :type="getStatusType(scope.row.status)">
                  {{ scope.row.status }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" fixed="right" width="150" align="center">
              <template #default="scope">
                <el-button link type="primary" @click="handleEdit(scope.row)">
                  编辑
                </el-button>
                <el-button link type="danger" @click="handleDelete(scope.row)">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <!-- 货物管理模块 -->
        <div v-if="activeModule === 'goods'">
          <el-table :data="goodsList" style="width: 100%">
            <el-table-column prop="shortId" label="ID" width="120" align="center" />
            <el-table-column prop="type" label="货物类型" width="120" align="center"/>
            <el-table-column prop="weight" label="重量(kg)" width="120" align="center"/>
            <el-table-column prop="owner" label="货主" width="240" align="center" :show-overflow-tooltip="true" />
            <el-table-column prop="status" label="状态" width="120" align="center">
              <template #default="scope">
                <el-tag :type="getStatusType(scope.row.status)">
                  {{ scope.row.status }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" fixed="right" width="150" align="center">
              <template #default="scope">
                <el-button link type="primary" @click="handleEdit(scope.row)">
                  编辑
                </el-button>
                <el-button link type="danger" @click="handleDelete(scope.row)">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <!-- 车辆管理模块 -->
        <div v-if="activeModule === 'car'">
          <el-table :data="carList" style="width: 100%">
            <el-table-column prop="shortId" label="ID" width="120" align="center"/>
            <el-table-column prop="typeName" label="车辆类型" width="120" align="center"/>
            <el-table-column prop="loadCapacity" label="载重(kg)" width="120" align="center"/>
            <el-table-column prop="longitude" label="经度" width="140" align="center"/>
            <el-table-column prop="latitude" label="纬度" width="140" align="center"/>
            <el-table-column prop="status" label="状态" width="120" align="center">
              <template #default="scope">
                <el-tag :type="getStatusType(scope.row.status)">
                  {{ scope.row.status }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" fixed="right" width="150" align="center">
              <template #default="scope">
                <el-button link type="primary" @click="handleEdit(scope.row)">
                  编辑
                </el-button>
                <el-button link type="danger" @click="handleDelete(scope.row)">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <!-- 地点管理模块 -->
        <div v-if="activeModule === 'poi'">
          <el-table :data="poiList" style="width: 100%">
            <el-table-column prop="shortId" label="ID" width="120" align="center"/>
            <el-table-column prop="name" label="名称" width="220" align="center" :show-overflow-tooltip="true" />
            <el-table-column prop="typeName" label="类型" width="140" align="center"/>
            <el-table-column prop="longitude" label="经度" width="140" align="center"/>
            <el-table-column prop="latitude" label="纬度" width="140" align="center"/>
            <el-table-column prop="status" label="状态" width="120" align="center">
              <template #default="scope">
                <el-tag :type="getStatusType(scope.row.status)">
                  {{ scope.row.status }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" fixed="right" width="150" align="center">
              <template #default="scope">
                <el-button link type="primary" @click="handleEdit(scope.row)">
                  编辑
                </el-button>
                <el-button link type="danger" @click="handleDelete(scope.row)">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-main>
    </el-container>

    <!-- 通用编辑对话框 -->
    <el-dialog
        v-model="dialogVisible"
        :title="dialogTitle"
        width="50%"
        :before-close="handleClose"
    >
      <el-form :model="formData" label-width="120px">
        <template v-if="activeModule === 'task'">
          <el-form-item label="货物ID">
            <el-input v-model="formData.goods_id" style="width: 30%;"/>
          </el-form-item>
          <el-form-item label="起点">
            <el-input v-model="formData.start_id" style="width: 70%;"/>
          </el-form-item>
          <el-form-item label="终点">
            <el-input v-model="formData.end_id" style="width: 70%;"/>
          </el-form-item>
          <el-form-item label="车辆ID">
            <el-input v-model="formData.car_id" style="width: 30%;"/>
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="formData.status" style="width: 30%;">
              <el-option label="待接取" value="pending"/>
              <el-option label="运输中" value="in_progress"/>
              <el-option label="已完成" value="completed"/>
              <el-option label="已取消" value="cancelled"/>
            </el-select>
          </el-form-item>
        </template>

<!--        货物管理编辑-->
        <template v-if="activeModule === 'goods'">
          <el-form-item label="类型">
            <el-input v-model="formData.type_id" style="width:40%;"/>
          </el-form-item>
          <el-form-item label="重量">
            <el-input-number v-model="formData.weight" style="width: 40%;"/>
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="formData.status" style="width: 30%;">
              <el-option label="待接取" value="pending"/>
              <el-option label="运输中" value="in_progress"/>
              <el-option label="已完成" value="completed"/>
              <el-option label="已取消" value="cancelled"/>
            </el-select>
          </el-form-item>
          <el-form-item label="货主">
            <el-input v-model="formData.owner_id" style="width: 70%;"/>
          </el-form-item>
        </template>

<!--        车辆管理编辑-->
        <template v-if="activeModule === 'car'">
          <!-- 车辆类型选择 -->
          <el-form-item label="车辆类型">
            <el-select v-model="formData.typeId" placeholder="请选择车辆类型" style="width: 40%;">
              <el-option label="大车型" value="1"></el-option>
              <el-option label="中车型" value="2"></el-option>
              <el-option label="小车型" value="3"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="载重(t)">
            <el-input-number v-model="formData.loadCapacity" :disabled="true" style="width: 40%;"/>
          </el-form-item>
          <el-form-item label="经度">
            <el-input-number v-model="formData.longitude" placeholder="请输入经度" style="width: 40%;"/>
          </el-form-item>
          <el-form-item label="纬度">
            <el-input-number v-model="formData.latitude" placeholder="请输入纬度" style="width: 40%;"/>
          </el-form-item>
        </template>

<!--        地点管理编辑-->
        <template v-if="activeModule === 'poi'">
          <el-form-item label="名称">
            <el-input v-model="formData.name" style="width: 70%;"/>
          </el-form-item>
          <el-form-item label="地址">
            <el-input v-model="formData.address" style="width: 70%;"/>
          </el-form-item>
          <el-form-item label="类型">
            <el-input v-model="formData.type_id" style="width: 40%;"/>
          </el-form-item>
          <el-form-item label="经度">
            <el-input-number v-model="formData.longitude" style="width: 40%;" />
          </el-form-item>
          <el-form-item label="纬度">
            <el-input-number v-model="formData.latitude" style="width: 40%;"/>
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="formData.status" style="width: 30%;">
              <el-option label="待接取" value="pending"/>
              <el-option label="运输中" value="in_progress"/>
              <el-option label="已完成" value="completed"/>
              <el-option label="已取消" value="cancelled"/>
            </el-select>
          </el-form-item>
        </template>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSave">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </el-container>
</template>


<style scoped>
.dashboard {
  height: 100vh;
}

.logo-container {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #545c64;
  color: white;
}

.el-header {
  background-color: #ffffff;
  border-bottom: 1px solid #ffffff;
  padding: 0 30px;
}

.header-content {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.el-aside {
  background-color: #545c64;
  width: 250px; /* 调整宽度 */
  font-size: 20px;
  box-shadow: 4px 0 6px rgba(0, 0, 0, 0.1); /* 增加阴影效果 */
}


.el-menu-vertical {
  border-right: none;
}

.search-bar {
  margin: 20px 0;
  padding: 20px;
  background-color: white;
  border-radius: 4px;
}


.el-main {
  background-color: #f0f2f5;
  padding: 20px;

}

.demo-tabs {
  margin-bottom: 20px;
}

.el-table {
  font-size: 17px; /* 调整表格字体大小 */
  margin-top: 6px; /* 增加上方间隔 */
}

.el-menu-item {
  font-size: 18px;
  margin-top: 12px;
  padding-top: 20px; /* 调整间距 */
  padding-bottom: 16px;
}

</style>
