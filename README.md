# Parsing JSON in Android Using Jackson

This recipe shows how to use [Jackson][jackson] in an Android app to parse a JSON response from a server.

[![Build Status](https://img.shields.io/travis/neilmcguiggan/JSONDemoApp.svg)](https://travis-ci.org/neilmcguiggan/JSONDemoApp)

## Add dependencies

The first step in using Jackson is to import the libraries from Maven Central. I'm just using the [core][jackson-core] library here as there's nothing especially complicated involved, but the other main ones are the [databind][jackson-databind] library for binding JSON to Java objects, and the [annotations][jackson-annotations] library containing the core annotations for Jackson.

I'm using [OkHttp][okhttp] for networking, so include that too if you want to follow this tutorial exactly.

```
//Jackson libraries for JSON parsing
compile 'com.fasterxml.jackson.core:jackson-core:2.5.1'

//okhttp client for network requests
compile 'com.squareup.okhttp:okhttp:2.3.0'
```

## Make the request

From whichever class requires the data, make an asynchronous request to the Data Manager class, in this case `UserManager`:

```
UserManager.getInstance().requestUsers(this);
```

The parameter `this` is a `UserManagerListener`, used for receiving callbacks when the data is loaded. This is a pretty common pattern in Android, even referenced in [Google's own tutorials][callbacks]. It lets you make the request asynchronously, meaning you can get on with other things while the data loads. Here's the interface:

```
/**
 * Interface to be implemented by any class that requests data from the UserManager.
 */
public interface UserManagerListener {
    /**
     * Callback for when the social data has been fetched.
     *
     * @param userList The list of users that has been loaded.
     */
    void onUserDataLoaded(List<User> userList);
}
```

And this is my implementation in the `MainActivity` class:

```
@Override
public void onUserDataLoaded(List<User> userList) {
    //the userList is loaded, do something with it.
}
```

## Data Manager class

If you've used OkHttp before, the `requestUsers` method should be really easy to understand. Thankfully, even if you've not used OkHttp before, it should be easy to understand as it's a really clear API. The interesting part in this tutorial is here:

```
List<User> userList = UserDataParser.parseUsersJson(responseString);
listener.onUserDataLoaded(userList);
```

The first line sends the String version of the JSON response to the UserDataParser, getting a List of User objects back. The second line then sends it back to the `listener` object who made the request in the first place. This completes the asynchronous request.

## Data Parser class

This is the key part of this example project: how to parse the JSON data using Jackson.

The main class used from Jackson is the [JsonParser][jsonparser]. Creating the `JsonParser` is very straightforward, just pass the String of JSON data into the `createParser` method of `JsonFactory`.

```
JsonParser parser = new JsonFactory().createParser(jsonString);
```

The parser works by breaking the JSON down into [JsonTokens][jsontokens]. This represents key elements like the start or end of an array or object, fields, and values. The approach taken here is to iterate over the tokens, until we reach the end:

```
while (parser.nextToken() != JsonToken.END_ARRAY) {
    User parsedUser = parseUserJson(parser);
    userList.add(parsedUser);
}
```

In this case, I'm parsing an array of user objects, so the last token I should receive is an `END_ARRAY` token.

There is a sub-loop in the `parseUserJson` method which iterates over each token in the `user` object. This is where I look at the keys and values in the JSON. `parser.getCurrentName();` will get the name of the field it's currently pointing at. The parser then moves on to the next token, the value. Using a switch statement to define each of the keys we're interested in, the value can then be passed into a User model object. And if the key isn't one that we're interested in, we can tell the parser to skip all children of this key and move on to the next key. This means we don't have to waste time or resources on every field that we're not interested in.

```
while (parser.nextToken() != JsonToken.END_OBJECT) {
    String fieldName = parser.getCurrentName();
    parser.nextToken(); //we've read the field's name, lets move the parser on to the value now.
    switch (fieldName) {
        case "_id":
            user.setId(parser.getValueAsString());
            break;
        ...
        default:
            parser.skipChildren(); //We're not interested in this value, or any children of this value.
            break;
    }
}
```

And that's it! We now have a fully parsed model object, built from the JSON that was received from the server.

[jackson]: https://github.com/FasterXML/jackson "GitHub: FasterXML/jackson"
[jackson-core]: https://github.com/FasterXML/jackson-core "GitHub: FasterXML/jackson-core"
[jackson-databind]: https://github.com/FasterXML/jackson-databind "GitHub: FasterXML/jackson-databind"
[jackson-annotations]: https://github.com/FasterXML/jackson-annotations "GitHub: FasterXML/jackson-annotations"
[okhttp]: http://square.github.io/okhttp/ "OkHttp"
[callbacks]: http://developer.android.com/training/basics/fragments/communicating.html "Communicating with Other Fragments"
[jsonparser]: https://github.com/FasterXML/jackson-core/blob/master/src/main/java/com/fasterxml/jackson/core/JsonParser.java "JsonParser.java"
[jsontokens]: https://github.com/FasterXML/jackson-core/blob/master/src/main/java/com/fasterxml/jackson/core/JsonToken.java "JsonToken.java"