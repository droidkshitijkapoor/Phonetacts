package com.example.testapp2021;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class NewContact extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    EditText etContactName, etContactNumber, etContactMail;
    Button btnCreateContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        etContactMail = findViewById(R.id.etContactMail);
        etContactName = findViewById(R.id.etContactName);
        etContactNumber = findViewById(R.id.etContactNum);
        btnCreateContact = findViewById(R.id.btnCreateContact);

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

    public void onClickNewContact(View view) {
        if (etContactNumber.getText().toString().isEmpty() || etContactName.getText().toString().isEmpty()
        || etContactMail.getText().toString().isEmpty()) {

            Toast.makeText(this, "Fill all the contact details!", Toast.LENGTH_LONG).show();
        }

        else {
            String name = etContactName.getText().toString().trim();
            String mail = etContactMail.getText().toString().trim();
            String number = etContactNumber.getText().toString().trim();

            Contact contact = new Contact();
            contact.setEmail(mail);
            contact.setName(name);
            contact.setNumber(number);

            //setting up the user to which that contact belongs.
            contact.setUserEmail(TestApplication.user.getEmail());

            showProgress(true);
            tvLoad.setText("Creating your new contact, kindly hold..");

            Backendless.Persistence.save(contact, new AsyncCallback<Contact>() {
                @Override
                public void handleResponse(Contact response) {
                    Toast.makeText(NewContact.this, response.getName() + " " + "contact is saved for email id:- "
                            + response.getUserEmail(), Toast.LENGTH_LONG).show();
                    showProgress(false);
                    etContactMail.setText("");
                    etContactName.setText("");
                    etContactNumber.setText("");
                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    Toast.makeText(NewContact.this, "Error: "+fault.getMessage(), Toast.LENGTH_LONG).show();
                    showProgress(false);
                }
            });
        }
    }
}