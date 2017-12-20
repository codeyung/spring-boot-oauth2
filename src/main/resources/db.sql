CREATE TABLE `OAuth2User` (
  `id` int(11) NOT NULL COMMENT '用户 Id',
  `username` varchar(128) NOT NULL DEFAULT '' COMMENT '名称',
  `password` varchar(128) NOT NULL COMMENT '密码',
  `client_id` varchar(128) NOT NULL DEFAULT '' COMMENT 'appkey',
  `client_secret` varchar(128) NOT NULL DEFAULT '' COMMENT 'appsecret',
  `redirect_uri` varchar(128) NOT NULL DEFAULT '' COMMENT '返回 uri',
  `memo` varchar(128) NOT NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `OAuth2UserScope` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `ouId` int(11) NOT NULL COMMENT '用户 id',
  `sId` int(11) NOT NULL COMMENT '范围 id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `Scope` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `scope` varchar(30) NOT NULL DEFAULT '' COMMENT '范围',
  `memo` varchar(150) NOT NULL DEFAULT '' COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `OpenAPI` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `sId` int(11) NOT NULL COMMENT '范围',
  `method` varchar(10) NOT NULL DEFAULT '' COMMENT '方法',
  `url` varchar(100) NOT NULL DEFAULT '' COMMENT '地址',
  `memo` varchar(100) NOT NULL DEFAULT '' COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;