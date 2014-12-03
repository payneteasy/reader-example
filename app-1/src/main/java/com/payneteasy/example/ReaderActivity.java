package com.payneteasy.example;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;
import com.payneteasy.android.sdk.reader.CardReaderFactory;
import com.payneteasy.android.sdk.reader.CardReaderInfo;
import com.payneteasy.android.sdk.reader.ICardReaderManager;
import com.payneteasy.example.app1.R;
import com.payneteasy.example.util.ActivityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class ReaderActivity extends Activity  {

    private final static Logger LOG = LoggerFactory.getLogger(ReaderActivity.class);

    private ICardReaderManager cardReaderManager;
    private TextView statusView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // keep screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_reader);

        initUi();

        initReader();

        cardReaderManager.onActivityCreate(this, savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cardReaderManager.onActivityResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cardReaderManager.onActivityPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cardReaderManager.onActivityDestroy();
        cardReaderManager  = null;
    }

    private void initReader() {
        CardReaderInfo cardReader = ActivityUtil.getFirstParameter(this, CardReaderInfo.class);

        BigDecimal amount = new BigDecimal(1);
        String currency = "RUB";

        SimpleCardReaderPresenter presenter = new SimpleCardReaderPresenter(this, statusView);

        cardReaderManager = CardReaderFactory.findManager(this, cardReader, presenter, amount, currency, null);
    }

    private void initUi() {
        statusView = (TextView) findViewById(R.id.statusView);
    }

}
