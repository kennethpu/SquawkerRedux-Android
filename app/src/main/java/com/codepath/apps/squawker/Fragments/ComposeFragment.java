package com.codepath.apps.squawker.Fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.squawker.Activities.MainActivity;
import com.codepath.apps.squawker.Models.Tweet;
import com.codepath.apps.squawker.R;
import com.codepath.apps.squawker.SquawkerApplication;
import com.codepath.apps.squawker.SquawkerClient;
import com.codepath.apps.squawker.UserStorage;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by kpu on 2/21/16.
 */
public class ComposeFragment extends DialogFragment{

    private static final String ARG_PRETEXT = "ARG_PRETEXT";

    @Bind(R.id.rivAuthorImage)
    ImageView rivAuthorImage;

    @Bind(R.id.tvAuthorFullName)
    TextView tvAuthorFullName;

    @Bind(R.id.tvAuthorScreenName)
    TextView tvAuthorScreenName;

    @Bind(R.id.ibClear)
    ImageButton ibClear;

    @Bind(R.id.etBody)
    EditText etBody;

    @Bind(R.id.btnTweet)
    Button btnTweet;

    @Bind(R.id.tvCharCount)
    TextView tvCharCount;

    private SquawkerClient client;

    public ComposeFragment() {
        // Empty constructor is required for DialogFragment
    }

    public static ComposeFragment newInstance(String preText) {
        ComposeFragment frag = new ComposeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PRETEXT, preText);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compose_tweet, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        // Set up click listener to dismiss dialog
        ibClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        UserStorage userStorage = new UserStorage(getActivity().getApplicationContext());
        String imageUrl = userStorage.getImageUrl();
        rivAuthorImage.setImageResource(0);
        Glide.with(getContext()).load(imageUrl).into(rivAuthorImage);

        String fullName = userStorage.getFullName();
        tvAuthorFullName.setText(fullName);

        String screenName = userStorage.getScreenName();
        tvAuthorScreenName.setText("@" + screenName);

        // Listen for text change so we can update remaining characters count
        etBody.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String remainingChars = String.format("%d", 140 - s.length());
                tvCharCount.setText(remainingChars);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        String preText = getArguments().getString(ARG_PRETEXT, "");
        etBody.setText(preText);

        // Show soft keyboard automatically and request focus to field
        etBody.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        // Set up click listener to post tweet
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SquawkerApplication.getRestClient().postTweet(etBody.getText().toString(), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Tweet tweet = Tweet.fromJSON(response);
                        ((MainActivity) getActivity()).insertTweet(tweet);
                        getDialog().dismiss();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.d("DEBUG", errorResponse.toString());
                        Toast.makeText(getActivity().getApplicationContext(), "Posting Tweet Failed!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
