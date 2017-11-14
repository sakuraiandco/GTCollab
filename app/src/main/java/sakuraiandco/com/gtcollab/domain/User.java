package sakuraiandco.com.gtcollab.domain;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Builder;
import lombok.Data;
import sakuraiandco.com.gtcollab.constants.Constants;

/**
 * Created by kaliq on 10/14/2017.
 */

@Data
@Builder
public class User extends Entity implements Parcelable {

    public static final String BASE_URL = Constants.BASE_URL + "/users/";

    int id;
    String username;
    String password;
    String firstName;
    String lastName;
    String email;

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return User.builder()
                    .id(in.readInt())
                    .username(in.readString())
                    .password(in.readString())
                    .firstName(in.readString())
                    .lastName(in.readString())
                    .email(in.readString())
                    .build();
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeInt(id);
        parcel.writeString(username);
        parcel.writeString(password);
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        parcel.writeString(email);
    }
}