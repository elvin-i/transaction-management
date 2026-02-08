<template>
  <a-card :bordered="false">
    <a-row :gutter="16">
      <a-col :md="12" :sm="24">
        <a-card title="管理概览" :bordered="false">
          <p>欢迎来到管理控制台。这是独立的落地页，不依赖交易管理。</p>
          <a-button type="primary" icon="setting">系统配置</a-button>
          <a-button style="margin-left: 8px" icon="team">用户与角色</a-button>
        </a-card>
      </a-col>
      <a-col :md="12" :sm="24">
        <a-card title="快速入口" :bordered="false">
          <a-button icon="dashboard">仪表盘</a-button>
          <a-button style="margin-left: 8px" icon="profile">报表中心</a-button>
        </a-card>
      </a-col>
    </a-row>
  </a-card>
</template>

<script>
export default {
  name: 'AdminLanding'
}
</script>

<style scoped>
p { margin-bottom: 12px; }
</style>
