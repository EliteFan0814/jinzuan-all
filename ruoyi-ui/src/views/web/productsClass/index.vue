<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      size="small"
      :inline="true"
      v-show="showSearch"
    >
      <el-form-item label="类别名称" prop="productClassName">
        <el-input
          v-model="queryParams.productClassName"
          placeholder="请输入类别名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="类别英文名称" prop="productClassNameEn">
        <el-input
          v-model="queryParams.productClassNameEn"
          placeholder="请输入类别英文名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select
          v-model="queryParams.status"
          placeholder="类别状态"
          clearable
        >
          <el-option
            v-for="dict in dict.type.sys_normal_disable"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
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
      <!-- <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['web:productsClass:add']"
          >新增</el-button
        >
      </el-col> -->
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-sort"
          size="mini"
          @click="toggleExpandAll"
          >展开/折叠</el-button
        >
      </el-col>
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <el-table
      v-if="refreshTable"
      v-loading="loading"
      :data="productsClassList"
      row-key="productClassId"
      :default-expand-all="isExpandAll"
      :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
    >
      <el-table-column
        prop="productClassName"
        label="类别名称"
        width="260"
      ></el-table-column>
      <el-table-column
        prop="productClassNameEn"
        label="类别英文名称"
        width="260"
      ></el-table-column>
      <el-table-column
        prop="orderNum"
        label="排序"
        width="200"
      ></el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template slot-scope="scope">
          <dict-tag
            :options="dict.type.sys_normal_disable"
            :value="scope.row.status"
          />
        </template>
      </el-table-column>
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        width="200"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['web:productsClass:edit']"
            >修改</el-button
          >
          <el-button
            v-if="!scope.row.parentId"
            size="mini"
            type="text"
            icon="el-icon-plus"
            @click="handleAdd(scope.row)"
            v-hasPermi="['web:productsClass:add']"
            >新增</el-button
          >
          <el-button
            v-if="scope.row.parentId != 0"
            size="mini"
            type="text"
            icon="el-icon-delete"
            style="color: red"
            @click="handleDelete(scope.row)"
            v-hasPermi="['web:productsClass:remove']"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改类别对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="110px">
        <el-row>
          <el-col :span="24" v-if="form.parentId !== 0">
            <el-form-item label="上级类别" prop="parentId">
              <treeselect
                disabled
                v-model="form.parentId"
                :options="productsClassOptions"
                :normalizer="normalizer"
                placeholder="选择上级类别"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="类别名称" prop="productClassName">
              <el-input
                v-model="form.productClassName"
                placeholder="请输入类别名称"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="类别英文名称" prop="productClassNameEn">
              <el-input
                v-model="form.productClassNameEn"
                placeholder="请输入类别英文名称"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示排序" prop="orderNum">
              <el-input-number
                v-model="form.orderNum"
                controls-position="right"
                :min="0"
              />
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
            <el-form-item label="品类介绍" prop="leader">
              <el-input
                type="textarea"
                v-model="form.leader"
                :rows="2"
                placeholder="请输入品类介绍"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="phone">
              <el-input
                v-model="form.phone"
                placeholder="请输入联系电话"
                maxlength="11"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input
                v-model="form.email"
                placeholder="请输入邮箱"
                maxlength="50"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="类别状态">
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
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  upImg,
  listProductsClass,
  getProductsClass,
  delProductsClass,
  addProductsClass,
  updateProductsClass,
  listProductsClassExcludeChild,
} from "@/api/web/productsClass";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "ProductsClass",
  dicts: ["sys_normal_disable"],
  components: { Treeselect },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 图片上传loading
      imageLoading: false,
      // 显示搜索条件
      showSearch: true,
      // 表格树数据
      productsClassList: [],
      // 类别树选项
      productsClassOptions: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否展开，默认全部展开
      isExpandAll: true,
      // 重新渲染表格状态
      refreshTable: true,
      // 查询参数
      queryParams: {
        productClassName: undefined,
        productClassNameEn: undefined,
        status: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        parentId: [
          { required: true, message: "上级类别不能为空", trigger: "blur" },
        ],
        productClassName: [
          { required: true, message: "类别名称不能为空", trigger: "blur" },
        ],
        productClassNameEn: [
          { required: true, message: "类别英文名称不能为空", trigger: "blur" },
        ],
        orderNum: [
          { required: true, message: "显示排序不能为空", trigger: "blur" },
        ],
        avatar: [{ required: true, message: "图片不能为空", trigger: "blur" }],
        leader: [
          { required: true, message: "品类介绍不能为空", trigger: "blur" },
        ],
        email: [
          {
            type: "email",
            message: "请输入正确的邮箱地址",
            trigger: ["blur", "change"],
          },
        ],
        phone: [
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号码",
            trigger: "blur",
          },
        ],
      },
      imgFileList: [],
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询类别列表 */
    getList() {
      this.loading = true;
      listProductsClass(this.queryParams).then((response) => {
        this.productsClassList = this.handleTree(
          response.data,
          "productClassId"
        );
        this.loading = false;
      });
    },
    /** 转换类别数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.productClassId,
        label: node.productClassName,
        children: node.children,
      };
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        productClassId: undefined,
        parentId: undefined,
        productClassName: undefined,
        orderNum: undefined,
        leader: undefined,
        phone: undefined,
        email: undefined,
        status: "0",
        avatar: undefined,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList();
    },

    handleUpImg(fileInfo) {
      this.imgFileList = [];
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
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleAdd(row) {
      this.reset();
      if (row != undefined) {
        this.form.parentId = row.productClassId;
      }
      this.open = true;
      this.title = "添加类别";
      listProductsClass().then((response) => {
        this.productsClassOptions = this.handleTree(
          response.data,
          "productClassId"
        );
      });
    },
    /** 展开/折叠操作 */
    toggleExpandAll() {
      this.refreshTable = false;
      this.isExpandAll = !this.isExpandAll;
      this.$nextTick(() => {
        this.refreshTable = true;
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      getProductsClass(row.productClassId).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改类别";
        listProductsClassExcludeChild(row.productClassId).then((response) => {
          this.productsClassOptions = this.handleTree(
            response.data,
            "productClassId"
          );
          if (this.productsClassOptions.length == 0) {
            const noResultsOptions = {
              productClassId: this.form.parentId,
              productClassName: this.form.parentName,
              children: [],
            };
            this.productsClassOptions.push(noResultsOptions);
          }
        });
      });
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.productClassId != undefined) {
            updateProductsClass(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addProductsClass(this.form).then((response) => {
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
      this.$modal
        .confirm('是否确认删除名称为"' + row.productClassName + '"的数据项？')
        .then(function () {
          return delProductsClass(row.productClassId);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
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
