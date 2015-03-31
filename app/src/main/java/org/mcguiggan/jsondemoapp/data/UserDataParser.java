package org.mcguiggan.jsondemoapp.data;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.mcguiggan.jsondemoapp.model.Friend;
import org.mcguiggan.jsondemoapp.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Parses JSON containing users into a UserList object.
 */
public class UserDataParser {

    /**
     * Parse the given string of JSON into a List of User objects.
     *
     * @param jsonString A String containing the raw JSON.
     * @return A List of Users.
     */
    public static List<User> parseUsersJson(String jsonString) {
        //To follow the parsing process through step-by-step, you can set a breakpoint here.
        List<User> userList = new ArrayList<>();

        try {
            //Create the Jackson parser which will parse the JSON string.
            JsonParser parser = new JsonFactory().createParser(jsonString);

            //The parser starts at a null token, so move the pointer to the first token,
            //which in this example should be START_ARRAY.
            parser.nextToken();

            //At the start of the loop, the parser's pointer is pointing at the START_ARRAY token.
            //On subsequent iterations, it's pointing at the END_OBJECT token from the last user object that was parsed.
            //In either case, we want to move on to make sure that the next token is START_OBJECT.
            //If it's END_ARRAY, we've finished the array of users.
            while (parser.nextToken() != JsonToken.END_ARRAY) {
                User parsedUser = parseUserJson(parser);
                userList.add(parsedUser);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return userList;
    }

    /**
     * From the current point in the parser's progress through the JSON string, parse the user object.
     * The parser should be passed in here with it's token pointer pointing at the START_OBJECT token for the user
     * being parsed.
     * <p/>
     * At the end of this method, the parser will be pointing at the END_OBJECT token for the user being parsed.
     *
     * @param parser The Jackson parser, initially pointing at the START_OBJECT token of the user being parsed.
     * @return The fully parsed User.
     * @throws IOException if there's a problem reading the JSON.
     */
    private static User parseUserJson(JsonParser parser) throws IOException {
        User user = new User();

        //On the first iteration, the parser's pointer is pointing at the START_OBJECT token indicating the start of
        //the user object.
        //On subsequent iterations, it's pointing at the last value that was read, or the end of the friends array.
        //In each case, we check the next token, and if it's a field, we'll carry on for another iteration,
        //but if it's the END_OBJECT token then we've finished parsing this user object.
        while (parser.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = parser.getCurrentName();
            parser.nextToken(); //we've read the field's name, lets move the parser on to the value now.
            switch (fieldName) {
                case "_id":
                    user.setId(parser.getValueAsString());
                    break;
                case "picture":
                    user.setPictureUrl(parser.getValueAsString());
                    break;
                case "email":
                    user.setEmail(parser.getValueAsString());
                    break;
                case "phone":
                    user.setPhoneNumber(parser.getValueAsString());
                    break;
                case "about":
                    user.setAbout(parser.getValueAsString());
                    break;
                case "friends":
                    user.setFriends(parseFriendsJson(parser));
                    break;
                default:
                    parser.skipChildren(); //We're not interested in this value, or any children of this value.
                    break;
            }
        }

        return user;
    }

    /**
     * From the current point in the parser's progress through the JSON string, parse the friends array.
     * The parser should be passed in here with it's token pointer pointing at the START_ARRAY token for the current
     * user's friends.
     * At the end of this method, the parser will be pointing at the END_ARRAY token for the user being parsed.
     *
     * @param parser The Jackson parser, initially pointing at the START_ARRAY token of the friends being parsed.
     * @return The fully parsed List of Friends.
     * @throws IOException if there's a problem reading the JSON.
     */
    private static List<Friend> parseFriendsJson(JsonParser parser) throws IOException {
        List<Friend> friends = new ArrayList<>();

        //On the first iteration, the parser's token pointer is pointing at the START_ARRAY token indicating the start
        //of the friends array.
        //On subsequent iterations, it's pointing at the END_OBJECT token of the last friend object that was parsed.
        //In each case, we check the next token, and if it's another friend object, we'll carry on,
        //but if it's the END_ARRAY token then we've finished parsing this array of friends.
        while (parser.nextToken() != JsonToken.END_ARRAY) {
            Friend friend = new Friend();
            friends.add(friend);

            //On the first iteration, the parser's pointer is pointing at the START_OBJECT token indicating the start
            //of the friend object.
            //On subsequent iterations, it's pointing at the last value that was read.
            //In each case, we check the next token, and if it's a field, we'll carry on,
            //but if it's the END_OBJECT token then we've finished parsing this friend object.
            while (parser.nextToken() != JsonToken.END_OBJECT) {
                String fieldName = parser.getCurrentName();
                parser.nextToken(); //The field's name has been read, move the parser on to the value token.
                switch (fieldName) {
                    case "id":
                        friend.setId(parser.getValueAsInt());
                        break;
                    case "name":
                        friend.setName(parser.getValueAsString());
                        break;
                    default:
                        parser.skipChildren(); //We're not interested in this value, or any children of this value.
                        break;
                }
            }
        }

        return friends;
    }
}
