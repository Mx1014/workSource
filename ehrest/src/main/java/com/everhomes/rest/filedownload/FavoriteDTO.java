// @formatter:off
package com.everhomes.rest.filedownload;

import com.everhomes.util.StringHelper;

public class FavoriteDTO {
    CREATE TABLE `eh_file_download_jobs` (
            `id` bigint(20) NOT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `size` bigint(20) DEFAULT NULL,
  `count` bigint(20) DEFAULT NULL,
  `uri` varchar(255) DEFAULT NULL,
  `create_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
