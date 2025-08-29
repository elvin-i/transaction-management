<style>
div .line-border{
  border: 1px #dccece solid;
  border-style: dashed;
  padding-top: 20px;
  padding-right: 20px;
  margin-top: 15px;
  margin-bottom: 15px;
}
</style>
<template>
  <a-modal
    v-model="visible"
    :destroyOnClose="true"
    :width="1080"
    :visible="visible"
    :title="getTitle(initvalue)"
  >
    <div class="main">
      <a-form-model ref="form" :layout="formLayout" :model="form" :rules="rules">
        <a-form-model-item
          v-show="false"
          label="id"
          prop="id"
          :label-col="formItemLayout.labelCol"
          :wrapper-col="formItemLayout.wrapperCol"
        >
          <a-input v-model="form.id" />
        </a-form-model-item>

          <!-- 业务请求流水号  -->
          <a-form-model-item
            label="业务请求流水号"
            prop="requestNo"
            :label-col="formItemLayout.labelCol"
            :wrapper-col="formItemLayout.wrapperCol"
          >
            <a-input v-model="form.requestNo" placeholder="业务请求流水号" />
          </a-form-model-item>
          <!-- 业务类型  -->
          <a-form-model-item
            label="业务类型"
            prop="businessType"
            :label-col="formItemLayout.labelCol"
            :wrapper-col="formItemLayout.wrapperCol"
          >
            <a-input v-model="form.businessType" placeholder="业务类型" />
          </a-form-model-item>
          <!-- 出款账户  -->
          <a-form-model-item
            label="出款账户"
            prop="payerAccountNo"
            :label-col="formItemLayout.labelCol"
            :wrapper-col="formItemLayout.wrapperCol"
          >
            <a-input v-model="form.payerAccountNo" placeholder="出款账户" />
          </a-form-model-item>
          <!-- 出款户名  -->
          <a-form-model-item
            label="出款户名"
            prop="payerAccountName"
            :label-col="formItemLayout.labelCol"
            :wrapper-col="formItemLayout.wrapperCol"
          >
            <a-input v-model="form.payerAccountName" placeholder="出款户名" />
          </a-form-model-item>
          <!-- 出款机构  -->
          <a-form-model-item
            label="出款机构"
            prop="payerOrgCode"
            :label-col="formItemLayout.labelCol"
            :wrapper-col="formItemLayout.wrapperCol"
          >
            <a-input v-model="form.payerOrgCode" placeholder="出款机构" />
          </a-form-model-item>
          <!-- 收款账户  -->
          <a-form-model-item
            label="收款账户"
            prop="payeeAccountNo"
            :label-col="formItemLayout.labelCol"
            :wrapper-col="formItemLayout.wrapperCol"
          >
            <a-input v-model="form.payeeAccountNo" placeholder="收款账户" />
          </a-form-model-item>
          <!-- 收款户名  -->
          <a-form-model-item
            label="收款户名"
            prop="payeeAccountName"
            :label-col="formItemLayout.labelCol"
            :wrapper-col="formItemLayout.wrapperCol"
          >
            <a-input v-model="form.payeeAccountName" placeholder="收款户名" />
          </a-form-model-item>
          <!-- 收款机构  -->
          <a-form-model-item
            label="收款机构"
            prop="payeeOrgCode"
            :label-col="formItemLayout.labelCol"
            :wrapper-col="formItemLayout.wrapperCol"
          >
            <a-input v-model="form.payeeOrgCode" placeholder="收款机构" />
          </a-form-model-item>
          <!-- 交易金额  -->
          <a-form-model-item
            label="交易金额"
            prop="amount"
            :label-col="formItemLayout.labelCol"
            :wrapper-col="formItemLayout.wrapperCol"
          >
            <a-input-number
              v-model="form.amount"
              :min="0.01"
              placeholder="交易金额"
              style="width: 100%;"
            />
          </a-form-model-item>
          <!-- 备注  -->
          <a-form-model-item
            label="备注"
            prop="remark"
            :label-col="formItemLayout.labelCol"
            :wrapper-col="formItemLayout.wrapperCol"
          >
            <a-input v-model="form.remark" placeholder="备注" />
          </a-form-model-item>
          <!-- 附言  -->
          <a-form-model-item
            label="附言"
            prop="postscript"
            :label-col="formItemLayout.labelCol"
            :wrapper-col="formItemLayout.wrapperCol"
          >
            <a-input v-model="form.postscript" placeholder="附言" />
          </a-form-model-item>

      </a-form-model>
    </div>
    <template slot="footer">
      <a-button @click="handleEdit()" type="primary"> 提交 </a-button>
      <a-button @click="handleClose()"> 取消 </a-button>
    </template>

  </a-modal>
</template>

<script>

import clone from 'clone'
import request from '@/utils/request'
import { message } from 'ant-design-vue'

const formItemLayout = {
  labelCol: { span: 3 },
  wrapperCol: { span: 21 }
}

export default {
  components: {
  },
  props: {
    initvalue: {
      type: Object, // 类型
      default: null // 默认值
    }
  },
  watch: {
    initvalue () {
      if (this.initvalue) {
        if (this.initvalue.url) {
          const file = {
            uid: this.initvalue.url,
            name: this.initvalue.url,
            status: 'done'
          }
          this.fileList.splice(this.fileList.indexOf(file), 1)
          this.fileList.push(file)
        }
        for (const key in this.initvalue) {
          this.form[key] = this.initvalue[key]
        }
      } else {
        this.form = clone(this.initForm)
      }
    },
    // 清理表单
    visible (val, newVal) {
      if (!val) {
        try {
          // 清理文件上传缓存
          this.fileList = []
          this.form = clone(this.initForm)
        } catch (e) {}
      }
    }
  },
  methods: {
    getTitle (initvalue) {
      if (initvalue) {
        return <span> 编辑 </span>
      } else {
        return <span> 新建 </span>
      }
    },
    show () {
      this.visible = true
    },
    handleClose () {
      this.form = clone(this.initForm)
      this.form.param = ''
      this.visible = false
    },
    handleEdit () {
      this.$refs.form.validate((bool) => {
        if (bool) {
          // 深拷贝,以避开提交时改变源数据
          var data = JSON.parse(JSON.stringify(this.form))
          request({
            url: '/api/web/1.0/order',
            method: 'post',
            dataType: 'json',
            data: data
          }).then(res => {
            if (res.code === 200) {
              message.success('成功!')
              this.handleClose()
              this.$emit('refresh')
            } else {
              message.error(res.head.msg)
            }
          }).catch((err) => {
            console.log(err)
          })
        }
      })
    }
  },
  data () {
    return {
      fileList: [],
      visible: false,
      formLayout: 'horizontal',
      formItemLayout,
      datasourcesList: [],
      form: {
        requestNo        :null,
        businessType     :null,
        payerAccountNo   :null,
        payerAccountName :null,
        payerOrgCode     :null,
        payeeAccountNo   :null,
        payeeAccountName :null,
        payeeOrgCode     :null,
        amount           :null,
        remark           :null,
        postscript       :null
      },
      initForm: {
        requestNo        :null,
        businessType     :null,
        payerAccountNo   :null,
        payerAccountName :null,
        payerOrgCode     :null,
        payeeAccountNo   :null,
        payeeAccountName :null,
        payeeOrgCode     :null,
        amount           :null,
        remark           :null,
        postscript       :null
      },
      rules: {
        requestNo: [
            { required: true, message: '请输入' },
            { max: 50, message: '最多输入50个字符' },
            {
              pattern: /^(?!(\s+$))/,
              message: '输入格式错误'
            }
          ],
        businessType: [
          { required: true, message: '请输入' },
          { max: 50, message: '最多输入50个字符' },
          {
            pattern: /^(?!(\s+$))/,
            message: '输入格式错误'
          }
        ],
        payerAccountNo: [
          { required: true, message: '请输入' },
          { max: 50, message: '最多输入50个字符' },
          {
            pattern: /^(?!(\s+$))/,
            message: '输入格式错误'
          }
        ],
        payerAccountName: [
          { required: true, message: '请输入' },
          { max: 50, message: '最多输入50个字符' },
          {
            pattern: /^(?!(\s+$))/,
            message: '输入格式错误'
          }
        ],
        payerOrgCode: [
          { required: true, message: '请输入' },
          { max: 50, message: '最多输入50个字符' },
          {
            pattern: /^(?!(\s+$))/,
            message: '输入格式错误'
          }
        ],
        payeeAccountNo: [
          { required: true, message: '请输入' },
          { max: 50, message: '最多输入50个字符' },
          {
            pattern: /^(?!(\s+$))/,
            message: '输入格式错误'
          }
        ],
        payeeAccountName: [
          { required: true, message: '请输入' },
          { max: 50, message: '最多输入50个字符' },
          {
            pattern: /^(?!(\s+$))/,
            message: '输入格式错误'
          }
        ],
        payeeOrgCode: [
          { required: true, message: '请输入' },
          { max: 50, message: '最多输入50个字符' },
          {
            pattern: /^(?!(\s+$))/,
            message: '输入格式错误'
          }
        ],
        amount: [
          { required: true, message: '请输入' },
        ]
        },
        commonRequest: {
          head: {
            operationTime: Date.now(),
            appId: process.env.VUE_APP_BUUKLE_APP_ID
          },
          body: {}
        },
        bodyById: {
          id: 0
        }
      }
  }
}
</script>
