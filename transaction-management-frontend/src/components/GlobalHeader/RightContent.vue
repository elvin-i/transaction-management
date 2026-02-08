<template>
  <div :class="wrpCls">
    <avatar-dropdown :menu="showMenu" :current-user="currentUser" :class="prefixCls" />
    <a-tooltip v-if="showBackToNav" title="返回导航">
      <span :class="prefixCls" @click="goNav">
        <a-icon type="appstore" />
        <span class="back-nav-label">返回导航</span>
      </span>
    </a-tooltip>
    <select-lang :class="prefixCls" />
  </div>
</template>

<script>
import AvatarDropdown from './AvatarDropdown'
import SelectLang from '@/components/SelectLang'

export default {
  name: 'RightContent',
  components: {
    AvatarDropdown,
    SelectLang
  },
  props: {
    prefixCls: {
      type: String,
      default: 'ant-pro-global-header-index-action'
    },
    isMobile: {
      type: Boolean,
      default: () => false
    },
    topMenu: {
      type: Boolean,
      required: true
    },
    theme: {
      type: String,
      required: true
    }
  },
  data () {
    return {
      showMenu: true,
      currentUser: {}
    }
  },
  computed: {
    wrpCls () {
      return {
        'ant-pro-global-header-index-right': true,
        [`ant-pro-global-header-index-${(this.isMobile || !this.topMenu) ? 'light' : this.theme}`]: true
      }
    },
    entry () {
      const pathname = typeof window !== 'undefined' ? window.location.pathname : ''
      return pathname.indexOf('nav') > -1 ? 'nav' : (pathname.indexOf('admin') > -1 ? 'admin' : (pathname.indexOf('ops') > -1 ? 'ops' : 'index'))
    },
    base () {
      const pub = process.env.PUBLIC_PATH || '/'
      return pub.endsWith('/') ? pub.slice(0, -1) : pub
    },
    showBackToNav () {
      return this.entry === 'index' || this.entry === 'admin' || this.entry === 'ops'
    },
    userInfo () {
      return this.$store.getters.userInfo
    }
  },
  created () {
    this.currentUser = this.userInfo
  },
  methods: {
    goNav () {
      window.location.href = `${this.base}/nav.html#/home`
    }
  }
}
</script>
