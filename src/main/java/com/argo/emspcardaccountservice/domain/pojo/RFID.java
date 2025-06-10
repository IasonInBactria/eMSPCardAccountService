package com.argo.emspcardaccountservice.domain.pojo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 保护的无参构造函数
@AllArgsConstructor // 全参构造函数
public class RFID implements Serializable {

    private String uid; // RFID的UID
    private String visibleNumber; // RFID的可见号码

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RFID rfid = (RFID) o;
        return Objects.equals(uid, rfid.uid) && Objects.equals(visibleNumber, rfid.visibleNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, visibleNumber);
    }

    @Override
    public String toString() {
        return "RFID{" +
               "uid='" + uid + '\'' +
               ", visibleNumber='" + visibleNumber + '\'' +
               '}';
    }
}
