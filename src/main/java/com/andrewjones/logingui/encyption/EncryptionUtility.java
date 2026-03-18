package com.andrewjones.logingui.encyption;

import at.favre.lib.crypto.bcrypt.BCrypt;

public final class EncryptionUtility {

    public static final int HASH_COST = 10;

    public static final BCrypt.Version HASH_VERSION = BCrypt.Version.VERSION_2Y;

    public static final BCrypt.Hasher HASHER = BCrypt.with(HASH_VERSION);

    public static final BCrypt.Verifyer VERIFIER = BCrypt.verifyer(HASH_VERSION);

}
