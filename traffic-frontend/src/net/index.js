import axios from "axios";
import {ElMessage} from "element-plus";

//默认失败处理
const defaultFailure = (message, code, url) => {
    console.error(`请求地址: ${url} 状态码: ${code} 错误信息: ${message}`);
    ElMessage.warning(message)
}

//默认错误处理
const defaultError = (err) => {
    console.error(err);
    ElMessage.warning('发生了一些错误，请联系管理员')
}

//封装post请求
function internalPost(url, data, success, failure) {
    axios.post(url, data).then(({data}) => {
        if (data.code === 200) {
            success(data.data);
        } else {
            failure(data.message, data.code, url);
        }
    }).catch(err => defaultError(err))
}

//封装get请求
function internalGet(url, success, failure) {
    axios.get(url).then(({data}) => {
        if (data.code === 200) {
            success(data.data);
        } else {
            failure(data.message, data.code, url);
        }
    }).catch(err => defaultError(err))
}

//get请求
function get(url, success, failure = defaultFailure) {
    internalGet(url, success, failure);
}

//post请求
function post(url, data, success, failure = defaultFailure) {
    internalPost(url, data, success, failure);
}

//暴露方法
export {get, post}