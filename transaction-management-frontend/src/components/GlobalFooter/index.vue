<template>
  <global-footer class="footer custom-render">
    <template v-slot:links>
      <a href="https://i.buukle.top" target="_blank">HOMEWORK</a>
    </template>
  </global-footer>
</template>

<script>
import { GlobalFooter } from '@ant-design-vue/pro-layout'

export default {
  name: 'ProGlobalFooter',
  components: {
    GlobalFooter
  }
}
</script>
