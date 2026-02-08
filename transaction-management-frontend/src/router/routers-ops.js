import router from './ops'
import store from '../store'
import NProgress from 'nprogress'
import '@/components/NProgress/nprogress.less'
import notification from 'ant-design-vue/es/notification'
import { setDocumentTitle, domTitle } from '@/utils/domUtil'
import { i18nRender } from '@/locales'

NProgress.configure({ showSpinner: false })

router.beforeEach((to, from, next) => {
  NProgress.start()
  to.meta && typeof to.meta.title !== 'undefined' && setDocumentTitle(`${i18nRender(to.meta.title)} - ${domTitle}`)
  if (store.getters.userInfo === null) {
    store.dispatch('GetInfo').then(() => {
      store.dispatch('GenerateRoutes').then(() => {
        router.addRoutes(store.getters.addRouters)
      })
      next()
    }).catch((e) => {
      console.log(e)
      notification.error({ message: '错误', description: '请求用户信息失败，请重试' })
    })
  } else {
    next()
  }
})

router.afterEach(() => {
  NProgress.done()
})
