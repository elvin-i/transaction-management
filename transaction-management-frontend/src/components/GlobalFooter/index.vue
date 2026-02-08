<template>
  <global-footer class="footer custom-render" :copyright="copyright">
    <template v-slot:links>
      <a href="https://i.buukle.top" target="_blank">{{ linkText }}</a>
    </template>
  </global-footer>
</template>

<script>
import { GlobalFooter } from '@ant-design-vue/pro-layout'

export default {
  name: 'ProGlobalFooter',
  components: {
    GlobalFooter
  },
  computed: {
    entry () {
      const pathname = typeof window !== 'undefined' ? window.location.pathname : ''
      return pathname.indexOf('nav') > -1 ? 'nav' : (pathname.indexOf('admin') > -1 ? 'admin' : (pathname.indexOf('ops') > -1 ? 'ops' : 'index'))
    },
    linkText () {
      return this.entry === 'nav' ? 'NAV' : (this.entry === 'admin' ? 'ADMIN' : (this.entry === 'ops' ? 'OPS' : 'HOMEWORK'))
    },
    copyright () {
      if (this.entry === 'nav') return 'Nav Console © ' + new Date().getFullYear()
      if (this.entry === 'admin') return 'Admin Console © ' + new Date().getFullYear()
      if (this.entry === 'ops') return 'Ops Console © ' + new Date().getFullYear()
      return 'Homework © ' + new Date().getFullYear()
    }
  }
}
</script>
