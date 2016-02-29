# SquawkerRedux-Android
Improved Twitter client for Android

Time spent: 15 hours in total

### Completed user stories:

The following **required** functionality is completed:

- [x] Includes **all required user stories** from Week 3 Twitter Client
- [x] User can switch between Timeline and Mention views using tabs
  - [x] User can view their home timeline tweets.
  - [x] User can view the recent mentions of their username.
- [x] User can navigate to **view their own profile**
  - [x] User can see picture, tagline, # of followers, # of following, and tweets on their profile.
- [x] User can **click on the profile image** in any tweet to see **another user's** profile.
 - [x] User can see picture, tagline, # of followers, # of following, and tweets of clicked user.
 - [x] Profile view includes that user's timeline
- [x] User can infinitely paginate any of these timelines (home, mentions, user) by scrolling to the bottom

The following **optional** functionality is completed:
- [ ] Robust error handling, check if internet is available, handle error cases, network failures
- [ ] When a network request is sent, user sees an indeterminate progress indicator
- [x] User can "reply" to any tweet on their home timeline
  - [x] The user that wrote the original tweet is automatically "@" replied in compose
- [x] User can click on a tweet to be taken to a "detail view" of that tweet
- [x] User can take favorite (and unfavorite) or retweet actions on a tweet
- [x] Improve the user interface and theme the app to feel "twitter branded"
- [ ] User can search for tweets matching a particular query and see results
- [ ] Use Parcelable instead of Serializable using the popular Parceler library
- [x] Apply the popular Butterknife annotation library to reduce view boilerplate
- [ ] User can view their Twitter direct messages (and/or send new ones)

### Walkthrough for all user stories:

![Video Walkthrough](img/squawker.gif)

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
