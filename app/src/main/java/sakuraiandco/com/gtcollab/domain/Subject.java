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
public class Subject extends Entity implements Parcelable {

    public static final String BASE_URL = Constants.BASE_URL + "/subjects/";

    int id;
    String name;
    String code;
    String termName; // TODO: needed?
    boolean coursesLoaded;

    public static final Creator<Subject> CREATOR = new Creator<Subject>() {
        @Override
        public Subject createFromParcel(Parcel in) {
            return Subject.builder()
                    .id(in.readInt())
                    .name(in.readString())
                    .code(in.readString())
                    .termName(in.readString())
                    .coursesLoaded(in.readByte() != 0)
                    .build();
        }

        @Override
        public Subject[] newArray(int size) {
            return new Subject[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(code);
        dest.writeString(termName);
        dest.writeByte((byte) (coursesLoaded ? 1 : 0));
    }
}