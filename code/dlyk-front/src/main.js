/*import ... from 语句导入，从vue框架中导入createApp函数*/
import { createApp } from 'vue'

/*从element-plus框架导入ElementPlus组件*/
import ElementPlus from 'element-plus'

//导入国际化的中文包
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'

/*导入element-plus的css样式，不需要from语句*/
import 'element-plus/dist/index.css'

import * as ElementPlusIconsVue from '@element-plus/icons-vue'

//从router.js中导入router组件
import router from "./router/router.js";

/*导入css样式，不需要from子句*/
//import './style.css'
/*从./App.vue凹入App组件*/
import App from './App.vue'
import {doGet} from "./http/httpRequest.js";
/*import LoginView from './view/LoginView.vue'*/

let app = createApp(App);

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

//自定义指令
//el：指令绑定到的元素。这可以用于直接操作 DOM。
//binding：一个对象，包含以下属性。彳亍重点看value属性:传递给指令的值。我们传的是clue:delete这个值

app.directive('hasPermission', (el, binding) => {
    // 这会在 `mounted` 和 `updated` 时都调用
    el.style.color = binding.value

    doGet("/api/login/info",{}).then(resp => {
        let user = resp.data.data
        let permissionList = user.permissionList
        let flag = false
        for (let key in permissionList) {
            if (permissionList[key] === binding.value){
                flag = true
                return flag;
                break
            }
        }
        if (!flag){
            //隐藏权限
            //el.style.display = 'none'
            //删除DOM元素
            el.parentNode && el.parentNode.removeChild(el)
        }
    })

})

/*利用上面导入的createApp函数，创建一个vue项目，mount是挂载到#App的地方*/
app.use(ElementPlus,{locale: zhCn,}).use(router).mount('#app')
