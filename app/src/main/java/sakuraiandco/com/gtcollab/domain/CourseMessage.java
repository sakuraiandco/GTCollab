package sakuraiandco.com.gtcollab.domain;

import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.DateTime;

import lombok.Builder;
import lombok.Data;
import sakuraiandco.com.gtcollab.constants.Constants;

/**
 * Created by kaliq on 10/14/2017.
 */

@Data
@Builder
public class CourseMessage extends Entity implements Parcelable {

    public static final String BASE_URL = Constants.BASE_URL + "/course-messages/";

    int id;
    String content;
    int courseId;
    User creator;
    DateTime timestamp;

    public static final Creator<CourseMessage> CREATOR = new Creator<CourseMessage>() {
        @Override
        public CourseMessage createFromParcel(Parcel in) {
            return CourseMessage.builder()
                    .id(in.readInt())
                    .content(in.readString())
                    .courseId(in.readInt())
                    .creator((User) in.readParcelable(User.class.getClassLoader()))
                    .timestamp(new DateTime(in.readString()))
                    .build();
        }

        @Override
        public CourseMessage[] newArray(int size) {
            return new CourseMessage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(content);
        dest.writeInt(courseId);
        dest.writeParcelable(creator, flags);
        dest.writeString(timestamp.toString());
    }
}
