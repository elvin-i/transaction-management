import storage from 'store'
// import { login, getInfo, logout } from '@/api/login'
import { login, logout } from '@/api/login'
import { ACCESS_TOKEN } from '@/store/mutation-types'

function getCookie (cname) {
  var name = cname + '='
  var ca = document.cookie.split(';')
  for (var i = 0; i < ca.length; i++) {
    var c = ca[i]
    while (c.charAt(0) == ' ') c = c.substring(1)
    if (c.indexOf(name) != -1) {
      return c.substring(name.length, c.length)
    }
  }
  return ''
}

const user = {
  state: {
    token: '',
    name: '',
    welcome: '',
    avatar: '',
    info: null
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_NAME: (state, { name, welcome }) => {
      state.name = name
      state.welcome = welcome
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_INFO: (state, info) => {
      state.info = info
    }
  },

  actions: {
    // 登录
    Login ({ commit }, userInfo) {
      return new Promise((resolve, reject) => {
        storage.set(ACCESS_TOKEN, 'fake-token', 7 * 24 * 60 * 60 * 1000)
        commit('SET_TOKEN', 'fake-token')
      })
    },

    // 获取用户信息
    GetInfo ({ commit }) {
      return new Promise((resolve, reject) => {
        commit('SET_NAME', { name: 'ADMIN(如有使用问题请联系zhanglei)' })
        commit('SET_INFO', { name: 'ADMIN' })
        resolve()
        // getInfo().then(response => {
        //   const result = response.result
        //   commit('SET_INFO', result)
        //   commit('SET_NAME', { name: result.name, welcome: welcome() })
        //   commit('SET_AVATAR', result.avatar)
        //   resolve(response)
        // }).catch(error => {
        //   reject(error)
        // })
      })
    },

    // 登出
    Logout ({ commit, state }) {
      return new Promise((resolve) => {
        logout(state.token).then(() => {
          commit('SET_TOKEN', '')
          storage.remove(ACCESS_TOKEN)
          resolve()
        }).catch(() => {
          resolve()
        }).finally(() => {
        })
      })
    }

  }
}

export default user
