# Udactiy_Popular_Movies

**How to use your own themoviesdb.org API key:**

1 - In build.gradle, add the following line to your dependencies:

```
buildTypes.each {
    it.buildConfigField "String", "MOVIES_TMDB_API_KEY", MyMovieDBApiKey
  }
  ```
  
2 - In gradle.properties, add the following:

```MyMovieDBApiKey="YOUR KEY HERE"```
