package com.example.testapp2021;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class ContactInfo extends AppCompatActivity {

    TextView tvChar,tvName,tvEditPanel;
    ImageView ivCall, ivMail, ivEdit, ivDelete;
    EditText etName, etMail, etNumber;
    Button btnSubmit;

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;
    boolean flagEdit = false;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);

        tvChar = findViewById(R.id.tvChar);
        tvName = findViewById(R.id.tvName);
        tvEditPanel = findViewById(R.id.tvEditPanel);

        ivCall = findViewById(R.id.ivCall);
        ivMail = findViewById(R.id.ivMail);
        ivEdit = findViewById(R.id.ivEdit);
        ivDelete = findViewById(R.id.ivDelete);

        etName = findViewById(R.id.etName);
        etMail = findViewById(R.id.etEmail);
        etNumber = findViewById(R.id.etMobileNum);

        btnSubmit = findViewById(R.id.btnSubmit);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        etName.setVisibility(View.GONE);
        etMail.setVisibility(View.GONE);
        etNumber.setVisibility(View.GONE);
        btnSubmit.setVisibility(View.GONE);
        tvEditPanel.setVisibility(View.GONE);

       index= getIntent().getIntExtra("index",0);

        etName.setText(TestApplication.contacts.get(index).getName());
        etMail.setText(TestApplication.contacts.get(index).getEmail());
        etNumber.setText(TestApplication.contacts.get(index).getNumber());

        tvChar.setText(TestApplication.contacts.get(index).getName().toUpperCase().charAt(0)+ "");
        tvName.setText(TestApplication.contacts.get(index).getName());

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });

            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    public void onClickCall(View view) {
        String uri = "tel: " + TestApplication.contacts.get(index).getNumber();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(uri));
        startActivity(intent);
    }

    public void onClickMail(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_EMAIL,TestApplication.contacts.get(index).getEmail());
        startActivity(Intent.createChooser(intent,"Send mail to: "+TestApplication.contacts.get(index).getName()));
    }

    public void onClickEdit(View view) {
        flagEdit = !flagEdit;

        if (flagEdit) {
            tvEditPanel.setVisibility(View.VISIBLE);
            etName.setVisibility(View.VISIBLE);
            etMail.setVisibility(View.VISIBLE);
            etNumber.setVisibility(View.VISIBLE);
            btnSubmit.setVisibility(View.VISIBLE);
        }

        else {
            tvEditPanel.setVisibility(View.GONE);
            etName.setVisibility(View.GONE);
            etMail.setVisibility(View.GONE);
            etNumber.setVisibility(View.GONE);
            btnSubmit.setVisibility(View.GONE);
        }

    }

    public void onClickDelete(View view) {

        //final AlertDialog.Builder builder = new AlertDialog.Builder(ContactInfo.this);
        AlertDialog.Builder builder = new AlertDialog.Builder(ContactInfo.this);

        builder.setMessage("Are you sure you want to delete this contact?");

        builder.setPositiveButton("YES, PLEASE!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showProgress(true);
                tvLoad.setText("Deleting the contact, please wait..");

                Backendless.Persistence.of(Contact.class).remove(TestApplication.contacts.get(index), new AsyncCallback<Long>() {
                    @Override
                    public void handleResponse(Long response) {
                        TestApplication.contacts.remove(index);
                        Toast.makeText(ContactInfo.this, "Contact successfully removed!", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        ContactInfo.this.finish();
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toast.makeText(ContactInfo.this, "Error: "+fault.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        builder.setNegativeButton("NO!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void onClickSubmit(View view) {
        if (etName.getText().toString().isEmpty() || etNumber.getText().toString().isEmpty()
        || etMail.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please dont press submit if any detail is kept empty!", Toast.LENGTH_LONG).show();
        }
        else {
            String name = etName.getText().toString().trim();
            String number = etNumber.getText().toString().trim();
            String mail = etMail.getText().toString().trim();

            TestApplication.contacts.get(index).setName(name);
            TestApplication.contacts.get(index).setNumber(number);
            TestApplication.contacts.get(index).setEmail(mail);
           // tvChar.setText(TestApplication.contacts.get(index).getName().toUpperCase().charAt(0)+"");
            //tvName.setText(TestApplication.contacts.get(index).getName());

            showProgress(true);
            tvLoad.setText("Updating your contact, please wait..");

            Backendless.Persistence.save(TestApplication.contacts.get(index), new AsyncCallback<Contact>() {
                @Override
                public void handleResponse(Contact response) {
                    tvChar.setText(TestApplication.contacts.get(index).getName().toUpperCase().charAt(0)+"");
                    tvName.setText(TestApplication.contacts.get(index).getName());

                    Toast.makeText(ContactInfo.this, "Contact successfully Updated!",
                            Toast.LENGTH_LONG).show();

                  /*  Context context = ContactInfo.this;
                    CharSequence text = "Contact successfully Updated!";
                    int duration = (int) 50.0;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();*/
                    showProgress(false);
                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    Toast.makeText(ContactInfo.this, "Error: "+fault.getMessage(), Toast.LENGTH_SHORT).show();
                    showProgress(false);
                }
            });
        }
    }
}