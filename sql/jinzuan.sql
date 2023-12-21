-- ----------------------------
-- 1、产品分类表
-- ----------------------------
drop table if exists web_products_class;
create table web_products_class (
                          product_class_id                   bigint(20)      not null auto_increment    comment '产品类id',
                          parent_id                          bigint(20)      default 0                  comment '产品父类id',
                          ancestors                          varchar(50)     default ''                 comment '祖级列表',
                          product_class_name                 varchar(30)     default ''                 comment '产品类名称',
                          product_class_name_en              varchar(30)     default ''                 comment '产品类英文名称',
                          order_num                          int(4)          default 0                  comment '显示顺序',
                          leader                             varchar(20)     default null               comment '负责人',
                          phone                              varchar(11)     default null               comment '联系电话',
                          email                              varchar(50)     default null               comment '邮箱',
                          status                             char(1)         default '0'                comment '产品类状态（0正常 1停用）',
                          del_flag                           char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
                          create_by                          varchar(64)     default ''                 comment '创建者',
                          create_time 	                     datetime                                   comment '创建时间',
                          update_by                          varchar(64)     default ''                 comment '更新者',
                          update_time                        datetime                                   comment '更新时间',
                          primary key (dept_id)
) engine=innodb auto_increment=200 comment = '产品分类表';

-- ----------------------------
-- 初始化-产品分类表数据
-- ----------------------------
insert into web_products_class values(100,  0,   '0',          '产品线',     'products',  0, '叶培举', '15888888888', 'y@qq.com', '0', '0', 'admin', sysdate(), '', null);
insert into web_products_class values(101,  100, '0,100',      '金属结合剂',  'products',  1, '叶培举', '15888888888', 'y@qq.com', '0', '0', 'admin', sysdate(), '', null);
insert into web_products_class values(102,  100, '0,100',      '树脂结合剂',  'products',  2, '叶培举', '15888888888', 'y@qq.com', '0', '0', 'admin', sysdate(), '', null);
insert into web_products_class values(103,  100, '0,100',      '电镀',       'products',  3, '叶培举', '15888888888', 'y@qq.com', '0', '0', 'admin', sysdate(), '', null);
insert into web_products_class values(104,  100, '0,100',      '陶瓷结合剂',  'products',  4, '叶培举', '15888888888', 'y@qq.com', '0', '0', 'admin', sysdate(), '', null);