import axios from "axios";
import { getCookie } from "./cookie";
const service = axios.create({
  // baseURL: "/api",
  baseURL: "http://localhost:8080",
  timeout: 5000, // request timeout
});

// 请求拦截器
service.interceptors.request.use(
  (config) => {
    const info = getCookie();
    if (info) {
      console.log(info);
      if (info.token) {
        config.headers.Authorization = "Bearer " + info.token;
      }
    }
    // console.log(1111)
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 响应拦截器
service.interceptors.response.use(
  (response) => {
    return response.data;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export default service;
