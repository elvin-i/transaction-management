<template>
  <page-header-wrapper>
    <template v-slot:content>
      <div class="page-header-content">
        <div class="avatar">
          <a-avatar size="large" :src="currentUser.avatar" />
        </div>
        <div class="content">
          <div class="content-title">
            <span class="welcome-text">{{ welcome }}</span>
          </div>
          <div>{{ user.name }} | admin</div>
        </div>
      </div>
    </template>
    <template v-slot:extraContent>
      <div class="extra-content">

        <div class="stat-item">
          <a-statistic title="archetype模板数" :value="archetypesCount" />
        </div>
        <div class="stat-item">
          <a-statistic title="archetype生成数" :value="archetypesGenCount" />
        </div>
        <div class="stat-item">
          <a-statistic title="连接数" :value="datasourcesCount" />
        </div>
        <div class="stat-item">
          <a-statistic title="代码模板数" :value="templatesCount" />
        </div>
        <div class="stat-item">
          <a-statistic title="配置数" :value="configuresCount" />
        </div>
        <div class="stat-item">
          <a-statistic title="配置生成数" :value="configuresGenCount" />
        </div>
      </div>
    </template>
  </page-header-wrapper>
</template>

<script>
import { mapState } from 'vuex'
import { PageHeaderWrapper } from '@ant-design-vue/pro-layout'
import { Radar } from '@/components'
import request from "@/utils/request";
import {message} from "ant-design-vue";

export default {
  name: 'workplace',
  components: {
    PageHeaderWrapper,
    Radar
  },
  methods: {
    getArchetypesCount () {
      this.commonRequest.head.operationTime = Date.now()
      const commonRequest = this.commonRequest
      request({
        url: '/workspace/getArchetypesCount',
        method: 'post',
        dataType: 'json',
        data: commonRequest
      }).then(res => {
        if (res.head.status === 'S') {
          this.archetypesCount = res.body
        } else {
          message.error(res.head.msg)
        }
      }).catch((err) => {
        console.log(err)
      })
    },
    getDatasourcesCount () {
      this.commonRequest.head.operationTime = Date.now()
      const commonRequest = this.commonRequest
      request({
        url: '/workspace/getDatasourcesCount',
        method: 'post',
        dataType: 'json',
        data: commonRequest
      }).then(res => {
        if (res.head.status === 'S') {
          this.datasourcesCount = res.body
        } else {
          message.error(res.head.msg)
        }
      }).catch((err) => {
        console.log(err)
      })
    },
    getTemplatesCount () {
      this.commonRequest.head.operationTime = Date.now()
      const commonRequest = this.commonRequest
      request({
        url: '/workspace/getTemplatesCount',
        dataType: 'json',
        method: 'post',
        data: commonRequest
      }).then(res => {
        if (res.head.status === 'S') {
          this.templatesCount = res.body
        } else {
          message.error(res.head.msg)
        }
      }).catch((err) => {
        console.log(err)
      })
    },
    getConfiguresCount () {
      this.commonRequest.head.operationTime = Date.now()
      const commonRequest = this.commonRequest
      request({
        url: '/workspace/getConfiguresCount',
        method: 'post',
        dataType: 'json',
        data: commonRequest
      }).then(res => {
        if (res.head.status === 'S') {
          this.configuresCount = res.body
        } else {
          message.error(res.head.msg)
        }
      }).catch((err) => {
        console.log(err)
      })
    },
    getArchetypesGenCount () {
      this.commonRequest.head.operationTime = Date.now()
      const commonRequest = this.commonRequest
      request({
        url: '/workspace/getArchetypesGenCount',
        method: 'post',
        dataType: 'json',
        data: commonRequest
      }).then(res => {
        if (res.head.status === 'S') {
          this.archetypesGenCount = res.body
        } else {
          message.error(res.head.msg)
        }
      }).catch((err) => {
        console.log(err)
      })
    },
    getConfiguresGenCount () {
      this.commonRequest.head.operationTime = Date.now()
      const commonRequest = this.commonRequest
      request({
        url: '/workspace/getConfiguresGenCount',
        method: 'post',
        dataType: 'json',
        data: commonRequest
      }).then(res => {
        if (res.head.status === 'S') {
          this.configuresGenCount = res.body
        } else {
          message.error(res.head.msg)
        }
      }).catch((err) => {
        console.log(err)
      })
    },
  },
  data () {
    return {
      archetypesCount: 0,
      datasourcesCount: 0,
      templatesCount: 0,
      configuresCount: 0,
      archetypesGenCount: 0,
      configuresGenCount: 0,
      commonRequest: {
        head: {
          operationTime: Date.now(),
        },
        body: {}
      },
    }
  },
  computed: {
    ...mapState({
      nickname: state => state.user.nickname,
      welcome: state => state.user.welcome
    }),
    currentUser () {
      return {
        name: '',
        avatar: ''
      }
    },
    userInfo () {
      return this.$store.getters.userInfo
    }
  },
  created () {
    this.user = this.userInfo
    this.avatar = this.userInfo.avatar
  },
  mounted () {
    this.getDatasourcesCount()
    this.getTemplatesCount()
    this.getConfiguresCount()
    this.getArchetypesGenCount()
    this.getConfiguresGenCount()
    this.getArchetypesCount()
  }
}
</script>

<style lang="less" scoped>
@import 'workplace.less';

.project-list {
  .card-title {
    font-size: 0;

    a {
      color: rgba(0, 0, 0, 0.85);
      margin-left: 12px;
      line-height: 24px;
      height: 24px;
      display: inline-block;
      vertical-align: top;
      font-size: 14px;

      &:hover {
        color: #1890ff;
      }
    }
  }

  .card-description {
    color: rgba(0, 0, 0, 0.45);
    height: 44px;
    line-height: 22px;
    overflow: hidden;
  }

  .project-item {
    display: flex;
    margin-top: 8px;
    overflow: hidden;
    font-size: 12px;
    height: 20px;
    line-height: 20px;

    a {
      color: rgba(0, 0, 0, 0.45);
      display: inline-block;
      flex: 1 1 0;

      &:hover {
        color: #1890ff;
      }
    }

    .datetime {
      color: rgba(0, 0, 0, 0.25);
      flex: 0 0 auto;
      float: right;
    }
  }

  .ant-card-metaDTO-description {
    color: rgba(0, 0, 0, 0.45);
    height: 44px;
    line-height: 22px;
    overflow: hidden;
  }
}

.item-group {
  padding: 20px 0 8px 24px;
  font-size: 0;

  a {
    color: rgba(0, 0, 0, 0.65);
    display: inline-block;
    font-size: 14px;
    margin-bottom: 13px;
    width: 25%;
  }
}

.members {
  a {
    display: block;
    margin: 12px 0;
    line-height: 24px;
    height: 24px;

    .member {
      font-size: 14px;
      color: rgba(0, 0, 0, 0.65);
      line-height: 24px;
      max-width: 100px;
      vertical-align: top;
      margin-left: 12px;
      transition: all 0.3s;
      display: inline-block;
    }

    &:hover {
      span {
        color: #1890ff;
      }
    }
  }
}

.mobile {
  .project-list {
    .project-card-grid {
      width: 100%;
    }
  }

  .more-info {
    border: 0;
    padding-top: 16px;
    margin: 16px 0 16px;
  }

  .headerContent .title .welcome-text {
    display: none;
  }
}
</style>
