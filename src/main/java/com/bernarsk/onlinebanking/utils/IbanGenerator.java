package com.bernarsk.onlinebanking.utils;


import java.math.BigInteger;
import java.util.Random;
import org.iban4j.Iban;
import org.iban4j.CountryCode;

public class IbanGenerator {
    public static String generateIban() {
        BigInteger maxLimit = new BigInteger("10000000000000");
        BigInteger accountNumber = new BigInteger(maxLimit.bitLength(), new Random());
        accountNumber = accountNumber.mod(maxLimit);
        String accountNumberString = String.format("%013d", accountNumber);
        System.out.println(accountNumber);
        Iban iban = new Iban.Builder()
                .countryCode(CountryCode.LV)
                .bankCode("JAVT")
                .accountNumber(accountNumberString)
                .build();
        System.out.println(iban.toString());
        return iban.toString();
    }
}
