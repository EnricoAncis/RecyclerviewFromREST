# Recyclerview from REST
Demo Android project that populates a recicleview from a REST call.
The call returns a JSON of post list from http://www.mocky.io/v2/59f2e79c2f0000ae29542931.

Every post has got id, user_id, title, description, image and published date .
The app view had a taggle buttom filter, when the toggle is disabled (the default state) all posts should be shown, when it is enabled (after a tap) the list should only show the posts having user_id set to 1 and sorted by descending published_at. Tapping on the button again will return it to its default state.

The project shows a easy data model architeture ad uses a SQLite db to save the post list and optimize the app launching and events that can kill and recreate the activity. The db save the state of the filter too.
Every time the user wants to update the posts he can tap in the refresh buttom.

The management of the REST architetture is realized with Restrofit library and Picasso for the images.
