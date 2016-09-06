package id.co.veritrans.sdk.sample;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import id.co.veritrans.sdk.coreflow.callback.TransactionCallback;
import id.co.veritrans.sdk.coreflow.core.VeritransSDK;
import id.co.veritrans.sdk.coreflow.models.TransactionResponse;

/**
 * Created by ziahaqi on 8/3/16.
 */
public class BankTransferPaymentActivity extends AppCompatActivity{

    public static final String TRANSFER_TYPE = "transfer_type";
    ProgressDialog dialog;
    private Button buttonPay;
    private String sampleEmail = "sample@email.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_payment_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.title_activity_bank_transfer));

        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setMessage("Loading");

        buttonPay = (Button) findViewById(R.id.btn_payment);
        buttonPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                String type = getIntent().getStringExtra(TRANSFER_TYPE);
                if(type.equals(getString(R.string.label_bank_transfer_bca))){
                    VeritransSDK.getInstance().snapPaymentUsingBankTransferBCA(
                            VeritransSDK.getInstance().readAuthenticationToken(),
                            sampleEmail, new TransactionCallback() {
                                @Override
                                public void onSuccess(TransactionResponse response) {
                                    actionTransactionSuccess(response);
                                }

                                @Override
                                public void onFailure(TransactionResponse response, String reason) {
                                    actionTransactionFailure(response, reason);
                                }

                                @Override
                                public void onError(Throwable error) {
                                    actionTransactionError(error);
                                }
                            });
                }else if(type.equals(getString(R.string.label_bank_transfer_permata))){
                    VeritransSDK.getInstance().snapPaymentUsingBankTransferPermata(
                            VeritransSDK.getInstance().readAuthenticationToken(),
                            sampleEmail, new TransactionCallback() {
                                @Override
                                public void onSuccess(TransactionResponse response) {
                                    actionTransactionSuccess(response);
                                }

                                @Override
                                public void onFailure(TransactionResponse response, String reason) {
                                    actionTransactionFailure(response, reason);
                                }

                                @Override
                                public void onError(Throwable error) {
                                    actionTransactionError(error);
                                }
                            });
                }else if (type.equals(getString(R.string.name_bank_transfer_mandiri))){
                    Toast.makeText(BankTransferPaymentActivity.this, getString(R.string.payment_type_unsupported), Toast.LENGTH_SHORT).show();

                }else if(type.equals(getString(R.string.name_bank_transfer_others))){
                    Toast.makeText(BankTransferPaymentActivity.this, getString(R.string.payment_type_unsupported), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void actionTransactionError(Throwable error) {
        dialog.dismiss();
        AlertDialog dialog = new AlertDialog.Builder(BankTransferPaymentActivity.this)
                .setMessage("Unknown error: " + error.getMessage())
                .create();
        dialog.show();
    }

    private void actionTransactionFailure(TransactionResponse response, String reason) {
        dialog.dismiss();
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(reason)
                .create();
        dialog.show();
    }

    private void actionTransactionSuccess(TransactionResponse response) {
        // Handle success transaction
        dialog.dismiss();
        Toast.makeText(this, "transaction successfull (" + response.getStatusMessage() + ")", Toast.LENGTH_LONG).show();
        setResult(RESULT_OK);
        finish();
    }

}