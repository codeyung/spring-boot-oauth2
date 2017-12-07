CREATE TABLE `Account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(32) NOT NULL DEFAULT '' COMMENT '用户名',
  `password` varchar(32) NOT NULL DEFAULT '' COMMENT '密码',
  `memo` varchar(32) NOT NULL DEFAULT '' COMMENT '备注',
  `createTime` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `AccountScope` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `aId` int(11) NOT NULL COMMENT '用户 id',
  `sId` int(11) NOT NULL COMMENT '范围 id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `OAuth2Client` (
  `aId` int(11) NOT NULL COMMENT '用户 Id',
  `client_id` varchar(128) NOT NULL DEFAULT '' COMMENT 'appkey',
  `client_secret` varchar(128) NOT NULL DEFAULT '' COMMENT 'appsecret',
  `redirect_uri` varchar(128) NOT NULL DEFAULT '' COMMENT '返回 uri',
  PRIMARY KEY (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `OpenAPI` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `sId` int(11) NOT NULL COMMENT '范围',
  `method` varchar(10) NOT NULL DEFAULT '' COMMENT '方法',
  `url` varchar(100) NOT NULL DEFAULT '' COMMENT '地址',
  `memo` varchar(100) NOT NULL DEFAULT '' COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `Scope` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `scope` varchar(30) NOT NULL DEFAULT '' COMMENT '范围',
  `memo` varchar(150) NOT NULL DEFAULT '' COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;