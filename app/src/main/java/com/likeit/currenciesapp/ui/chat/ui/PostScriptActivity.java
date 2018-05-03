package com.likeit.currenciesapp.ui.chat.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.ui.chat.server.network.http.HttpException;
import com.likeit.currenciesapp.ui.chat.server.response.FriendInvitationResponse;
import com.likeit.currenciesapp.ui.chat.server.utils.NToast;
import com.likeit.currenciesapp.ui.chat.server.widget.LoadDialog;
import com.likeit.currenciesapp.ui.chat.ui.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostScriptActivity extends BaseActivity implements View.OnClickListener {

    private static final int ADD_FRIEND = 11;

    /*    @BindView(R.id.tv_header)
        TextView tvHeader;
        @BindView(R.id.tv_right)
        TextView tvRight;*/
    private String mUserId;
    @BindView(R.id.etMsg)
    EditText mEtMsg;
    @BindView(R.id.ibClear)
    ImageButton mIbClear;
    private String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_script);
        ButterKnife.bind(this);
        setTitle("轉轉寳");
        mHeadRightText.setVisibility(View.VISIBLE);
        mHeadRightText.setText("發送");
        mHeadRightText.setOnClickListener(this);
        mUserId = getIntent().getStringExtra("userId");
        mIbClear.setOnClickListener(v -> mEtMsg.setText(""));
        // initView();
    }

 /*   private void initView() {
        tvHeader.setText("大台北外匯");
        tvRight.setText("發送");

    }*/


/*    @OnClick({R.id.backBtn, R.id.tv_right})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
            case R.id.tv_right:
                msg = mEtMsg.getText().toString().trim();
                request(ADD_FRIEND);
                break;
        }
    }*/

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();

    }


    @Override
    public Object doInBackground(int requestCode, String id) throws HttpException {
        switch (requestCode) {
            case ADD_FRIEND:
                return action.sendFriendInvitation(mUserId, msg);
        }
        return super.doInBackground(requestCode, id);
    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        if (result != null) {
            switch (requestCode) {
                case ADD_FRIEND:
                    FriendInvitationResponse fres = (FriendInvitationResponse) result;
                    if (fres.getCode() == 200) {
                        NToast.shortToast(mContext, getString(R.string.request_success));
                        LoadDialog.dismiss(mContext);
                        finish();
                    } else {
                        NToast.shortToast(mContext, "请求失败 错误码:" + fres.getCode());
                        LoadDialog.dismiss(mContext);
                    }
                    break;
            }
        }
    }

    @Override
    public void onFailure(int requestCode, int state, Object result) {
        switch (requestCode) {
            case ADD_FRIEND:
                NToast.shortToast(mContext, "你们已经是好友");
                LoadDialog.dismiss(mContext);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        msg = mEtMsg.getText().toString().trim();
        request(ADD_FRIEND);
    }
}
