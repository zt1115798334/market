package com.example.market.common.mysql.service;

import com.example.market.common.base.service.BaseService;
import com.example.market.common.mysql.entity.FileInfo;
import com.example.market.common.utils.module.UploadFile;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/2/21 10:20
 * description:
 */
public interface FileInfoService extends BaseService<FileInfo, Long> {

    FileInfo saveFileInfo(UploadFile uploadFile);

    List<FileInfo> saveFileInfo(List<UploadFile> uploadFile);

    FileInfo findFileInfo(Long id);

}
