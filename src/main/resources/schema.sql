CREATE TABLE IF NOT EXISTS `user`
(
    `id`            BIGINT UNSIGNED     NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `create_time`   datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `username`      VARCHAR(255)        NOT NULL DEFAULT '' COMMENT '用户名',
    `email`         VARCHAR(255) UNIQUE NOT NULL DEFAULT '' COMMENT '用户邮箱',
    `password`      CHAR(65)            NOT NULL DEFAULT '' COMMENT '用户密码',
    `enabled`       tinyint unsigned    NOT NULL,
    `token_expired` tinyint unsigned    NOT NULL,
    PRIMARY KEY pk_id (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户数据表';

CREATE TABLE `image_metadata`
(
    `id`          bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `user_id`     bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '图片上传用户 ID',
    `image_url`   varchar(255)        NOT NULL DEFAULT '' COMMENT '图片路径',
    `origin_name` varchar(255)        NOT NULL DEFAULT '' COMMENT '图片原始文件名',
    `create_time` datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY pk_id (`id`),
    INDEX idx_user_id (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='图片元数据表';

CREATE TABLE `file_journal`
(
    `id`          bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `user_id`     bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '用户 ID',
    `journal_id`  bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '日志 ID',
    `path`        varchar(255)        NOT NULL DEFAULT '' COMMENT '文件路径',
    `event_type`  TINYINT(3)          NOT NULL DEFAULT '0' COMMENT '事件类型',
    `description` varchar(255)        NOT NULL DEFAULT '' COMMENT '额外信息',
    `create_time` datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY pk_id (`id`),
    INDEX idx_user_id (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='文件日志表';

CREATE TABLE `file_object`
(
    `id`           bigint unsigned  NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `user_id`      bigint unsigned  NOT NULL DEFAULT '0' COMMENT '用户 ID',
    `path`         varchar(255)     NOT NULL DEFAULT '' COMMENT '文件路径',
    `is_directory` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '是否目录 0否 1是',
    `version`      int              NOT NULL DEFAULT '0' COMMENT '文件版本',
    `create_time`  datetime         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  datetime         NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY pk_id (`id`),
    INDEX idx_user_id (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='文件日志表';

CREATE TABLE `version_control`
(
    `id`          bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `user_id`     bigint unsigned NOT NULL DEFAULT '0' COMMENT '用户 ID',
    `name`        varchar(255)    NOT NULL DEFAULT '0' COMMENT '版本名',
    `create_time` datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY pk_id (`id`),
    INDEX idx_user_id (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='版本控制表';