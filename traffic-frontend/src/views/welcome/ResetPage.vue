<script setup>

import {ref, reactive, computed} from "vue";
import {ChatRound, Lock, Message} from "@element-plus/icons-vue";
import {get, post} from "@/net/index.js";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";

const active = ref(0)
const coldTime = ref(0);
const formRef = ref();

const form = reactive({
  email: '',
  code: '',
  password: '',
  password_repeat: ''
})

const validatePasswordRepeat = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'));
  } else if (value !== form.password) {
    callback(new Error('两次输入的密码不一致'));
  } else {
    callback();
  }
};

const rule = {
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
    get(`/api/auth/ask-code?email=${form.email}&type=reset`, () => {
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

function confirmReset() {
  formRef.value.validate((valid) => {
    if (valid) {
      post('/api/auth/reset-confirm', {
        email: form.email,
        code: form.code
      }, () => active.value++)
    }
  })
}

function doReset() {
  formRef.value.validate((valid) => {
    if (valid) {
      post('/api/auth/reset-password', {...form}, () => {
        router.push('/')
        ElMessage.success('密码重置成功，请重新登录')
      })
    }
  })

}

</script>

<template>
  <div style="text-align: center;margin: 0 20px">
    <div style="margin-top: 60px">
      <el-steps :active="active" finish-status="success" align-center>
        <el-step title="验证电子邮箱"/>
        <el-step title="重新设定密码"/>
      </el-steps>
    </div>
    <div v-if="active === 0">
      <div style="margin-top: 30px">
        <div style="font-size: 25px;font-weight: bold">重置密码</div>
        <div style="font-size: 14px;color: grey;margin-top: 15px">请输入想要重置密码的电子邮箱地址</div>
      </div>
      <div style="margin-top: 50px">
        <el-form :model="form" :rules="rule" ref="formRef">
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
        <div style="margin-top: 30px">
          <el-button style="width: 270px" type="success" @click="confirmReset" plain>下一步</el-button>
        </div>
      </div>
    </div>
    <div v-if="active === 1">
      <div style="margin-top: 30px">
        <div style="font-size: 25px;font-weight: bold">重置密码</div>
        <div style="font-size: 14px;color: grey;margin-top: 15px">请输入新密码</div>
      </div>
      <div style="margin-top: 50px">
        <el-form :model="form" :rules="rule" ref="formRef">
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
        </el-form>
        <div style="margin-top: 10px">
          <el-button style="width: 270px" type="success" @click="doReset" plain>确认重置</el-button>
        </div>
      </div>
    </div>
    <div style="margin-top: 20px">
      <span style="font-size:14px;line-height:15px;color:grey">想起密码? </span>
      <el-link style="translate: 0 -2px" @click="router.push('/')">立即登录</el-link>
    </div>
  </div>

</template>

<style scoped>

</style>