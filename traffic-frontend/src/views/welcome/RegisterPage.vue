<script setup>
import {computed, reactive, ref} from 'vue'
import {ChatRound, Lock, Message, User} from "@element-plus/icons-vue";
import router from "@/router/index.js";
import {ElMessage} from "element-plus";
import {get, post} from "@/net/index.js";

const coldTime = ref(0);
const formRef = ref();

const form = reactive({
  name: '',
  email: '',
  password: '',
  password_repeat: '',
  code: '',
});

const validateUsername = (rule, value, callback) => {
  if (!/^[a-zA-Z0-9\u4e00-\u9fa5]+$/.test(value)) {
    callback(new Error('用户名只能包含字母、数字和汉字'));
  } else {
    callback();
  }
};

const validatePasswordRepeat = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'));
  } else if (value !== form.password) {
    callback(new Error('两次输入的密码不一致'));
  } else {
    callback();
  }
};

const rules = {
  username: [
    {validator: validateUsername, trigger: ['blur', 'change']},
    {required: true, message: '请输入用户名', trigger: 'blur'},
    {min: 2, max: 12, message: '用户名长度必须在2-12个字符之间', trigger: ['blur', 'change']}
  ],
  email: [
    {required: true, message: '请输入邮箱地址', trigger: 'blur'},
    {type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change']}
  ],
  password: [
    {required: true, message: '请输入密码', trigger: 'blur'},
    {min: 6, max: 18, message: '密码长度必须在6-18个字符之间', trigger: ['blur', 'change']}
  ],
  password_repeat: [
    {validator: validatePasswordRepeat, trigger: ['blur', 'change']}
  ],
  code: [
    {required: true, message: '请输入验证码', trigger: 'blur'}
  ]
}

function askCode() {
  if (isEmailValid) {
    coldTime.value = 60;
    get(`/api/auth/ask-code?email=${form.email}&type=register`, () => {
      ElMessage.success('验证码已发送，请注意查收')
      if (coldTime.value > 0) {
        const timer = setInterval(() => {
          coldTime.value--;
          if (coldTime.value === 0) {
            clearInterval(timer);
          }
        }, 1000)
      }
    }, (message) => {
      ElMessage.warning(message);
      coldTime.value = 0;
    })
  } else {
    ElMessage.error('请输入正确的邮箱地址')
  }
}

const isEmailValid = computed(() => /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(form.email))

function register() {
  formRef.value.validate((valid) => {
    if (valid) {
      post('/api/auth/register', {...form}, () => {
        ElMessage.success('注册成功')
        router.push('/')
      })
    } else {
      ElMessage.warning("请正确填写信息")
    }
  })
}

</script>

<template>
  <div style="text-align: center;margin: 0 20px">
    <div style="margin-top: 150px">
      <div style="font-size: 25px;font-weight: bold">用户注册</div>
      <div style="font-size: 14px;color: grey;margin-top: 15px">请填写相关信息进行注册</div>
    </div>
    <div style="margin-top: 20px">
      <el-form :model="form" :rules="rules" ref="formRef">
        <el-form-item prop="username">
          <el-input v-model="form.username" maxlength="12" type="text" placeholder="用户名">
            <template #prefix>
              <el-icon>
                <User/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" maxlength="18" type="password" placeholder="密码">
            <template #prefix>
              <el-icon>
                <Lock/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password_repeat">
          <el-input v-model="form.password_repeat" maxlength="18" type="password" placeholder="重复密码">
            <template #prefix>
              <el-icon>
                <Lock/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="email">
          <el-input v-model="form.email" type="email" placeholder="电子邮箱">
            <template #prefix>
              <el-icon>
                <Message/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="code">
          <el-row :gutter="10" style="width: 100%">
            <el-col :span="16">
              <el-input v-model="form.code" maxlength="6" type="text" placeholder="请输入验证码">
                <template #prefix>
                  <el-icon>
                    <ChatRound/>
                  </el-icon>
                </template>
              </el-input>
            </el-col>
            <el-col :span="6">
              <el-button style="width: 120px" @click=askCode :disabled="!isEmailValid||coldTime" type="primary">
                {{ coldTime > 0 ? coldTime + 's后再次获取' : '获取验证码' }}
              </el-button>
            </el-col>
          </el-row>
        </el-form-item>
      </el-form>
    </div>
    <div style="margin-top: 30px">
      <el-button style="width: 270px" type="success" @click="register" plain>立即注册</el-button>
    </div>
    <div style="margin-top: 20px">
      <span style="font-size:14px;line-height:15px;color:grey">已有账号? </span>
      <el-link style="translate: 0 -2px" @click="router.push('/')">立即登录</el-link>
    </div>
  </div>
</template>

<style scoped>

</style>