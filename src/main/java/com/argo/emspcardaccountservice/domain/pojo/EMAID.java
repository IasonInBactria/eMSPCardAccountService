package com.argo.emspcardaccountservice.domain.pojo;

import com.argo.emspcardaccountservice.domain.exception.InvalidEMAIDFormatException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 保护的无参构造函数
public class EMAID implements Serializable {

    private static final String EMAID_REGEX = "^[A-Z]{2}-[A-Z0-9]{3}-[A-Z0-9]{5,10}-[A-Z0-9]{1,9}$";
    private static final Pattern EMAID_PATTERN = Pattern.compile(EMAID_REGEX);

    private String value; // 存储EMAID的字符串值

    private EMAID(String value) {
        if (!isValid(value)) {
            throw new InvalidEMAIDFormatException("Invalid EMAID format: " + value);
        }
        this.value = value;
    }

    public static EMAID of(String value) {
        return new EMAID(value);
    }

    public static boolean isValid(String emaid) {
        return emaid != null && EMAID_PATTERN.matcher(emaid).matches();
    }

    public static EMAID generate() {
        // 示例：生成一个EMAID
        String countryCode = "DE";
        String providerId = "HUB";

        String rfidIdentifier = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        String internalIdentifier = String.valueOf(ThreadLocalRandom.current().nextLong(1000, 999999999L));
        internalIdentifier = internalIdentifier.substring(0, Math.min(internalIdentifier.length(), 9));

        String generatedEmaid = String.format("%s-%s-%s-%s", countryCode, providerId, rfidIdentifier, internalIdentifier);
        return new EMAID(generatedEmaid);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EMAID emaid = (EMAID) o;
        return Objects.equals(value, emaid.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
