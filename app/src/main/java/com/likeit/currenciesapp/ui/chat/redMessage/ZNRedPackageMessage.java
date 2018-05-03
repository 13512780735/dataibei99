package com.likeit.currenciesapp.ui.chat.redMessage;

import android.os.Parcel;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.common.ParcelUtils;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;

/**
 * 自定义融云IM消息类
 *
 * @author lsy
 * @date 2015-11-19
 * <p>
 * MessageTag 中 flag 中参数的含义：
 * 1.NONE，空值，不表示任何意义.在会话列表不会显示出来。
 * 2.ISPERSISTED，消息需要被存储到消息历史记录。
 * 3.ISCOUNTED，消息需要被记入未读消息数。
 * <p>
 * value：消息对象名称。
 * 请不要以 "RC:" 开头， "RC:" 为官方保留前缀。
 */

@MessageTag(value = "RCD:redPackage", flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED)
public class ZNRedPackageMessage extends MessageContent {

    private String userid;
    private String content;
    private String money;
    private String extra;
    private String group_id;
    private String red_id;


    public ZNRedPackageMessage() {

    }

    public static ZNRedPackageMessage obtain(String userid, String content, String money, String extra, String group_id, String red_id) {
        ZNRedPackageMessage rongRedPacketMessage = new ZNRedPackageMessage();
        rongRedPacketMessage.userid = userid;
        rongRedPacketMessage.content = content;
        rongRedPacketMessage.money = money;
        rongRedPacketMessage.extra = extra;
        rongRedPacketMessage.group_id = group_id;
        rongRedPacketMessage.red_id = red_id;
        return rongRedPacketMessage;
    }

    // 给消息赋值。
    public ZNRedPackageMessage(byte[] data) {

        try {
            String jsonStr = new String(data, "UTF-8");
            JSONObject jsonObj = new JSONObject(jsonStr);
            setUserid(jsonObj.getString("userid"));
            setContent(jsonObj.getString("content"));
            setMoney(jsonObj.getString("money"));
            setExtra(jsonObj.getString("extra"));
            setExtra(jsonObj.getString("group_id"));
            setExtra(jsonObj.getString("red_id"));
            if (jsonObj.has("user")) {
                setUserInfo(parseJsonToUserInfo(jsonObj.getJSONObject("user")));
            }
        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
        } catch (UnsupportedEncodingException e1) {

        }
    }

    /**
     * 构造函数。
     *
     * @param in 初始化传入的 Parcel。
     */
    public ZNRedPackageMessage(Parcel in) {
        setUserid(ParcelUtils.readFromParcel(in));
        setContent(ParcelUtils.readFromParcel(in));
        setMoney(ParcelUtils.readFromParcel(in));
        setExtra(ParcelUtils.readFromParcel(in));
        setGroup_id(ParcelUtils.readFromParcel(in));
        setRed_id(ParcelUtils.readFromParcel(in));
        setUserInfo(ParcelUtils.readFromParcel(in, UserInfo.class));
    }

    /**
     * 读取接口，目的是要从Parcel中构造一个实现了Parcelable的类的实例处理。
     */
    public static final Creator<ZNRedPackageMessage> CREATOR = new Creator<ZNRedPackageMessage>() {

        @Override
        public ZNRedPackageMessage createFromParcel(Parcel source) {
            return new ZNRedPackageMessage(source);
        }

        @Override
        public ZNRedPackageMessage[] newArray(int size) {
            return new ZNRedPackageMessage[size];
        }
    };

    /**
     * 描述了包含在 Parcelable 对象排列信息中的特殊对象的类型。
     *
     * @return 一个标志位，表明Parcelable对象特殊对象类型集合的排列。
     */
    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * 将类的数据写入外部提供的 Parcel 中。
     *
     * @param dest  对象被写入的 Parcel。
     * @param flags 对象如何被写入的附加标志。
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // 这里可继续增加你消息的属性
        ParcelUtils.writeToParcel(dest, userid);
        ParcelUtils.writeToParcel(dest, content);
        ParcelUtils.writeToParcel(dest, money);
        ParcelUtils.writeToParcel(dest, extra);
        ParcelUtils.writeToParcel(dest, group_id);
        ParcelUtils.writeToParcel(dest, red_id);
        ParcelUtils.writeToParcel(dest, getUserInfo());

    }

    /**
     * 将消息属性封装成 json 串，再将 json 串转成 byte 数组，该方法会在发消息时调用
     */
    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();
        try {

            jsonObj.put("userid", userid);
            jsonObj.put("content", content);
            jsonObj.put("money", money);
            jsonObj.put("extra", extra);
            jsonObj.put("group_id", group_id);
            jsonObj.put("red_id", red_id);
            // jsonObj.put("money", money);

            if (getJSONUserInfo() != null)
                jsonObj.putOpt("user", getJSONUserInfo());

        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getRed_id() {
        return red_id;
    }

    public void setRed_id(String red_id) {
        this.red_id = red_id;
    }

}