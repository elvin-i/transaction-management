<template>
  <a-modal
    v-model="visible"
    :destroyOnClose="true"
    :width="900"
    :height="600"
    :visible="visible"
    :title="getTitle(initvalue)"
  >
    <div class="main">
      <template>
        <div>
          <a-row type="flex">
            <!-- 业务请求流水号 -->
            <a-col :span="12" style="margin-top: 5px">
              <a-col :span="6" align="right">
                <span>业务请求流水号:</span>
              </a-col>
              <a-col :span="18" style="padding-left: 10px;">
                <span> {{ initvalue.requestNo }} </span>
              </a-col>
            </a-col>

            <!-- 业务类型 -->
            <a-col :span="12" style="margin-top: 5px">
              <a-col :span="6" align="right">
                <span>业务类型:</span>
              </a-col>
              <a-col :span="18" style="padding-left: 10px;">
                <span> {{ initvalue.businessType }} </span>
              </a-col>
            </a-col>

            <!-- 出款户号 -->
            <a-col :span="12" style="margin-top: 5px">
              <a-col :span="6" align="right">
                <span>出款户号:</span>
              </a-col>
              <a-col :span="18" style="padding-left: 10px;">
                <span> {{ initvalue.payerAccountNo }} </span>
              </a-col>
            </a-col>

            <!-- 出款户名 -->
            <a-col :span="12" style="margin-top: 5px">
              <a-col :span="6" align="right">
                <span>出款户名:</span>
              </a-col>
              <a-col :span="18" style="padding-left: 10px;">
                <span> {{ initvalue.payerAccountName }} </span>
              </a-col>
            </a-col>

            <!-- 出款机构 -->
            <a-col :span="12" style="margin-top: 5px">
              <a-col :span="6" align="right">
                <span>出款机构:</span>
              </a-col>
              <a-col :span="18" style="padding-left: 10px;">
                <span> {{ initvalue.payerOrgCode }} </span>
              </a-col>
            </a-col>

            <!-- 收款户号 -->
            <a-col :span="12" style="margin-top: 5px">
              <a-col :span="6" align="right">
                <span>收款户号:</span>
              </a-col>
              <a-col :span="18" style="padding-left: 10px;">
                <span> {{ initvalue.payeeAccountNo }} </span>
              </a-col>
            </a-col>

            <!-- 收款户名 -->
            <a-col :span="12" style="margin-top: 5px">
              <a-col :span="6" align="right">
                <span>收款户名:</span>
              </a-col>
              <a-col :span="18" style="padding-left: 10px;">
                <span> {{ initvalue.payeeAccountName }} </span>
              </a-col>
            </a-col>

            <!-- 收款机构 -->
            <a-col :span="12" style="margin-top: 5px">
              <a-col :span="6" align="right">
                <span>收款机构:</span>
              </a-col>
              <a-col :span="18" style="padding-left: 10px;">
                <span> {{ initvalue.payeeOrgCode }} </span>
              </a-col>
            </a-col>

            <!-- 交易金额 -->
            <a-col :span="12" style="margin-top: 5px">
              <a-col :span="6" align="right">
                <span>交易金额:</span>
              </a-col>
              <a-col :span="18" style="padding-left: 10px;">
                <span> {{ initvalue.amount }} </span>
              </a-col>
            </a-col>
            <!-- 创建时间 -->
            <a-col :span="12" style="margin-top: 5px">
              <a-col :span="6" align="right">
                <span>创建时间:</span>
              </a-col>
              <a-col :span="18" style="padding-left: 10px;">
                <span> {{ gmtDateFormat(initvalue.gmtCreated) }} </span>
              </a-col>
            </a-col>
            <!-- 更新时间 -->
            <a-col :span="12" style="margin-top: 5px">
              <a-col :span="6" align="right">
                <span>更新时间:</span>
              </a-col>
              <a-col :span="18" style="padding-left: 10px;">
                <span> {{ gmtDateFormat( initvalue.gmtUpdated) }} </span>
              </a-col>
            </a-col>
            <!-- 备注 -->
            <a-col :span="24" style="margin-top: 5px">
              <a-col :span="3" align="right">
                <span>备注:</span>
              </a-col>
              <a-col :span="21" style="padding-left: 10px;">
                <span> {{ initvalue.remark }} </span>
              </a-col>
            </a-col>
            <!-- 附言 -->
            <a-col :span="24" style="margin-top: 5px">
              <a-col :span="3" align="right">
                <span>附言:</span>
              </a-col>
              <a-col :span="21" style="padding-left: 10px;">
                <span> {{ initvalue.postscript }} </span>
              </a-col>
            </a-col>
          <!-- row end -->
          </a-row>
        </div>
      </template>
    </div>
    <template slot="footer">
      <a-button @click="handleClose()"> 返回 </a-button>
    </template>
  </a-modal>
</template>
<script>

import moment from 'moment'
import { ref, computed } from 'vue'

const formItemLayout = {
  labelCol: { span: 5 },
  wrapperCol: { span: 16 }
}
export default {
  methods: {
    getTitle (initvalue) {
      return <span> 详情</span>
    },
    show () {
      this.visible = true
    },
    handleClose () {
      this.visible = false
    },
    gmtDateFormat (arr) {
      if (!arr || arr.length < 6) return '';
      // 将数组转换为 Date 对象，月份减 1，纳秒转换为毫秒
      const date = new Date(
        arr[0],            // 年
        arr[1] - 1,        // 月
        arr[2],            // 日
        arr[3],            // 时
        arr[4],            // 分
        arr[5],            // 秒
        arr[6] ? Math.floor(arr[6] / 1e6) : 0 // 纳秒 -> 毫秒
      );
      return moment(date).format('YYYY-MM-DD HH:mm:ss');
    },
    handleDown (url) {
      const headers = {}
      headers[process.env.VUE_APP_AUTHORIZATION_HEADER_KEY] = this.getCookie(process.env.VUE_APP_AUTHORIZATION_COOKIE_KEY)
      fetch(url, {
        method: 'GET',
        headers: headers
      }).then(res => res.blob()).then(data => {
          const blobUrl = window.URL.createObjectURL(data)
          const a = document.createElement('a')
          a.href = blobUrl
          a.click()
        })
    },
    getCookie (cname) {
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
  },
  props: {
    initvalue: {
      type: Object, // 类型
      default: null // 默认值
    }
  },
  data () {
    return {
      visible: false,
      formItemLayout
    }
  }
}
</script>
