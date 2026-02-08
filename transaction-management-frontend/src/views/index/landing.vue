<template>
  <a-card :bordered="false">
    <a-row :gutter="16">
      <a-col :md="12" :sm="24">
        <a-card title="支付运营概览" :bordered="false">
          <p>欢迎来到支付运营控制台。这是首页（index/home）。</p>
          <a-button type="primary" icon="profile">交易报表</a-button>
          <a-button style="margin-left: 8px" icon="fund">运营分析</a-button>
        </a-card>
      </a-col>
      <a-col :md="12" :sm="24">
        <a-card title="常用操作" :bordered="false">
          <a-button icon="search">订单查询</a-button>
          <a-button style="margin-left: 8px" icon="sync">对账处理</a-button>
        </a-card>
      </a-col>
    </a-row>
  </a-card>
  
</template>

<script>
export default {
  name: 'IndexLanding'
}
</script>

<style scoped>
p { margin-bottom: 12px; }
</style>
