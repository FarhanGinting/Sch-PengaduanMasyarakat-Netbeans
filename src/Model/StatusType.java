package Model;

import Koneksi.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public enum StatusType {
    BELUM, PROSES, SELESAI;

    private static final Map<String, StatusType> stringToEnum = new HashMap<>();

    static {
        for (StatusType statusType : values()) {
            stringToEnum.put(statusType.name().toLowerCase(), statusType);
        }
    }

    public static StatusType fromString(String statusString) {
        return stringToEnum.get(statusString.toLowerCase());
    }

    public static StatusType bacaStatusDariDatabase(String statusString) {
        StatusType status = null;

        switch (statusString) {
            case "0":
                status = BELUM;
                break;
            case "proses":
                status = PROSES;
                break;
            case "selesai":
                status = SELESAI;
                break;
            default:
                // Nilai default jika tidak sesuai
                status = null;
        }

        return status;
    }
}
