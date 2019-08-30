package com.example.market.app.controller;

import com.example.market.app.BaseAutoLoginTest;
import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2019/8/30 17:02
 * description:
 */
public class FruitsTransactionControllerTest extends BaseAutoLoginTest {

    private String url = "app/fruitsTransaction/";
    private Map<String, Object> params = Maps.newHashMap();

    @Test
    public void findFruitsType() {
        url += "findFruitsType";
        postParams(url, params);
    }

    @Test
    public void saveFruitsTransaction() {
    }

    @Test
    public void saveFruitsTransactionImg() {
    }

    @Test
    public void deleteFruitsTransaction() {
    }

    @Test
    public void findFruitsTransaction() {
    }

    @Test
    public void findFruitsTransactionEffective() {
    }

    @Test
    public void findFruitsTransactionUser() {
    }

    @Test
    public void findFruitsTransactionCollection() {
    }

    @Test
    public void enableFruitsTransactionZanOn() {
    }

    @Test
    public void enableFruitsTransactionZanOff() {
    }

    @Test
    public void enableFruitsTransactionCollectionOn() {
    }

    @Test
    public void enableFruitsTransactionCollectionOff() {
    }

    @Test
    public void findFruitsTransactionComment() {
    }

    @Test
    public void findFruitsTransactionCommentCount() {
    }

    @Test
    public void findFruitsTransactionCommentReply() {
    }

    @Test
    public void findFruitsTransactionCommentAndReply() {
    }

    @Test
    public void saveFruitsTransactionComment() {
    }

    @Test
    public void saveFruitsTransactionCommentReplyToComment() {
    }

    @Test
    public void saveFruitsTransactionCommentReplyToReply() {
    }

    @Test
    public void enableFruitsTransactionCommentZanOn() {
    }

    @Test
    public void enableFruitsTransactionCommentZanOff() {
    }
}