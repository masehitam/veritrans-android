package com.midtrans.sdk.ui.views.ebanking.bca_klikpay;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.midtrans.sdk.core.models.snap.ebanking.bcaklikpay.BcaKlikpayPaymentResponse;
import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.abtracts.BasePaymentActivity;
import com.midtrans.sdk.ui.constants.Constants;
import com.midtrans.sdk.ui.constants.PaymentType;
import com.midtrans.sdk.ui.models.PaymentResult;
import com.midtrans.sdk.ui.utils.UiUtils;
import com.midtrans.sdk.ui.views.webpayment.PaymentWebActivity;

/**
 * Created by rakawm on 4/10/17.
 */

public class BcaKlikpayActivity extends BasePaymentActivity implements BcaKlikpayView {
    private BcaKlikpayPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bca_klikpay);
        initPresenter();
    }

    private void initPresenter() {
        presenter = new BcaKlikpayPresenter(this);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    private void showProgressDialog(String message) {
        UiUtils.showProgressDialog(this, message, false);
    }

    private void dismissProgressDialog() {
        UiUtils.hideProgressDialog();
    }

    @Override
    public void onBcaKlikpaySuccess(BcaKlikpayPaymentResponse paymentResponse) {
        dismissProgressDialog();
        String url = paymentResponse.redirectUrl;
        startWebPayment(url);
    }

    @Override
    public void onBcaKlikpayFailure(BcaKlikpayPaymentResponse paymentResponse) {
        dismissProgressDialog();
        UiUtils.showToast(this, getString(R.string.message_payment_failed_general));
    }

    @Override
    public void onBcaKlikpayError(String message) {
        dismissProgressDialog();
        UiUtils.showToast(this, getString(R.string.message_payment_failed_general));
    }

    private void startWebPayment(String url) {
        Intent intent = new Intent(this, PaymentWebActivity.class);
        intent.putExtra(Constants.WEB_VIEW_URL, url);
        intent.putExtra(Constants.WEB_VIEW_PARAM_TYPE, PaymentType.BCA_KLIKPAY);
        startActivityForResult(intent, Constants.INTENT_CODE_WEB_PAYMENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.INTENT_CODE_WEB_PAYMENT) {
                finishPayment(resultCode, presenter.getPaymentResult());
            }
        }
    }

    private void finishPayment(int resultCode, PaymentResult<BcaKlikpayPaymentResponse> paymentResult) {
        Intent data = new Intent();
        data.putExtra(Constants.PAYMENT_RESULT, paymentResult);
        setResult(resultCode, data);
        finish();
    }

    @Override
    protected void startPayment() {
        showProgressDialog(getString(R.string.processing_payment));
        presenter.startPayment();
    }

    @Override
    protected boolean validatePayment() {
        return true;
    }
}
