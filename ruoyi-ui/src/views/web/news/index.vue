<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--新闻数据-->
      <el-col :xs="24">
        <el-form
          :model="queryParams"
          ref="queryForm"
          size="small"
          :inline="true"
          v-show="showSearch"
          label-width="68px"
        >
          <el-form-item label="新闻名称" prop="newsName">
            <el-input
              v-model="queryParams.newsName"
              placeholder="请输入新闻名称"
              clearable
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select
              v-model="queryParams.status"
              placeholder="新闻状态"
              clearable
              style="width: 240px"
            >
              <el-option
                v-for="dict in dict.type.sys_normal_disable"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="创建时间">
            <el-date-picker
              v-model="dateRange"
              style="width: 240px"
              value-format="yyyy-MM-dd"
              type="daterange"
              range-separator="-"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
            ></el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button
              type="primary"
              icon="el-icon-search"
              size="mini"
              @click="handleQuery"
              >搜索</el-button
            >
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
              >重置</el-button
            >
          </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
              type="primary"
              plain
              icon="el-icon-plus"
              size="mini"
              @click="handleAdd"
              v-hasPermi="['web:news:add']"
              >新增</el-button
            >
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="success"
              plain
              icon="el-icon-edit"
              size="mini"
              :disabled="single"
              @click="handleUpdate"
              v-hasPermi="['web:news:edit']"
              >修改</el-button
            >
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="danger"
              plain
              icon="el-icon-delete"
              size="mini"
              :disabled="multiple"
              @click="handleDelete"
              v-hasPermi="['web:news:remove']"
              >删除</el-button
            >
          </el-col>
          <!-- <el-col :span="1.5">
            <el-button
              type="info"
              plain
              icon="el-icon-upload2"
              size="mini"
              @click="handleImport"
              v-hasPermi="['web:news:import']"
              >导入</el-button
            >
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="warning"
              plain
              icon="el-icon-download"
              size="mini"
              @click="handleExport"
              v-hasPermi="['web:news:export']"
              >导出</el-button
            >
          </el-col> -->
          <right-toolbar
            :showSearch.sync="showSearch"
            @queryTable="getList"
            :columns="columns"
          ></right-toolbar>
        </el-row>

        <el-table
          v-loading="loading"
          :data="newsList"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column
            label="新闻ID"
            align="center"
            key="newsId"
            prop="newsId"
            v-if="columns[0].visible"
          />
          <el-table-column
            label="新闻名称"
            align="center"
            key="newsName"
            prop="newsName"
            v-if="columns[1].visible"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="状态"
            align="center"
            key="status"
            v-if="columns[2].visible"
          >
            <template slot-scope="scope">
              <el-switch
                v-model="scope.row.status"
                active-value="0"
                inactive-value="1"
                @change="handleStatusChange(scope.row)"
              ></el-switch>
            </template>
          </el-table-column>
          <el-table-column
            label="创建时间"
            align="center"
            prop="createTime"
            v-if="columns[3].visible"
            width="160"
          >
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="操作"
            align="center"
            width="160"
            class-name="small-padding fixed-width"
          >
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['web:news:edit']"
                >查看 | 修改</el-button
              >
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                style="color: red"
                @click="handleDelete(scope.row)"
                v-hasPermi="['web:news:remove']"
                >删除</el-button
              >
            </template>
          </el-table-column>
        </el-table>

        <pagination
          v-show="total > 0"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
        />
      </el-col>
    </el-row>

    <!-- 添加或修改新闻配置对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      width="60%"
      fullscreen
      append-to-body
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="新闻名称" prop="newsName">
              <el-input
                v-model="form.newsName"
                placeholder="请输入新闻名称"
                maxlength="500"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="状态">
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="dict in dict.type.sys_normal_disable"
                  :key="dict.value"
                  :label="dict.value"
                  >{{ dict.label }}</el-radio
                >
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="图片" prop="avatar">
              <el-upload
                v-loading="imageLoading"
                class="avatar-uploader"
                :file-list="imgFileList"
                action="#"
                :limit="1"
                accept="image/jpeg,image/png"
                :show-file-list="false"
                :http-request="handleUpImg"
              >
                <div v-if="form.avatar" class="img-wrap">
                  <img :src="form.avatar" class="avatar" />
                </div>
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input
                v-model="form.remark"
                type="textarea"
                placeholder="请输入备注"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="内容">
              <editor v-model="form.content" :min-height="200" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 新闻导入对话框 -->
    <el-dialog
      :title="upload.title"
      :visible.sync="upload.open"
      width="400px"
      append-to-body
    >
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <div class="el-upload__tip" slot="tip">
            <el-checkbox v-model="upload.updateSupport" />
            是否更新已经存在的新闻数据
          </div>
          <span>仅允许导入xls、xlsx格式文件。</span>
          <el-link
            type="primary"
            :underline="false"
            style="font-size: 12px; vertical-align: baseline"
            @click="importTemplate"
            >下载模板</el-link
          >
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  upImg,
  listNews,
  getNews,
  delNews,
  addNews,
  updateNews,
  resetNewsPwd,
  changeNewsStatus,
} from "@/api/web/news";
import { getToken } from "@/utils/auth";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "News",
  dicts: ["sys_normal_disable"],
  components: { Treeselect },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 图片上传loading
      imageLoading: false,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 新闻表格数据
      newsList: null,
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 日期范围
      dateRange: [],
      // 表单参数
      form: {},
      defaultProps: {
        children: "children",
        label: "label",
      },
      // 新闻导入参数
      upload: {
        // 是否显示弹出层（新闻导入）
        open: false,
        // 弹出层标题（新闻导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的新闻数据
        updateSupport: 0,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/web/news/importData",
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        newsName: undefined,
        status: undefined,
      },
      // 列信息
      columns: [
        { key: 0, label: `新闻ID`, visible: true },
        { key: 1, label: `新闻名称`, visible: true },
        { key: 2, label: `状态`, visible: true },
        { key: 3, label: `创建时间`, visible: true },
      ],
      // 表单校验
      rules: {
        newsName: [
          { required: true, message: "新闻名称不能为空", trigger: "blur" },
          {
            min: 2,
            max: 500,
            message: "新闻名称长度必须介于 2 和 500 之间",
            trigger: "blur",
          },
        ],
        avatar: [{ required: true, message: "图片不能为空", trigger: "blur" }],

      },
      imgFileList: [],
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询新闻列表 */
    getList() {
      this.loading = true;
      listNews(this.addDateRange(this.queryParams, this.dateRange)).then(
        (response) => {
          this.newsList = response.rows;
          this.total = response.total;
          this.loading = false;
        }
      );
    },
    // 筛选节点
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    // 新闻状态修改
    handleStatusChange(row) {
      let text = row.status === "0" ? "启用" : "停用";
      this.$modal
        .confirm('确认要"' + text + '""' + row.newsName + '"新闻吗？')
        .then(function () {
          return changeNewsStatus(row.newsId, row.status);
        })
        .then(() => {
          this.$modal.msgSuccess(text + "成功");
        })
        .catch(function () {
          row.status = row.status === "0" ? "1" : "0";
        });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        newsId: undefined,
        newsName: undefined,
        status: "0",
        remark: undefined,
        avatar: undefined,
      };
      this.imgFileList = []
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },

    handleUpImg(fileInfo) {
      this.imgFileList = []
      const { file } = fileInfo;
      const fileTypeList = ["image/jpeg", "image/png"];
      const isJPGPNG = fileTypeList.includes(file.type);
      const fileSizeLimit = 5;
      const isLtLimit = file.size / 1024 / 1024 < fileSizeLimit;
      if (!isJPGPNG) {
        this.$message.error("上传图片只能是 JPG 或 PNG 格式!");
        return;
      }
      if (!isLtLimit) {
        this.$message.error(`上传图片大小不能超过 ${fileSizeLimit}MB!`);
        return;
      }
      const formdata = new FormData();
      formdata.append("file", file);
      this.imageLoading = true;
      upImg(formdata)
        .then((res) => {
          this.form.avatar = res.url;
          console.log(res);
        })
        .catch((err) => {})
        .finally(() => {
          this.imageLoading = false;
        });
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.$refs.tree.setCurrentKey(null);
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.newsId);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      getNews().then((response) => {
        this.open = true;
        this.title = "添加新闻";
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const newsId = row.newsId || this.ids;
      getNews(newsId).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改新闻";
      });
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.newsId != undefined) {
            updateNews(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addNews(this.form).then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const newsIds = row.newsId || this.ids;
      this.$modal
        .confirm('是否确认删除新闻ID为"' + newsIds + '"的数据项？')
        .then(function () {
          return delNews(newsIds);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "web/news/export",
        {
          ...this.queryParams,
        },
        `news_${new Date().getTime()}.xlsx`
      );
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = "新闻导入";
      this.upload.open = true;
    },
    /** 下载模板操作 */
    importTemplate() {
      this.download(
        "web/news/importTemplate",
        {},
        `news_template_${new Date().getTime()}.xlsx`
      );
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert(
        "<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" +
          response.msg +
          "</div>",
        "导入结果",
        { dangerouslyUseHTMLString: true }
      );
      this.getList();
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    },
  },
};
</script>
<style lang="scss" scoped>
.avatar-uploader {
  width: 178px;
  height: 178px;
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  &:hover {
    border-color: #409eff;
  }
  .img-wrap {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 178px;
    height: 178px;
  .avatar {
    max-width: 178px;
    max-height: 178px;
  }
  }
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
// .avatar {
//   width: 178px;
//   height: 178px;
//   display: block;
// }
</style>
