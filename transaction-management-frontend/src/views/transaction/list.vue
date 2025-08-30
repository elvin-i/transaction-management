<template>
    <a-card :bordered="false">
      <div class="table-page-search-wrapper">
        <a-form layout="inline">
          <a-row :gutter="48">
            <a-col :md="8" :sm="24">
              <a-form-item label="流水号">
                <a-input v-model="queryParam.requestNo" name="requestNo" placeholder=""/>
              </a-form-item>
            </a-col>
            <template v-if="advanced">
            </template>
            <a-col :md="!advanced && 8 || 24" :sm="24">
              <span class="table-page-search-submitButtons" :style="advanced && { float: 'right', overflow: 'hidden' } || {} ">
                <a-button type="primary" @click="handleAdd">新建</a-button>
                <a-button style="margin-left: 8px" type="primary" @click="handleSearch()">查询</a-button>
                <a-button style="margin-left: 8px" @click="() => this.queryParam = {}">重置</a-button>
              </span>
            </a-col>
          </a-row>
        </a-form>
      </div>

      <s-table
        ref="table"
        size="default"
        rowKey="id"
        :columns="columns"
        :data="loadData"
        :alert="true"
        :rowSelection="rowSelection"
        :pagination="{ showTotal: total => `共 ${total} 条记录` }"
      >
        <span slot="serial" slot-scope="text, record, index">
          {{ index + 1 }}
        </span>
        <span slot="status" slot-scope="text">
          <a-badge :status="text | statusTypeFilter" :text="text | statusFilter" />
        </span>
        <span slot="tooltip-request-no" slot-scope="text">
          <ellipsis :length="30" tooltip>{{ text }}</ellipsis>
        </span>
        <span slot="gmtCreated" slot-scope="text">
          <ellipsis :length="64" tooltip>{{ gmtDateFormat(text) }}</ellipsis>
        </span>
        <span slot="action" slot-scope="text, record">
          <template>
            <a @click="handleEdit(record.id)">编辑</a>&nbsp;
            <a @click="handleDelete(record.id)">删除</a>&nbsp;
            <a @click="handleInfo(record.id)">详情</a>&nbsp;
          </template>
        </span>
      </s-table>
      <AddOrEditForm
        ref="AddOrEditForm"
        :initvalue="initvalue"
        @refresh="refresh"
      />
      <InfoTable
        ref="InfoTable"
        :initvalue="initvalueForInfo"
        @refresh="refresh"
      />
    </a-card>
</template>

<script>
import { STable, Ellipsis } from '@/components'
 import request from '@/utils/request'
import { message } from 'ant-design-vue'
import AddOrEditForm from '@/views/transaction/addOrEditForm'
import InfoTable from '@/views/transaction/infoTable'
import moment from 'moment'

message.config({
  duration: 3, // 提示时常单位为s
  top: '40px', // 距离顶部的位置
  maxCount: 3 // 最多显示提示信息条数(后面的会替换前面的提示)
})

export default {
  name: 'List',
  components: {
    InfoTable,
    STable,
    Ellipsis,
    AddOrEditForm
  },
  methods: {
    refresh () {
      this.$refs.table.refresh(true)
    },
    handleSearch () {
      this.$refs.table.pagination.pageNo = 1
      this.$refs.table.refresh(true)
    },
    handleAdd () {
      this.initvalue = {}
      this.$refs.AddOrEditForm.show()
    },
    handleDelete (id) {
      var obj = this.$refs.table
      this.commonRequest.head.operationTime = Date.now()
      this.bodyById.id = id
      this.commonRequest.body = this.bodyById
      const commonRequest = this.commonRequest
      this.$confirm({
        title: '确认提示',
        content: `是否删除该条记录？`,
        okType: 'danger',
        okText: '确认',
        cancelText: '取消',
        onOk () {
          request({
            url: '/api/web/1.0/order/' + id,
            method: 'delete',
            dataType: 'json',
            data: commonRequest
          }).then(res => {
            message.success('成功!')
            obj.refresh(true)
          }).catch((err) => {
            message.error('系统开小差了')
            console.log(err)
          })
        }
      })
    },
    handleEdit (id) {
      request({
        url: '/api/web/1.0/order/' + id,
        method: 'get',
      }).then(res => {
        if (res.code === 200) {
          this.initvalue = res.data
          this.$refs.AddOrEditForm.show()
        } else {
          message.error(res.info)
        }
      }).catch((err) => {
        console.log(err)
      })
    },
    handleInfo (id) {
      request({
        url: '/api/web/1.0/order/' + id,
        method: 'get',
      }).then(res => {
        if (res.code === 200) {
          this.initvalueForInfo = res.data
          this.$refs.InfoTable.show()
        } else {
          message.error(res.info)
        }
      }).catch((err) => {
        console.log(err)
      })
    },
    onSelectChange (selectedRowKeys, selectedRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectedRows = selectedRows
    },
    toggleAdvanced () {
      this.advanced = !this.advanced
    },
    gmtDateFormat (text) {
      return moment(text).format('yyyy-MM-DD HH:mm:ss')
    }
  },
  data () {
    this.columns = columns
    return {
      initvalue: {},
      initvalueForInfo: {},
      // create model
      visible: false,
      confirmLoading: false,
      dialogFormVisible: false,
      mdl: null,
      // 高级搜索 展开/关闭
      advanced: false,
      // 查询参数
      queryParam: {},
      commonRequest: {
        head: {
          operationTime: Date.now(),
          appId: process.env.VUE_APP_BUUKLE_APP_ID
        },
        body: {}
      },
      bodyById: {
        id: 0
      },
      // 加载数据方法 必须为 Promise 对象
      loadData: parameter => {
        const param = Object.assign({}, parameter, this.queryParam)
        this.commonRequest.body = param
        return request({
          url: '/api/web/1.0/order',
          method: 'get',
          params: param,
          dataType: 'json'
        }).then(res => {
          let r = res.data
          let wrapperR = {
            pageSize:'',
            pageNo:'',
            totalCount:'',
            totalPage:'',
            data:[]
          }
          wrapperR.pageNo = r.current
          wrapperR.pageSize = r.size
          wrapperR.totalCount = r.total
          wrapperR.totalPage = r.total / r.size + (r.total % r.size > 0 ? 1 : 0)
          wrapperR.data = r.records

          return wrapperR
        })
      },
      selectedRowKeys: [],
      selectedRows: []
    }
  },
  filters: {
    statusFilter (type) {
      return statusMap[type].text
    },
    statusTypeFilter (type) {
      return statusMap[type].status
    }
  },
   computed: {
    rowSelection () {
      return {
        selectedRowKeys: this.selectedRowKeys,
        onChange: this.onSelectChange
      }
    }
  }
}
const columns = [
  {
     title: '序号',
    width: '5%',
    scopedSlots: { customRender: 'serial' }
  },
  {
    title: 'id',
    dataIndex: 'id',
    width: '5%'
  },
  {
    title: '请求流水号',
    dataIndex: 'requestNo',
    width: '20%',
    scopedSlots: { customRender: 'tooltip-request-no' }
  },
  {
    title: '出款户号',
    dataIndex: 'payerAccountNo',
    width: '15%'
  },
  {
    title: '出款户名',
    dataIndex: 'payerAccountName',
    width: '10%'
  },
  {
    title: '收款户号',
    dataIndex: 'payeeAccountNo',
    width: '15%'
  },
  {
    title: '收款户名',
    dataIndex: 'payeeAccountName',
    width: '10%'
  },
  {
    title: '交易金额',
    dataIndex: 'amount',
    width: '10%'
  },
  {
    title: '操作',
    dataIndex: 'action',
    width: '15%',
    scopedSlots: { customRender: 'action' }
  }
]
const statusMap = {
  1: {
    status: 'default',
    text: '创建完成'
  },
  2: {
    status: 'processing',
    text: '审批中'
  },
  3: {
    status: 'success',
    text: '正常'
  },
  4: {
    status: 'warning',
    text: '已禁用'
  }
}
</script>
