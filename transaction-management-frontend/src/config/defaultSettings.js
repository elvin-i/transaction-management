/**
 * 项目默认配置项
 * primaryColor - 默认主题色, 如果修改颜色不生效，请清理 localStorage
 * navTheme - sidebar theme ['dark', 'light'] 两种主题
 * colorWeak - 色盲模式
 * layout - 整体布局方式 ['sidemenu', 'topmenu'] 两种布局
 * fixedHeader - 固定 Header : boolean
 * fixSiderbar - 固定左侧菜单栏 ： boolean
 * contentWidth - 内容区布局： 流式 |  固定
 *
 * storageOptions: {} - Vue-ls 插件配置项 (localStorage/sessionStorage)
 *
 */

const pathname = typeof window !== 'undefined' ? window.location.pathname : ''
const entry = pathname.indexOf('nav') > -1 ? 'nav' : (pathname.indexOf('admin') > -1 ? 'admin' : (pathname.indexOf('ops') > -1 ? 'ops' : 'index'))

const titles = {
  index: '支付运营控制台',
  admin: '管理控制台',
  ops: '运维控制台',
  nav: '导航控制台'
}

export default {
  // All entries use light (white) sidebar, including admin
  navTheme: 'light', // theme for nav menu: 'dark' | 'light'
  primaryColor: '#13c2c2', // primary color of ant design
  layout: entry === 'nav' ? 'topmenu' : 'sidemenu', // nav: use topmenu
  contentWidth: entry === 'nav' ? 'Fixed' : 'Fluid', // topmenu usually pairs with Fixed
  fixedHeader: true, // sticky header
  fixSiderbar: true, // sticky siderbar
  colorWeak: false,
  menu: { locale: true },
  title: titles[entry] || 'homework',
  pwa: false,
  iconfontUrl: '',
  production: process.env.NODE_ENV === 'production' && process.env.VUE_APP_PREVIEW !== 'true'
}
