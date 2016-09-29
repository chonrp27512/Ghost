package com.zcy.ghost.vivideo.presenter;

import android.support.annotation.NonNull;

import com.zcy.ghost.vivideo.base.RxPresenter;
import com.zcy.ghost.vivideo.model.bean.Record;
import com.zcy.ghost.vivideo.model.bean.VideoType;
import com.zcy.ghost.vivideo.model.db.RealmHelper;
import com.zcy.ghost.vivideo.presenter.contract.HistoryContract;
import com.zcy.ghost.vivideo.utils.StringUtils;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: CollectionPresenter
 * Creator: cp
 * date: 2016/9/29 12:19
 */
public class HistoryRecordPresenter extends RxPresenter implements HistoryContract.Presenter {
    HistoryContract.View mView;
    private boolean isMine;
    private boolean isSetting;

    public HistoryRecordPresenter(@NonNull HistoryContract.View oneView, boolean isSetting) {
        this.isSetting = isSetting;
        mView = StringUtils.checkNotNull(oneView);
        mView.setPresenter(this);
        EventBus.getDefault().register(this);
        getHistoryData();
    }

    @Subscriber(tag = VideoInfoPresenter.Refresh_History_List)
    public void setData(String tag) {
        isMine = true;
        getHistoryData();
    }

    @Override
    public void getHistoryData() {
        List<Record> records = RealmHelper.getInstance().getRecordList();
        List<VideoType> list = new ArrayList<>();
        VideoType videoType;
        int size = records.size();
        if (isMine) {
            if (size > 3) {
                size = 3;
            }
        }
        if (isSetting) {
            if (size > 3) {
                size = 3;
            }
        }
        for (int i = 0; i < size; i++) {
            Record record = records.get(i);
            videoType = new VideoType();
            videoType.title = record.title;
            videoType.pic = record.pic;
            videoType.dataId = record.getId();
            list.add(videoType);
        }
        mView.showContent(list);
    }

    @Override
    public void delAllHistory() {
        RealmHelper.getInstance().deleteAllRecord();
    }
}