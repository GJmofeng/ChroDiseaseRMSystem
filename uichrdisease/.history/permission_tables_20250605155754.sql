-- 角色表
CREATE TABLE IF NOT EXISTS `sys_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(50) NOT NULL COMMENT '角色名称',
  `role_code` varchar(50) NOT NULL COMMENT '角色编码',
  `description` varchar(255) DEFAULT NULL COMMENT '角色描述',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统角色表';

-- 菜单表
CREATE TABLE IF NOT EXISTS `sys_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父菜单ID',
  `menu_name` varchar(50) NOT NULL COMMENT '菜单名称',
  `path` varchar(100) DEFAULT NULL COMMENT '菜单路径',
  `component` varchar(100) DEFAULT NULL COMMENT '组件路径',
  `icon` varchar(50) DEFAULT NULL COMMENT '菜单图标',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `hidden` tinyint(1) DEFAULT '0' COMMENT '是否隐藏（0显示 1隐藏）',
  `type` tinyint(1) DEFAULT '1' COMMENT '菜单类型（1目录 2菜单 3按钮）',
  `permission` varchar(100) DEFAULT NULL COMMENT '权限标识',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统菜单表';

-- 角色菜单关联表
CREATE TABLE IF NOT EXISTS `sys_role_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_menu` (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单关联表';

-- 用户角色关联表
CREATE TABLE IF NOT EXISTS `sys_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_role` (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- 插入初始菜单数据
INSERT INTO `sys_menu` (`parent_id`, `menu_name`, `path`, `component`, `icon`, `sort`, `hidden`, `type`, `permission`) VALUES
(0, '系统管理', '/system', '', 'fas fa-cog', 1, 0, 1, 'system'),
(1, '用户管理', '/main/user-manage', 'UserManage', 'fas fa-users', 1, 0, 2, 'system:user'),
(1, '权限管理', '/main/permission-manage', 'PermissionManage', 'fas fa-shield-alt', 2, 0, 2, 'system:permission'),
(1, '行政区管理', '/main/region-manage', 'RegionManage', 'fas fa-map-marked-alt', 3, 0, 2, 'system:region'),
(1, '慢病信息管理', '/main/disease-info', 'DiseaseInfo', 'fas fa-bell', 4, 0, 2, 'system:disease'),
(1, '慢病政策管理', '/main/policy-manage', 'PolicyManagement', 'fas fa-file-medical', 5, 0, 2, 'system:policy'),
(0, '业务管理', '/business', '', 'fas fa-briefcase', 2, 0, 1, 'business'),
(7, '参合信息录入', '/main/participation', 'ParticipationInfo', 'fas fa-user-plus', 1, 0, 2, 'business:participation'),
(7, '慢性病证管理', '/main/chronic-cert', 'ChronicCertManage', 'fas fa-id-card', 2, 0, 2, 'business:chronic'),
(7, '参合缴费登记', '/main/reimbursement-form', 'ReimbursementForm', 'fas fa-money-check-alt', 3, 0, 2, 'business:reimbursement:form'),
(7, '报销记录', '/main/reimbursement-list', 'ReimbursementList', 'fas fa-file-invoice-dollar', 4, 0, 2, 'business:reimbursement:list'),
(7, '报销审核', '/main/reimbursement-audit', 'ReimbursementAudit', 'fas fa-clipboard-check', 5, 0, 2, 'business:reimbursement:audit'),
(7, '报销汇款', '/main/reimbursement-remit', 'ReimbursementRemit', 'fas fa-money-bill-wave', 6, 0, 2, 'business:reimbursement:remit'),
(0, '统计分析', '/statistics', '', 'fas fa-chart-line', 3, 0, 1, 'statistics'),
(14, '按年份统计', '/main/yearly-statistics', 'YearlyStatistics', 'fas fa-chart-bar', 1, 0, 2, 'statistics:yearly'),
(14, '按月份统计', '/main/monthly-statistics', 'MonthlyStatistics', 'fas fa-chart-area', 2, 0, 2, 'statistics:monthly');

-- 插入初始角色数据
INSERT INTO `sys_role` (`role_name`, `role_code`, `description`) VALUES
('超级管理员', 'ROLE_ADMIN', '系统超级管理员，拥有所有权限'),
('县合管办领导', 'ROLE_LEADER', '县级合管办领导，拥有查看和审批权限'),
('县合管办经办人', 'ROLE_OPERATOR', '县级合管办经办人，负责日常业务办理'),
('乡镇农合经办人', 'ROLE_TOWN_OPERATOR', '乡镇农合经办人，负责基层业务办理');

-- 为超级管理员分配所有菜单权限
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)
SELECT 1, id FROM `sys_menu`; 