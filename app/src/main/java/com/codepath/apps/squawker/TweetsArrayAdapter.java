package com.codepath.apps.squawker;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.squawker.Models.Tweet;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by kpu on 2/20/16.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

    private ITweetActionsListener mListener;

    static class ViewHolder {
        @Bind(R.id.rivProfileImage)
        RoundedImageView rivProfileImage;

        @Bind(R.id.tvFullName)
        TextView tvFullName;

        @Bind(R.id.tvScreenName)
        TextView tvScreenName;

        @Bind(R.id.tvTimeStamp)
        TextView tvTimeStamp;

        @Bind(R.id.tvBody)
        LinkifiedTextView tvBody;

        @Bind(R.id.rivMedia)
        RoundedImageView rivMedia;

        @Bind(R.id.ibReply)
        ImageButton ibReply;

        @Bind(R.id.ibRetweet)
        ImageButton ibRetweet;

        @Bind(R.id.tvRetweetCount)
        TextView tvRetweetCount;

        @Bind(R.id.ibLike)
        ImageButton ibLike;

        @Bind(R.id.tvLikeCount)
        TextView tvLikeCount;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface ITweetActionsListener {
        void replyTweet(int position);
        void retweetTweet(int position);
        void favoriteTweet(int position);
        void unFavoriteTweet(int position);
    }

    public TweetsArrayAdapter(Context context, List<Tweet> tweets, ITweetActionsListener listener) {
        super(context, android.R.layout.simple_list_item_1, tweets);

        mListener = listener;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        final Tweet tweet = getItem(position);

        viewHolder.rivProfileImage.setImageResource(0);
        Glide.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(viewHolder.rivProfileImage);
        viewHolder.tvFullName.setText(tweet.getUser().getFullName());
        viewHolder.tvScreenName.setText("@" + tweet.getUser().getScreenName());
        viewHolder.tvTimeStamp.setText(getRelativeTimeAgo(tweet.getCreatedAt()));
        viewHolder.tvBody.setText(tweet.getBody());

        // Configure media image
        if (tweet.getMediaUrl() != null) {
            viewHolder.rivMedia.setVisibility(View.VISIBLE);
            viewHolder.rivMedia.setImageResource(0);
            Glide.with(getContext()).load(tweet.getMediaUrl()).into(viewHolder.rivMedia);
        } else {
            viewHolder.rivMedia.setVisibility(View.GONE);
        }

        // Configure reply button click action
        viewHolder.ibReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.replyTweet(position);
            }
        });

        // Configure retweet section UI
        configureViewHolderRetweetsForTweet(viewHolder, tweet);

        // Configure retweet button click action
        viewHolder.ibRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSelected = viewHolder.ibRetweet.isSelected();
                if (!isSelected) {
                    mListener.retweetTweet(position);
                }
            }
        });

        // Configure like section UI
        configureViewHolderLikesForTweet(viewHolder, tweet);

        // Configure like button click action
        viewHolder.ibLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSelected = viewHolder.ibLike.isSelected();
                if (isSelected) {
                    mListener.unFavoriteTweet(position);
                } else {
                    mListener.favoriteTweet(position);
                }
            }
        });

        return convertView;
    }

    private String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            String relativeDateString = DateUtils.getRelativeTimeSpanString(dateMillis, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
            String[] stringComponents = relativeDateString.split(" ");
            relativeDate = stringComponents[0] + stringComponents[1].charAt(0);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    private void configureViewHolderLikesForTweet(ViewHolder viewHolder, Tweet tweet) {
        // Configure like button
        viewHolder.ibLike.setSelected(tweet.isFavorited());

        // Configure like count text
        int likeCount = tweet.getFavoriteCount();
        viewHolder.tvLikeCount.setText(String.valueOf(likeCount));
        int likeTextColor = tweet.isFavorited() ? R.color.like_red : R.color.icon_default;
        viewHolder.tvLikeCount.setTextColor(ContextCompat.getColor(getContext(), likeTextColor));
        int likeTextVisibility = likeCount > 0 ? View.VISIBLE : View.INVISIBLE;
        viewHolder.tvLikeCount.setVisibility(likeTextVisibility);
    }

    private void configureViewHolderRetweetsForTweet(ViewHolder viewHolder, Tweet tweet) {
        // Configure retweet button
        UserStorage userStorage = new UserStorage(getContext());
        if (userStorage.getUserId() == tweet.getUser().getuId()) {
            viewHolder.ibRetweet.setEnabled(false);
        } else {
            viewHolder.ibRetweet.setEnabled(true);
            viewHolder.ibRetweet.setSelected(tweet.isRetweeted());
        }

        // Configure retweet count text
        int retweetCount = tweet.getRetweetCount();
        viewHolder.tvRetweetCount.setText(String.valueOf(retweetCount));
        int retweetTextColor = tweet.isRetweeted() ? R.color.retweet_green : R.color.icon_default;
        viewHolder.tvRetweetCount.setTextColor(ContextCompat.getColor(getContext(), retweetTextColor));
        int retweetTextVisibility = retweetCount > 0 ? View.VISIBLE : View.INVISIBLE;
        viewHolder.tvRetweetCount.setVisibility(retweetTextVisibility);
    }
}
