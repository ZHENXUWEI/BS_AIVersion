<script setup>
  import {reactive } from 'vue';
   
      const credentials = reactive({
        username: '',
        password: ''
      });
      const login= ()=>{
        // 这里添加登录逻辑，例如发送请求到API进行验证
        console.log('Login with credentials:', credentials);
      }

  </script>

<template>
    <div class="login-container">
      <h1>登录</h1>
      <form @submit.prevent="login">
        <div>
          <label for="username">用户名:</label>
            <el-input id="username" v-model="credentials.username"  style="width: 250px"
              placeholder="Please input"/>
        </div>
        <div>
          <label for="password">密 码:</label>
          <el-input id="password" v-model="credentials.password" style="width: 250px"
              placeholder="Please input"/>
          
        </div>
        
        <el-button type="primary">登录</el-button>
      </form>
    </div>
  </template>
   
  
   
  <style scoped>
  .login-container {
    width: 300px;
    margin: 100px auto;
    padding: 20px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  }

  .login-container h1{
   color:#007bff;
    text-align: center;
  }
  form div {
    margin: 10px 0;
  }
  label {
    display: block;
    margin-bottom: 5px;
  }
  input[type="text"], input[type="password"] {
    width: 100%;
    padding: 8px;
    margin-top: 2px;
  }
  button {
    width: 100%;
    padding: 10px;
    margin-top: 10px;
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
  }
  </style>