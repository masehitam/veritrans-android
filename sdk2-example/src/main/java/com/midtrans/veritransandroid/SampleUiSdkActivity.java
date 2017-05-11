package com.midtrans.veritransandroid;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.midtrans.sdk.core.models.BankType;
import com.midtrans.sdk.core.models.Channel;
import com.midtrans.sdk.core.models.merchant.Address;
import com.midtrans.sdk.core.models.merchant.CheckoutOrderDetails;
import com.midtrans.sdk.core.models.merchant.CheckoutTokenRequest;
import com.midtrans.sdk.core.models.merchant.CreditCard;
import com.midtrans.sdk.core.models.merchant.CustomerDetails;
import com.midtrans.sdk.core.models.merchant.ItemDetails;
import com.midtrans.sdk.core.models.papi.CardTokenRequest;
import com.midtrans.sdk.core.models.snap.card.Installment;
import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.MidtransUiCallback;
import com.midtrans.sdk.ui.constants.PaymentStatus;
import com.midtrans.sdk.ui.constants.PaymentType;
import com.midtrans.sdk.ui.models.PaymentResult;
import com.midtrans.sdk.ui.utils.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by ziahaqi on 3/5/17.
 */

public class SampleUiSdkActivity extends AppCompatActivity {

    private RadioButton rbCardNormal, rbCardTwoclick, rbCardOneclick, rb3dsSecure, rb3dsNotSecure,
            rbBankBni, rbBankMandiri, rbBankBca, rbBankMyBank, rbBankBri, preAuthActive;
    private EditText etCustomField1, etCustomField2, etCustomField3;

    private Button buttonUiSdk;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui_sdk);
        rbCardNormal = (RadioButton) findViewById(R.id.radio_card_normal);
        rbCardOneclick = (RadioButton) findViewById(R.id.radio_card_one_click);
        rbCardTwoclick = (RadioButton) findViewById(R.id.radio_card_two_click);
        rb3dsSecure = (RadioButton) findViewById(R.id.radio_secure);
        rb3dsNotSecure = (RadioButton) findViewById(R.id.radio_not_secure);
        rbBankBni = (RadioButton) findViewById(R.id.radio_bni);
        rbBankBca = (RadioButton) findViewById(R.id.radio_bca);
        rbBankBri = (RadioButton) findViewById(R.id.radio_bri);
        rbBankMandiri = (RadioButton) findViewById(R.id.radio_mandiri);
        rbBankMyBank = (RadioButton) findViewById(R.id.radio_maybank);
        preAuthActive = (RadioButton) findViewById(R.id.radio_pre_auth_active);
        etCustomField1 = (EditText) findViewById(R.id.custom_field1);
        etCustomField2 = (EditText) findViewById(R.id.custom_field2);
        etCustomField3 = (EditText) findViewById(R.id.custom_field3);
        buttonUiSdk = (Button) findViewById(R.id.show_ui_flow);

        setupViews();
    }

    private void setupViews() {
        buttonUiSdk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkout();
            }
        });

        rbCardOneclick.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    rb3dsSecure.setChecked(true);
                }
            }
        });


    }

    private void checkout() {
        MidtransUi.getInstance().runUiSdk(this, MyApp.CHECKOUT_URL, initialiseCheckoutTokenRequest(), new MidtransUiCallback() {
            @Override
            public void onFinished(PaymentResult result) {
                Logger.d("onFinished:", "result: " + result.getPaymentStatus());
                if (result.isCanceled()) {
                    // Payment is canceled
                    Toast.makeText(SampleUiSdkActivity.this, "CANCELED", Toast.LENGTH_SHORT).show();
                } else {
                    // Payment is finished
                    // Check the status
                    String status = result.getPaymentStatus();
                    switch (status) {
                        case PaymentStatus.PENDING:
                            // Pending status for VA (BCA, Mandiri, Permata), KlikBCA, Indomaret and Kioson

                            // For E-Banking transaction such as BCAKlikpay, CIMB Clicks, BRI E-Pay, Mandiri E-Cash
                            // Merchant need to get latest transaction status using Midtrans API.
                            break;
                        case PaymentStatus.CHALLENGE:
                            // Credit debit card only
                            // Transaction is
                            break;
                        case PaymentStatus.DENY:
                            // Credit card only
                            // Denied by FDS or bank
                            break;
                        case PaymentStatus.INVALID:
                            // Invalid transaction
                            // No error message
                            break;
                        case PaymentStatus.FAILED:
                            // Error transaction
                            // Internal error detected
                            break;
                    }
                }
                Toast.makeText(SampleUiSdkActivity.this, result.getPaymentStatus(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private CheckoutTokenRequest initialiseCheckoutTokenRequest() {
        // Setup order details
        CheckoutOrderDetails checkoutOrderDetails = new CheckoutOrderDetails(UUID.randomUUID().toString(), 3000);
        // Setup customer details
        CustomerDetails customerDetails = new CustomerDetails(
                "Raka", "Mogandhi", "rakamogandhi@hotmail.com", "082140518011",
                new Address("Raka", "Mogandhi", "Jalan Wirajaya 312", "Yogyakarta", "55283", "082140518011", "IDN"),
                new Address("Raka", "Mogandhi", "Jalan Wirajaya 312", "Yogyakarta", "55283", "082140518011", "IDN"));

        // Setup item details
        List<ItemDetails> itemDetailsList = new ArrayList<>();
        ItemDetails itemDetails = new ItemDetails("item1", 1000, 1, "Shoe 1");
        ItemDetails itemDetails2 = new ItemDetails("item1", 2000, 1, "Shoe 2");
        itemDetailsList.add(itemDetails);
        itemDetailsList.add(itemDetails2);

        //creditcard properties
        // Create creditcard options for payment
        // noted : channel migs is needed if bank type is BCA, BRI or MyBank

        CreditCard creditCard = new CreditCard();

        if (rbBankMandiri.isChecked()) {
            // Set bank to Mandiri
            creditCard.setBank(BankType.MANDIRI);
        } else if (rbBankBni.isChecked()) {
            // Set bank to BNI
            creditCard.setBank(BankType.BNI);
        } else if (rbBankBca.isChecked()) {
            //Set bank to BCA
            creditCard.setBank(BankType.BCA);
            // credit card payment using bank BCA need migs channel
            creditCard.setChannel(Channel.MIGS);
        } else if (rbBankMyBank.isChecked()) {
            //Set bank to Maybank
            creditCard.setBank(BankType.MAYBANK);
            // credit card payment using bank Maybank need migs channel
            creditCard.setChannel(Channel.MIGS);
        } else if (rbBankBri.isChecked()) {
            // Set bank to BRI
            creditCard.setBank(BankType.BRI);
            // credit card payment using bank BRI need migs channel
            creditCard.setChannel(Channel.MIGS);
        }

        if (preAuthActive.isChecked()) {
            // Set Pre Auth mode
            creditCard.setType(CardTokenRequest.TYPE_AUTHORIZE);
        }

        if (rbCardOneclick.isChecked()) {
            creditCard.setSecure(true);
            creditCard.setSaveCard(true);
        } else if (rbCardTwoclick.isChecked()) {
            creditCard.setSaveCard(true);
        } else {
            creditCard.setSecure(rb3dsSecure.isChecked());
            if (rb3dsSecure.isChecked()) {
                creditCard.setSecure(true);
            } else {
                creditCard.setSecure(false);
            }
        }

        String customField1 = etCustomField1.getText().toString();
        String customField2 = etCustomField2.getText().toString();
        String customField3 = etCustomField3.getText().toString();

        List<String> enabledPayments = new ArrayList<>();
        enabledPayments.add(PaymentType.CREDIT_CARD);
        enabledPayments.add(PaymentType.BANK_TRANSFER);
        enabledPayments.add(PaymentType.E_CHANNEL);

        List<Integer> integers = new ArrayList<>();
        integers.add(3);
        integers.add(6);
        integers.add(12);

        Map<String, List<Integer>> terms = new HashMap<>();
        terms.put(BankType.MANDIRI, integers);
        terms.put(BankType.BNI, integers);

        Installment installment = new Installment(true, terms);

        creditCard.setInstallment(installment);

        return CheckoutTokenRequest.newCompleteCheckout("rakawm-test1", null, null, creditCard, enabledPayments, itemDetailsList, customerDetails, checkoutOrderDetails, null, customField1, customField2, customField3);
    }
}
