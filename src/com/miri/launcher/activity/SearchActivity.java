package com.miri.launcher.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.miri.launcher.Constants;
import com.miri.launcher.R;
import com.miri.launcher.adapter.CharacterAdapter;
import com.miri.launcher.utils.Logger;
import com.miri.launcher.utils.NetworkUtil;
import com.miri.launcher.utils.Toolkit;
import com.miri.launcher.view.CustomToast;

/**
 * 影片搜索
 * 
 * @author zengjiantao
 * 
 */
public class SearchActivity extends BaseActivity {

    private final Logger logger = Logger.getLogger();

    private GridView mCharGrid;

    private CharacterAdapter mCharAdapter;

    private Button mDelBtn;

    private Button mOkBtn;

    private EditText mSearchEdt;

    private StringBuffer mSearchBuffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        mSearchBuffer = new StringBuffer();
        initWidget();
        fillWidgetContent();
    }

    private void initWidget() {
        mCharGrid = (GridView) findViewById(R.id.char_grid);
        mCharGrid.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                logger.d("position:" + position);
                String character = mCharAdapter.getItem(position).toString();
                mSearchBuffer.append(character);
                mSearchEdt.setText(mSearchBuffer.toString());
            }
        });

        mDelBtn = (Button) findViewById(R.id.search_del_btn);
        mDelBtn.setOnClickListener(mOnClickListener);
        mOkBtn = (Button) findViewById(R.id.search_ok_btn);
        mOkBtn.setOnClickListener(mOnClickListener);

        mSearchEdt = (EditText) findViewById(R.id.search_edt);
    }

    private void fillWidgetContent() {
        mCharAdapter = new CharacterAdapter(this);
        mCharGrid.setAdapter(mCharAdapter);
    }

    private final OnClickListener mOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
            case R.id.search_del_btn:
                if (mSearchBuffer.length() > 0) {
                    mSearchBuffer.deleteCharAt(mSearchBuffer.length() - 1);
                    mSearchEdt.setText(mSearchBuffer.toString());
                }
                break;
            case R.id.search_ok_btn:
                if (NetworkUtil.isNetworkAvailable(SearchActivity.this)) {
                    String searchKey = mSearchBuffer.toString();
                    logger.d("searchKey:" + searchKey);
                    if (!Toolkit.isEmpty(searchKey)) {
                        Intent intent = new Intent(SearchActivity.this,
                                SearchResultActivity.class);
                        intent.putExtra(Constants.KEY_SEARCH_KEY, searchKey);
                        startActivity(intent);
                    }else{
                        CustomToast.makeText(getApplicationContext(),
                                "请输入关键字！", Toast.LENGTH_LONG).show();
                    }
                } else {
                    CustomToast.makeText(getApplicationContext(),
                            "网络未连接，请检查网络", Toast.LENGTH_LONG).show();
                }
                break;

            default:
                break;
            }
        }
    };
}
