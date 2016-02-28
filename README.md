# Squawker-android
Simple Twitter client for Android

Time spent: 20 hours in total

### Completed user stories:

#### Auth Flow
- [x] Required: User can sign in to Twitter using OAuth login
 
#### Viewing Tweets
- [x] Required: User can view the username, name, and body for each tweet
- [x] Required: User can view the relative timestamp for each tweet
- [x] Required: User can view more tweets as they scroll with infinite pagination
- [x] Optional: Links in tweets are clickable and will launch the web browser
- [x] Optional: User can refresh tweets timeline by pulling down to refresh
- [x] Optional: User can tap a tweet to display a "detailed" view of that tweet
- [x] Optional: User can select "reply" from detail view to respond to a tweet
- [x] Optional: Improve the user interface and theme the app to feel "twitter branded"
- [x] Optional: User can see embedded image media for each tweet

#### Composing Tweets
- [x] Required: User can click a “Compose” icon in the Action Bar on the top right
- [x] Required: User can then enter a new tweet and post this to twitter
- [x] Required: User is taken back to home timeline with new tweet visible in timeline
- [x] Optional: While composing a tweet, user can see a character counter with characters remaining for tweet out of 140
- [x] Optional: Compose activity is replaced with a modal overlay
- [x] Additional: User is unable to enter any new characters after limit is reached

#### Miscellaneous
- [x] Optional: Use Parcelable instead of Serializable
- [x] Optional: Apply the popular Butterknife annotation library to reduce view boilerplate
- [x] Optional: Replace Picasso with Glide for more efficient image rendering
- [x] Additional: User can switch between Timeline and Mention views using tabs
- [x] Additional: User can favorite/unfavorite tweets
- [x] Additional: User can retweet tweets

### Walkthrough for all user stories:

#### Auth Flow
![Auth Flow](img/squawker_auth.gif)

#### Viewing Tweets
![Video Walkthrough](img/squawker_use.gif)

#### Composing Tweets
![Composing Tweets](img/squawker_compose.gif)

#### Extra Functionality
![Extra Functionality](img/squawker_extra.gif)

GIFs created with [LiceCap](http://www.cockos.com/licecap/).

Credits
---------
* [Twitter API](https://dev.twitter.com/rest/public)
* [android-async-http](http://loopj.com/android-async-http/)
* [Glide](https://github.com/bumptech/glide)
* [Butterknife](http://jakewharton.github.io/butterknife/)
* [PagerSlidingTabStrip](https://github.com/astuetz/PagerSlidingTabStrip)
* [RoundedImageView](https://github.com/vinc3m1/RoundedImageView)
* [Twitter Brand Resources](https://dev.twitter.com/overview/general/image-resources)
* [Google Material Design Icons](https://design.google.com/icons/)
