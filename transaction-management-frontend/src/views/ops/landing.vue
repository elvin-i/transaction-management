<template>
  <a-card :bordered="false">
    <a-row :gutter="16">
      <a-col :md="12" :sm="24">
        <a-card title="运维概览" :bordered="false">
          <p>欢迎来到运维控制台。这是独立的落地页，不依赖交易管理。</p>
          <a-button type="primary" icon="tool">服务管理</a-button>
          <a-button style="margin-left: 8px" icon="alert">告警中心</a-button>
        </a-card>
      </a-col>
      <a-col :md="12" :sm="24">
        <a-card title="快捷操作" :bordered="false">
          <a-button icon="reload">发布/回滚</a-button>
          <a-button style="margin-left: 8px" icon="database">配置中心</a-button>
        </a-card>
      </a-col>
    </a-row>
  </a-card>
</template>

<script>
export default {
  name: 'OpsLanding'
}
</script>

<style scoped>
p { margin-bottom: 12px; }
</style>
