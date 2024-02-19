create database if not exists `ry-vue` character set utf8mb4 collate utf8mb4_general_ci;
use `ry-vue`;
-- ----------------------------
-- 1、产品分类表
-- ----------------------------
drop table if exists web_products_class;
create table web_products_class (
                          product_class_id                   bigint(20)      not null auto_increment    comment '产品类id',
                          parent_id                          bigint(20)      default 0                  comment '产品父类id',
                          ancestors                          varchar(50)     default ''                 comment '祖级列表',
                          product_class_name                 varchar(200)     default ''                 comment '产品类名称',
                          product_class_name_en              varchar(200)     default ''                 comment '产品类英文名称',
                          avatar                             varchar(500)    default ''                 comment '产品分类图片',
                          order_num                          int(4)          default 0                  comment '显示顺序',
                          leader                             varchar(500)     default null              comment '产品类别介绍引领',
                          phone                              varchar(11)     default null               comment '联系电话',
                          email                              varchar(50)     default null               comment '邮箱',
                          status                             char(1)         default '0'                comment '产品类状态（0正常 1停用）',
                          del_flag                           char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
                          create_by                          varchar(64)     default ''                 comment '创建者',
                          create_time 	                     datetime                                   comment '创建时间',
                          update_by                          varchar(64)     default ''                 comment '更新者',
                          update_time                        datetime                                   comment '更新时间',
                          primary key (product_class_id)
) engine=innodb auto_increment=200 comment = '产品分类表';

-- ----------------------------
-- 初始化-产品分类表数据
-- ----------------------------
insert into web_products_class values(100,  0,   '0',          '产品分类',    'products', '', 0, '叶培举', '15888888888', 'cartroyal@gmail.com', '0', '0', 'admin', sysdate(), '', null);
insert into web_products_class values(101,  100, '0,100',      '金属结合剂',  'products', '', 1, '叶培举', '15888888888', 'cartroyal@gmail.com', '0', '0', 'admin', sysdate(), '', null);
insert into web_products_class values(102,  100, '0,100',      '树脂结合剂',  'products', '', 2, '叶培举', '15888888888', 'cartroyal@gmail.com', '0', '0', 'admin', sysdate(), '', null);
insert into web_products_class values(103,  100, '0,100',      '电镀',       'products', '', 3, '叶培举', '15888888888', 'cartroyal@gmail.com', '0', '0', 'admin', sysdate(), '', null);
insert into web_products_class values(104,  100, '0,100',      '陶瓷结合剂',  'products', '', 4, '叶培举', '15888888888', 'cartroyal@gmail.com', '0', '0', 'admin', sysdate(), '', null);

-- ----------------------------
-- 2、产品表
-- ----------------------------
drop table if exists web_product;
create table web_product (
                          product_id        bigint(20)      not null auto_increment    comment '产品ID',
                          class_id          bigint(20)      default null               comment '产品类别ID',
                          product_name      varchar(200)     not null                   comment '产品名称',
                          product_name_en   varchar(200)    default ''                 comment '产品名称英文',
                          avatar            varchar(500)    default ''                 comment '产品图片地址',
                          status            char(1)         default '0'                comment '产品状态（0正常 1停用）',
                          del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
                          create_by         varchar(64)     default ''                 comment '创建者',
                          create_time       datetime                                   comment '创建时间',
                          update_by         varchar(64)     default ''                 comment '更新者',
                          update_time       datetime                                   comment '更新时间',
                          remark            varchar(500)    default null               comment '备注',
                          content           longblob        default null               comment '产品介绍富文本',
                          primary key (product_id)
) engine=innodb auto_increment=100 comment = '产品表';

-- ----------------------------
-- 初始化-产品表数据
-- ----------------------------
insert into web_product values(1,  101, '金属结合剂产品A', 'JinShuA', '', '0', '0', '叶培举', sysdate(), '叶培举', sysdate(), '', '这是金属结合剂产品A介绍');

-- ----------------------------
-- 2、官网新闻表
-- ----------------------------
drop table if exists web_news;
create table web_news (
                             news_id            bigint(20)      not null auto_increment    comment '新闻ID',
                             class_id          bigint(20)      default null               comment '新闻类别ID',
                             news_name          varchar(500)    not null                   comment '新闻名称',
                             avatar            varchar(500)    default ''                 comment '新闻图片地址',
                             status            char(1)         default '0'                comment '新闻状态（0正常 1停用）',
                             del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
                             create_by         varchar(64)     default ''                 comment '创建者',
                             create_time       datetime                                   comment '创建时间',
                             update_by         varchar(64)     default ''                 comment '更新者',
                             update_time       datetime                                   comment '更新时间',
                             remark            varchar(500)    default null               comment '备注',
                             content           longblob        default null               comment '新闻介绍富文本',
                             primary key (news_id)
) engine=innodb auto_increment=100 comment = '新闻表';

-- ----------------------------
-- 初始化-新闻表数据
-- ----------------------------
insert into web_news values(1,  101, '金属结合剂新闻A', '', '0', '0', '叶培举', sysdate(), '叶培举', sysdate(), '', '这是金属结合剂新闻A介绍');