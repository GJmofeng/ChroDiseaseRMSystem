CREATE TABLE chronic_cert (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    cert_no VARCHAR(50) NOT NULL COMMENT '证件号码',
    patient_name VARCHAR(50) NOT NULL COMMENT '患者姓名',
    id_card VARCHAR(18) NOT NULL COMMENT '身份证号',
    disease_type VARCHAR(50) NOT NULL COMMENT '疾病类型',
    issue_date DATE NOT NULL COMMENT '发证日期',
    validity_period DATE NOT NULL COMMENT '有效期',
    status CHAR(1) NOT NULL DEFAULT '1' COMMENT '状态（1：有效，0：已过期）',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by VARCHAR(50) COMMENT '创建人',
    update_by VARCHAR(50) COMMENT '更新人',
    UNIQUE KEY uk_cert_no (cert_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='慢性病证表'; 