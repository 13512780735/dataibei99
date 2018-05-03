package com.likeit.currenciesapp.ui.chat.redMessage;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.ui.base.Container;
import com.likeit.currenciesapp.ui.chat.SealUserInfoManager;
import com.likeit.currenciesapp.ui.chat.db.GroupMember;
import com.likeit.currenciesapp.ui.chat.server.widget.LoadDialog;
import com.likeit.currenciesapp.utils.StringUtil;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imlib.model.Conversation;

public class RedPacketActivity01 extends Container {
    EditText edAmount;
    EditText et_peak_message;
    TextView tv_amount;
    private String money;
    private String message = null;
    private String targetId;
    private String message1;
    private Conversation.ConversationType conversationType;
    private String type;
    private TextView tv_spell;
    private TextView tv_group_member;
    private EditText et_red_number;
    private LinearLayout ll_common_red, ll_spell_red, ll_number_layout;
    private List<GroupMember> mGroupMember;
    private String rednumber = null;
    private String redType = null;
    private String groupNumber = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_packet4);
        ButterKnife.bind(this);
        targetId = getIntent().getStringExtra("targetId");
        type = getIntent().getStringExtra("type");
        init();
        if ("1".equals(type)) {
            conversationType = Conversation.ConversationType.PRIVATE;
        } else if ("2".equals(type)) {
            conversationType = Conversation.ConversationType.GROUP;
            getGroupMembers();
        }
        initView();

    }

    private void init() {
        edAmount = (EditText) findViewById(R.id.et_amount);//总金额
        et_peak_message = (EditText) findViewById(R.id.et_peak_message);//红包祝福语
        et_red_number = (EditText) findViewById(R.id.et_red_number);//红包个数
        tv_amount = (TextView) findViewById(R.id.tv_amount);//显示红包金额
        tv_spell = (TextView) findViewById(R.id.tv_spell);//拼
        tv_group_member = (TextView) findViewById(R.id.tv_group_member);//群人数
        ll_common_red = (LinearLayout) findViewById(R.id.ll_common_red);//普通红包
        ll_spell_red = (LinearLayout) findViewById(R.id.ll_spell_red);//拼手气红包
        ll_number_layout = (LinearLayout) findViewById(R.id.ll_number_layout);//拼手气红包
    }

    private void getGroupMembers() {
        SealUserInfoManager.getInstance().getGroupMembers(targetId, new SealUserInfoManager.ResultCallback<List<GroupMember>>() {
            @Override
            public void onSuccess(List<GroupMember> groupMembers) {
                LoadDialog.dismiss(mContext);
                if (groupMembers != null && groupMembers.size() > 0) {
                    mGroupMember = groupMembers;
                    initGroupMemberData();
                }
            }

            @Override
            public void onError(String errString) {
                LoadDialog.dismiss(mContext);
            }
        });
    }

    private void initGroupMemberData() {
        if (mGroupMember != null && mGroupMember.size() > 0) {
            setTitle(getString(R.string.group_info) + "(" + mGroupMember.size() + ")");
            groupNumber = String.valueOf(mGroupMember.size());
            tv_group_member.setText("本群共" + groupNumber + "人");
        } else {
            return;
        }
    }

    private void initView() {
        if (conversationType == Conversation.ConversationType.GROUP) {
            ll_common_red.setVisibility(View.VISIBLE);
            redType = "2";
            tv_group_member.setVisibility(View.VISIBLE);
            ll_number_layout.setVisibility(View.VISIBLE);
        } else if (conversationType == Conversation.ConversationType.PRIVATE) {
            ll_common_red.setVisibility(View.GONE);
            tv_group_member.setVisibility(View.GONE);
            ll_number_layout.setVisibility(View.GONE);
        }
        edAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    DecimalFormat df = new DecimalFormat("######0.00");
                    Double s1 = Double.valueOf(s.toString());
                    tv_amount.setText(df.format(s1) + "");
                } else {
                    tv_amount.setText("0.00");
                }

            }
        });

    }

    @OnClick({R.id.backBtn, R.id.btn_putin, R.id.ll_common_red, R.id.ll_spell_red})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                finish();
                break;
            case R.id.btn_putin:
                money = edAmount.getText().toString();
                message = et_peak_message.getText().toString();
                rednumber = et_red_number.getText().toString();
                if (StringUtil.isBlank(message)) {
                    message1 = "恭喜发财，大吉大利！";
                } else message1 = message;
                Log.d("TAG", "message33-->" + message1);
                RedFragment dialog = new RedFragment();
                Bundle bundle = new Bundle();
                bundle.putString("targetId", targetId);
                bundle.putString("message", message1);
                bundle.putString("groupNumber", groupNumber);
                bundle.putString("money", money);
                bundle.putString("rednumber", rednumber);
                bundle.putString("type", type);//分辨群还是私聊
                bundle.putString("redType", String.valueOf(redType));
                dialog.setArguments(bundle);
                dialog.show(this.getSupportFragmentManager(), "RedFragment");
                break;
            case R.id.ll_common_red:
                ll_common_red.setVisibility(View.GONE);
                ll_spell_red.setVisibility(View.VISIBLE);
                tv_spell.setVisibility(View.VISIBLE);
                redType = "1";
                break;
            case R.id.ll_spell_red:
                ll_spell_red.setVisibility(View.GONE);
                tv_spell.setVisibility(View.GONE);
                ll_common_red.setVisibility(View.VISIBLE);
                redType = "2";
                break;
        }
//        Intent intent = getIntent();
//        intent.putExtra("sendSelectedFiles", "11");
//        this.setResult(100, intent);
//        finish();
    }

}
