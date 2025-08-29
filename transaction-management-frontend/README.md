## buukle-generator-front 
----
based on ant-design-pro

### 1. important configuration

#### 1.1 request backend url prefix -- VUE_APP_API_BASE_URL
##### 1.1.1 location 
@utils/request.js

##### 1.1.2 code

(1) declare

``.env.development  .env.preview  .env.production``

````
VUE_APP_API_BASE_URL=/buukle-generator
````
(2) usage
````
const request = axios.create({
  // API  请求的默认前缀
  baseURL: process.env.VUE_APP_API_BASE_URL,
  timeout: 6000 // 请求超时时间
})
````
#### 1.2 request frontend publicUrl

##### 1.2.1 location : vue.config.js

##### 1.2.2 code

(1) declare

``.env.development  .env.preview  .env.production``

````
PUBLIC_PATH=/buukle-generator/
````

(2) usage
````
const vueConfig = {
  publicPath: process.env.PUBLIC_PATH
  ....
}
````
