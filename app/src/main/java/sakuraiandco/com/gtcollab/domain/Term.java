package sakuraiandco.com.gtcollab.domain;

import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.LocalDate;

import lombok.Builder;
import lombok.Data;
import sakuraiandco.com.gtcollab.constants.Constants;

/**
 * Created by kaliq on 10/14/2017.
 */

@Data
@Builder
public class Term extends Entity implements Parcelable {

    public static final String BASE_URL = Constants.BASE_URL + "/terms/";

    int id;
    String name;
    String code;
    LocalDate startDate;
    LocalDate endDate;
    boolean subjectsLoaded; // TODO

    public static final Creator<Term> CREATOR = new Creator<Term>() {
        @Override
        public Term createFromParcel(Parcel in) {
            return Term.builder()
                    .id(in.readInt())
                    .name(in.readString())
                    .code(in.readString())
                    .startDate(new LocalDate(in.readString()))
                    .endDate(new LocalDate(in.readString()))
                    .subjectsLoaded(in.readByte() != 0)
                    .build();
        }

        @Override
        public Term[] newArray(int size) {
            return new Term[size];
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
        dest.writeString(startDate.toString());
        dest.writeString(endDate.toString());
        dest.writeByte((byte) (subjectsLoaded ? 1 : 0));
    }
}
