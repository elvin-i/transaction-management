// eslint-disable-next-line
import * as loginService from '@/api/login'
// eslint-disable-next-line
import { BasicLayout, BlankLayout, PageView, RouteView } from '@/layouts'

// 前端路由表
const constantRouterComponents = {
  // 基础页面 layout 必须引入
  BasicLayout: BasicLayout,
  BlankLayout: BlankLayout,
  RouteView: RouteView,
  PageView: PageView,

  // 你需要动态引入的页面组件
  Workplace: () => import('@/views/workplace')

}

// 前端未找到页面路由（固定不用改）
const notFoundRouter = {
  path: '*',
  redirect: '/workplace',
  hidden: true
}

// 根级菜单
const rootRouter = {
  key: '',
  name: 'index',
  path: '',
  component: 'BasicLayout',
  redirect: '/workplace',
  meta: {
    title: '首页'
  },
  children: []
}

/**
 * 动态生成菜单
 * @returns {Promise<Router>}
 */
export const generatorDynamicRouter = () => {
  return new Promise((resolve, reject) => {
    // Detect current entry page to generate different menus per system
    const pathname = typeof window !== 'undefined' ? window.location.pathname : ''
    const entry = pathname.indexOf('nav') > -1 ? 'nav' : (pathname.indexOf('admin') > -1 ? 'admin' : (pathname.indexOf('ops') > -1 ? 'ops' : 'index'))

    let result = []
    if (entry === 'admin') {
      rootRouter.redirect = '/home'
      result = [
        {
          id: 1,
          parentId: 0,
          component: 'PageView',
          name: 'admin',
          meta: { show: true, title: '管理控制台', icon: 'setting' }
        },
        {
          id: 2,
          parentId: 1,
          component: 'admin/landing.vue',
          name: '/home',
          path: '/home',
          meta: { show: true, title: '首页', icon: 'dashboard' }
        }
      ]
    } else if (entry === 'ops') {
      rootRouter.redirect = '/home'
      result = [
        {
          id: 11,
          parentId: 0,
          component: 'PageView',
          name: 'ops',
          meta: { show: true, title: '运维控制台', icon: 'tool' }
        },
        {
          id: 12,
          parentId: 11,
          component: 'ops/landing.vue',
          name: '/home',
          path: '/home',
          meta: { show: true, title: '首页', icon: 'dashboard' }
        }
      ]
    } else if (entry === 'nav') {
      // Navigation hub: a single landing page with external links to other apps
      rootRouter.redirect = '/home'
      result = [
        {
          id: 31,
          parentId: 0,
          component: 'PageView',
          name: 'nav',
          meta: { show: true, title: '导航控制台', icon: 'appstore' }
        },
        {
          id: 32,
          parentId: 31,
          component: 'nav/landing.vue',
          name: '/home',
          path: '/home',
          meta: { show: true, title: '导航', icon: 'home' }
        }
      ]
    } else {
      // default index (支付运营控制台)
      rootRouter.redirect = '/home'
      result = [
        {
          id: 21,
          parentId: 0,
          component: 'PageView',
          name: 'index',
          meta: { show: true, title: '支付运营控制台', icon: 'bank' }
        },
        {
          id: 22,
          parentId: 21,
          component: 'index/landing.vue',
          name: '/home',
          path:'/home',
          meta: { show: true, title: '首页', icon: 'home' }
        },
        {
          id: 23,
          parentId: 21,
          component: 'transaction/list.vue',
          name: '/transaction/list',
          path:'/transaction/list',
          meta: { show: true, title: '交易订单列表', icon: 'profile' }
        }
      ]
    }
    // Use the computed `result` directly to produce menu/tree
    const menuNav = []
    const childrenNav = []
    //      后端数据, 根级树数组,  根级 PID
    listToTree(result, childrenNav, 0)
    rootRouter.children = childrenNav
    menuNav.push(rootRouter)
    console.log('menuNav', menuNav)
    const routers = generator(menuNav)
    routers.push(notFoundRouter)
    console.log('routers', routers)
    resolve(routers)
  })
}

/**
 * 格式化树形结构数据 生成 vue-router 层级路由表
 *
 * @param routerMap
 * @param parent
 * @returns {*}
 */
export const generator = (routerMap, parent) => {
  return routerMap.map(item => {
    const { title, show, hideChildren, hiddenHeaderContent, target, icon } = item.meta || {}
    const currentRouter = {
      // 如果路由设置了 path，则作为默认 path，否则 路由地址 动态拼接生成如 /dashboard/workplace
      path: item.path || `${(parent && parent.path) || ''}/${item.key}`,
      // 路由名称，建议唯一
      name: item.name || item.key || '',
      // 该路由对应页面的 组件 :方案1
      // component: constantRouterComponents[item.component || item.key],
      // 该路由对应页面的 组件 :方案2 (动态加载)
      component: constantRouterComponents[item.component || item.key] || (() => import(`@/views/${item.component}`)),

      // meta: 页面标题, 菜单图标, 页面权限(供指令权限用，可去掉)
      meta: {
        title: title,
        icon: icon || undefined,
        hiddenHeaderContent: hiddenHeaderContent,
        target: target,
        permission: item.name
      }
    }
    // 是否设置了隐藏菜单
    if (show === false) {
      currentRouter.hidden = true
    }
    // 是否设置了隐藏子菜单
    if (hideChildren) {
      currentRouter.hideChildrenInMenu = true
    }
    // 为了防止出现后端返回结果不规范，处理有可能出现拼接出两个 反斜杠
    if (!currentRouter.path.startsWith('http')) {
      currentRouter.path = currentRouter.path.replace('//', '/')
    }
    // 重定向
    item.redirect && (currentRouter.redirect = item.redirect)
    // 是否有子菜单，并递归处理
    if (item.children && item.children.length > 0) {
      // Recursion
      currentRouter.children = generator(item.children, currentRouter)
    }
    return currentRouter
  })
}

/**
 * 数组转树形结构
 * @param list 源数组
 * @param tree 树
 * @param parentId 父ID
 */
const listToTree = (list, tree, parentId) => {
  list.forEach(item => {
    // 判断是否为父级菜单
    if (item.parentId === parentId) {
      const child = {
        ...item,
        key: item.key || item.name,
        children: []
      }
      // 迭代 list， 找到当前菜单相符合的所有子菜单
      listToTree(list, child.children, item.id)
      // 删掉不存在 children 值的属性
      if (child.children.length <= 0) {
        delete child.children
      }
      // 加入到树中
      tree.push(child)
    }
  })
}
