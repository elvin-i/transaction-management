<template>
  <a-card :bordered="false">
    <a-row :gutter="16">
      <a-col :md="8" :sm="24">
        <a-card title="支付运营控制台" :bordered="false">
          <p>跳转到 index 应用。</p>
          <a-button type="primary" icon="bank" @click="goIndex">进入</a-button>
        </a-card>
      </a-col>
      <a-col :md="8" :sm="24">
        <a-card title="管理控制台" :bordered="false">
          <p>跳转到 admin 应用。</p>
          <a-button type="primary" icon="setting" @click="goAdmin">进入</a-button>
        </a-card>
      </a-col>
      <a-col :md="8" :sm="24">
        <a-card title="运维控制台" :bordered="false">
          <p>跳转到 ops 应用。</p>
          <a-button type="primary" icon="tool" @click="goOps">进入</a-button>
        </a-card>
      </a-col>
    </a-row>
  </a-card>
</template>

<script>
export default {
  name: 'NavLanding',
  methods: {
    goIndex () { window.location.href = `${this.base}/index.html#/home` },
    goAdmin () { window.location.href = `${this.base}/admin.html#/home` },
    goOps () { window.location.href = `${this.base}/ops.html#/home` }
  },
  computed: {
    base () {
      // honor PUBLIC_PATH if set, else root
      const pub = process.env.PUBLIC_PATH || '/'
      return pub.endsWith('/') ? pub.slice(0, -1) : pub
    }
  }
}
</script>

<style scoped>
p { margin-bottom: 12px; }
</style>
