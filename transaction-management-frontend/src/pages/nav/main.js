// with polyfills
import 'core-js/stable'

import Vue from 'vue'
import App from '../../App.vue'
import router from '../../router/nav'
import store from '../../store/'
import i18n from '../../locales'
import { VueAxios } from '../../utils/request'
import ProLayout, { PageHeaderWrapper } from '@ant-design-vue/pro-layout'
import themePluginConfig from '../../../config/themePluginConfig'
import Antd from 'ant-design-vue'
import 'ant-design-vue/dist/antd.css'

import bootstrap from '../../core/bootstrap'
import '../../core/lazy_use'
import '../../router/routers-nav'
import '../../utils/filter'
import '../../global.less'

Vue.config.productionTip = false

Vue.use(VueAxios)
Vue.use(Antd)
Vue.component('pro-layout', ProLayout)
Vue.component('page-container', PageHeaderWrapper)
Vue.component('page-header-wrapper', PageHeaderWrapper)

window.umi_plugin_ant_themeVar = themePluginConfig.theme

new Vue({
  router,
  store,
  i18n,
  created: bootstrap,
  render: h => h(App)
}).$mount('#app')
