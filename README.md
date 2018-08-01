# Popular Movies Project

Signed APK file located in PopularMovies\app\release\app-release.apk
Install on android device or emulator to run. 

API key has been saved in properties.gradle which has not been committed. In order to run project enter your API key in the app gradle file in the API_KEY Variable in the buildConfigField method under defaultConfig .

If you do not have an API key create one at https://www.themoviedb.org/account/signup. 

## Project Overview
Most of us can relate to kicking back on the couch and enjoying a movie with friends and family. In this project, you’ll build an app to allow users to discover the most popular movies playing. We will split the development of this app in two stages.

## Part 1 Requirements
Your app will:

Upon launch, present the user with an grid arrangement of movie posters.
Allow your user to change sort order via a setting:
The sort order can be by most popular, or by top rated
Allow the user to tap on a movie poster and transition to a details screen with additional information such as:
original title
movie poster image thumbnail
A plot synopsis (called overview in the api)
user rating (called vote_average in the api)
release date

## Part 2 Requirements
In this stage you’ll add additional functionality to the app you built in Stage 1.

You’ll add more information to your movie details view:

You’ll allow users to view and play trailers ( either in the youtube app or a web browser).
You’ll allow users to read reviews of a selected movie.
You’ll also allow users to mark a movie as a favorite in the details view by tapping a button(star). This is for a local movies collection that you will maintain and does not require an API request*.
You’ll modify the existing sorting criteria for the main view to include an additional pivot to show their favorites collection.