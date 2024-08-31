//从vue-router这个依赖库中导入createRouter, createWebHistory中导入createRouter, createWebHistory函数
import { createRouter, createWebHistory } from "vue-router";
//定义一个变量
let router = createRouter({
    //路由历史(网址记录)
    history:createWebHistory(),
    //配置路由，里面为一个数组，可以配直多个路由
    //路由(就是vue访问的路径)
    routes:[
        {
            //路由路径
            path:'/',
            //路由路径对应的页面
            component : () => import('../view/LoginView.vue'),//..当前文件的上一级目录
        },
        {
            //路由路径
            path:'/dashboard',
            //路由路径对应的页面
            component : () => import('../view/DashboardView.vue'),//..当前文件的上一级目录
            //子路由
            children:[
                //Statistic
                {
                    //路由路径,子路由路径不能以/开头(省略)
                    path:'',
                    //路由路径对应的页面
                    component : () => import('../view/StatisticView.vue'),//..当前文件的上一级目录
                },
                {
                    //路由路径,子路由路径不能以/开头(省略)
                    path:'user',
                    //路由路径对应的页面
                    component : () => import('../view/UserView.vue'),//..当前文件的上一级目录
                },
                {
                    //路由路径,子路由路径不能以/开头,id为动态变量，这个叫动态路由
                    path:'user/:id',
                    //路由路径对应的页面
                    component : () => import('../view/UserDetailView.vue'),//..当前文件的上一级目录
                },
                {
                    //路由路径,子路由路径不能以/开头(省略)
                    path:'activity',
                    //路由路径对应的页面
                    component : () => import('../view/ActivityView.vue'),//..当前文件的上一级目录
                },
                {
                    //路由路径,子路由路径不能以/开头(省略)
                    path:'activity/add',
                    //路由路径对应的页面
                    component : () => import('../view/ActivityRecordView.vue'),//..当前文件的上一级目录
                },
                {
                    //路由路径,子路由路径不能以/开头(省略)
                    path:'activity/edit/:id',
                    //路由路径对应的页面
                    component : () => import('../view/ActivityRecordView.vue'),//..当前文件的上一级目录
                },
                {
                    //路由路径,子路由路径不能以/开头(省略)
                    path:'activity/:id',
                    //路由路径对应的页面
                    component : () => import('../view/ActivityDetailView.vue'),//..当前文件的上一级目录
                },
                {
                    //路由路径,子路由路径不能以/开头(省略)
                    path:'clue',
                    //路由路径对应的页面
                    component : () => import('../view/ClueView.vue'),//..当前文件的上一级目录
                },
                {
                    //路由路径,子路由路径不能以/开头(省略)
                    path:'clue/add',
                    //路由路径对应的页面
                    component : () => import('../view/ClueRecordView.vue'),//..当前文件的上一级目录
                },
                {
                    //路由路径,子路由路径不能以/开头(省略)
                    path:'clue/edit/:id',
                    //路由路径对应的页面
                    component : () => import('../view/ClueRecordView.vue'),//..当前文件的上一级目录
                },
                {
                    //路由路径,子路由路径不能以/开头(省略)
                    path:'clue/detail/:id',
                    //路由路径对应的页面
                    component : () => import('../view/ClueDetailView.vue'),//..当前文件的上一级目录
                },

                {
                    //路由路径,子路由路径不能以/开头(省略)
                    path:'customer',
                    //路由路径对应的页面
                    component : () => import('../view/CustomerView.vue'),//..当前文件的上一级目录
                },

            ]
        }

    ]
})
//导出创建的路由对象
export default router;